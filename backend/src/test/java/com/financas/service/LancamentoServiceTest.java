package com.financas.service;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
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
	
}
