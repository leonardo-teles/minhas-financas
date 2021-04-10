package com.financas.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.financas.dto.AtualizaStatusDTO;
import com.financas.dto.LancamentoDTO;
import com.financas.enums.StatusLancamento;
import com.financas.enums.TipoLancamento;
import com.financas.exception.RegraNegocioException;
import com.financas.model.Lancamento;
import com.financas.model.Usuario;
import com.financas.service.LancamentoService;
import com.financas.service.UsuarioService;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoResource {

	private LancamentoService lancamentoService;
	
	private UsuarioService usuarioService;
	
	public LancamentoResource(LancamentoService lancamentoService, UsuarioService usuarioService) {
		this.lancamentoService = lancamentoService;
		this.usuarioService = usuarioService;
	}
	
	@GetMapping
	@SuppressWarnings("rawtypes")
	public ResponseEntity buscar(
								 @RequestParam(value = "descricao", required = false) String descricao,
								 @RequestParam(value = "mes", required = false) Integer mes,
								 @RequestParam(value = "ano", required = false) Integer ano,
								 @RequestParam("usuario") Long idUsuario) {
		
		Lancamento lancamentoFiltro = new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);
		
		Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
		if (!usuario.isPresent()) {
			return ResponseEntity.badRequest().body("Não foi possível realizar a consulta. Usuário não encontrado");
		} else {
			lancamentoFiltro.setUsuario(usuario.get());
		}
		
		List<Lancamento> lancamentos = lancamentoService.buscar(lancamentoFiltro);
		
		return ResponseEntity.ok(lancamentos);
	}
	
	@GetMapping("{id}")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ResponseEntity obterLancamento(@PathVariable("id") Long id) {
		return lancamentoService.obterPorId(id)
				.map(lancamento -> new ResponseEntity(converter(lancamento), HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity(HttpStatus.NOT_FOUND));
	}
	
	@PostMapping
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ResponseEntity salvar(@RequestBody LancamentoDTO lancamentoDTO) {
		try {
			Lancamento lancamento = converter(lancamentoDTO);
			lancamento = lancamentoService.salvar(lancamento);
			
			return new ResponseEntity(lancamento, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("{id}")
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDTO lancamentoDTO) {
		return lancamentoService.obterPorId(id).map(l -> {
			try {
				Lancamento lancamento = converter(lancamentoDTO);
				lancamento.setId(l.getId());
				lancamentoService.atualizar(lancamento);
				
				return ResponseEntity.ok(lancamento);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Lançamento não encontrado", HttpStatus.BAD_REQUEST));
	}
	
	@PutMapping("{id}/atualizar-status")
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ResponseEntity atualizarStatus(@PathVariable("id") Long id, @RequestBody AtualizaStatusDTO atualizaStatusDTO) {
		return lancamentoService.obterPorId(id).map(l -> {
			StatusLancamento statusSelecionado = StatusLancamento.valueOf(atualizaStatusDTO.getStatus());
			
			if (statusSelecionado == null) {
				return ResponseEntity.badRequest().body("Não foi possível atualizar o status do lançamento.");
			}
			try {
				l.setStatus(statusSelecionado);
				lancamentoService.atualizar(l);
				
				return ResponseEntity.ok(l);
				
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Lançamento não encontrado", HttpStatus.BAD_REQUEST));
	}
	
	@DeleteMapping("{id}")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ResponseEntity deletar(@PathVariable("id") Long id) {
		return lancamentoService.obterPorId(id).map(l -> {
			lancamentoService.deletar(l);
			
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(() -> new ResponseEntity("Lançamento não encontrado", HttpStatus.BAD_REQUEST));
	}
	
	private LancamentoDTO converter(Lancamento lancamento) {
		return LancamentoDTO.builder()
				.id(lancamento.getId())
				.descricao(lancamento.getDescricao())
				.valor(lancamento.getValor())
				.mes(lancamento.getMes())
				.ano(lancamento.getAno())
				.status(lancamento.getStatus().name())
				.tipo(lancamento.getTipo().name())
				.usuario(lancamento.getUsuario().getId())
				.build();
	}
	
	private Lancamento converter(LancamentoDTO lancamentoDTO) {
		Lancamento lancamento = new Lancamento();
		lancamento.setId(lancamentoDTO.getId());
		lancamento.setDescricao(lancamentoDTO.getDescricao());
		lancamento.setMes(lancamentoDTO.getMes());
		lancamento.setAno(lancamentoDTO.getAno());
		lancamento.setValor(lancamentoDTO.getValor());
		
		Usuario usuario = usuarioService.obterPorId(lancamentoDTO.getUsuario()).orElseThrow(() -> new RegraNegocioException("Usuário não encontrado para o ID informado"));
		
		lancamento.setUsuario(usuario);
		
		if (lancamentoDTO.getTipo() != null) {
			lancamento.setTipo(TipoLancamento.valueOf(lancamentoDTO.getTipo()));
		}
		
		if (lancamentoDTO.getStatus() != null) {
			lancamento.setStatus(StatusLancamento.valueOf(lancamentoDTO.getStatus()));
		}
		
		return lancamento;
	}
}
