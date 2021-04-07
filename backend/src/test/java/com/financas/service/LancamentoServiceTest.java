package com.financas.service;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.financas.enums.StatusLancamento;
import com.financas.exception.RegraNegocioException;
import com.financas.model.Lancamento;
import com.financas.repository.LancamentoRepository;
import com.financas.repository.LancamentoRepositoryTest;
import com.financas.service.impl.LancamentoServiceImpl;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class LancamentoServiceTest {
	
	@SpyBean
	LancamentoServiceImpl lancamentoServiceImpl;

	@MockBean
	LancamentoRepository lancamentoRepository;
	
	@Test
	public void deveSalvarUmLancamento() {
		//cenário
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarlancamento();
		Mockito.doNothing().when(lancamentoServiceImpl).validar(lancamentoASalvar);
		
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarlancamento();
		lancamentoSalvo.setId(1L);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		
		Mockito.when(lancamentoRepository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);
		
		//execução
		Lancamento lancamento =  lancamentoServiceImpl.salvar(lancamentoASalvar);
		
		//verificação
		Assertions.assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
		Assertions.assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
	}
	
	@Test
	public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
		//cenário
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarlancamento();
		Mockito.doThrow(RegraNegocioException.class).when(lancamentoServiceImpl).validar(lancamentoASalvar);
		
		//execução e verificação
		Assertions.catchThrowableOfType(() -> lancamentoServiceImpl.salvar(lancamentoASalvar), RegraNegocioException.class);
		
		Mockito.verify(lancamentoRepository, Mockito.never()).save(lancamentoASalvar);
	}
	
	@Test
	public void deveAtualizarUmLancamento() {
		//cenário
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarlancamento();
		lancamentoSalvo.setId(1L);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		
		Mockito.doNothing().when(lancamentoServiceImpl).validar(lancamentoSalvo);
		
		Mockito.when(lancamentoRepository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);
		
		//execução
		lancamentoServiceImpl.atualizar(lancamentoSalvo);
		
		//verificação
		Mockito.verify(lancamentoRepository, Mockito.times(1)).save(lancamentoSalvo);
	}
	
	@Test
	public void deveLancarErroAoTentarAtualizarUmLancamentoQueAindaNaoFoiSalvo() {
		//cenário
		Lancamento lancamento = LancamentoRepositoryTest.criarlancamento();
		
		//execução e verificação
		Assertions.catchThrowableOfType(() -> lancamentoServiceImpl.atualizar(lancamento), NullPointerException.class);
		
		Mockito.verify(lancamentoRepository, Mockito.never()).save(lancamento);
		
	}
	
	@Test
	public void deveDeletarUmLancamento() {
		//cenário
		Lancamento lancamento = LancamentoRepositoryTest.criarlancamento();
		lancamento.setId(1L);
		
		//execução
		lancamentoServiceImpl.deletar(lancamento);
		
		//verificação
		Mockito.verify(lancamentoRepository).delete(lancamento);
	}
	
	@Test
	public void deveLancarErroAoTentarDeletarUmLancamentoQueAindaNaoFoiSalvo() {
		//cenário
		Lancamento lancamento = LancamentoRepositoryTest.criarlancamento();
		
		//execução
		Assertions.catchThrowableOfType(() -> lancamentoServiceImpl.deletar(lancamento), NullPointerException.class);
		
		//verificação
		Mockito.verify(lancamentoRepository, Mockito.never()).delete(lancamento);
		
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void deveFiltrarLancamentos() {
		//cenário
		Lancamento lancamento = LancamentoRepositoryTest.criarlancamento();
		lancamento.setId(1L);
		
		List<Lancamento> lista = Arrays.asList(lancamento);
		Mockito.when(lancamentoRepository.findAll(Mockito.any(Example.class))).thenReturn(lista);
		
		//execução
		List<Lancamento> resultado = lancamentoServiceImpl.buscar(lancamento);
		
		//verificação
		Assertions.assertThat(resultado).isNotEmpty().hasSize(1).contains(lancamento);
	}
	
	@Test
	public void deveAtualizarOStatusDeUmLancamento() {
		//cenário
		Lancamento lancamento = LancamentoRepositoryTest.criarlancamento();
		lancamento.setId(1L);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		
		StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
		Mockito.doReturn(lancamento).when(lancamentoServiceImpl).atualizar(lancamento);
		
		//execução
		lancamentoServiceImpl.atualizarStatus(lancamento, novoStatus);
		
		//verificação
		Assertions.assertThat(lancamento.getStatus()).isEqualTo(novoStatus);
		Mockito.verify(lancamentoServiceImpl).atualizar(lancamento);
	}
	
}
