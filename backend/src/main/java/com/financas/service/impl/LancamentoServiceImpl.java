package com.financas.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.financas.enums.StatusLancamento;
import com.financas.enums.TipoLancamento;
import com.financas.exception.RegraNegocioException;
import com.financas.model.Lancamento;
import com.financas.repository.LancamentoRepository;
import com.financas.service.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService {

	private LancamentoRepository lancamentoRepository;
	
	public LancamentoServiceImpl(LancamentoRepository lancamentoRepository) {
		this.lancamentoRepository = lancamentoRepository;
	}
	
	@Override
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		validar(lancamento);
		
		lancamento.setStatus(StatusLancamento.PENDENTE);
		
		return lancamentoRepository.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		validar(lancamento);
		
		return lancamentoRepository.save(lancamento);
	}

	@Override
	@Transactional
	public void deletar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		
		lancamentoRepository.delete(lancamento);
	}

	@Override
	@Transactional(readOnly = true)
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
		Example example = Example.of(lancamentoFiltro, ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING));
		
		return lancamentoRepository.findAll(example);
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatus(status);
		
		atualizar(lancamento);
	}

	@Override
	public void validar(Lancamento lancamento) {
		if (lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")) {
			throw new RegraNegocioException("Informe uma descri????o v??lida");
		}
		
		if (lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12) {
			throw new RegraNegocioException("Informe um m??s v??lido");
		}
		
		if (lancamento.getAno() == null || lancamento.getAno().toString().length() != 4) {
			throw new RegraNegocioException("Informe um ano v??lido");
		}
		
		if (lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null) {
			throw new RegraNegocioException("Informe um usu??rio");
		}
		
		if (lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1) {
			throw new RegraNegocioException("Informe um valor v??lido");
		}
		
		if (lancamento.getTipo() == null) {
			throw new RegraNegocioException("Informe um tipo de lan??amento");
		}
	}

	@Override
	public Optional<Lancamento> obterPorId(Long id) {
		return lancamentoRepository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal obterSaldoPorUsuario(Long id) {
		BigDecimal receitas = lancamentoRepository.obterSaldoPorTipoLancamentoEUsuarioEStatus(id, TipoLancamento.RECEITA, StatusLancamento.EFETIVADO);
		BigDecimal despesas = lancamentoRepository.obterSaldoPorTipoLancamentoEUsuarioEStatus(id, TipoLancamento.DESPESA, StatusLancamento.EFETIVADO);
		
		if (receitas == null) {
			receitas = BigDecimal.ZERO;
		}
		
		if (despesas == null) {
			despesas = BigDecimal.ZERO;
		}
		
		return receitas.subtract(despesas);
	}

}
