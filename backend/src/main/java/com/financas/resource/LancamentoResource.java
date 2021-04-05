package com.financas.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	
	public LancamentoResource(LancamentoService lancamentoService) {
		this.lancamentoService = lancamentoService;
	}
	
	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoDTO lancamentoDTO) {
		
	}
	
	@SuppressWarnings("unused")
	private Lancamento converter(LancamentoDTO lancamentoDTO) {
		Lancamento lancamento = new Lancamento();
		lancamento.setId(lancamentoDTO.getId());
		lancamento.setDescricao(lancamentoDTO.getDescricao());
		lancamento.setMes(lancamentoDTO.getMes());
		lancamento.setAno(lancamentoDTO.getAno());
		lancamento.setValor(lancamentoDTO.getValor());
		
		Usuario usuario = usuarioService.obterPorId(lancamentoDTO.getUsuario()).orElseThrow(() -> new RegraNegocioException("Usuário não encontrado para o ID informado"));
		
		lancamento.setUsuario(usuario);
		lancamento.setTipo(TipoLancamento.valueOf(lancamentoDTO.getTipo()));
		lancamento.setStatus(StatusLancamento.valueOf(lancamentoDTO.getStatus()));
		
		return lancamento;
	}
}
