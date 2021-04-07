package com.financas.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.financas.enums.StatusLancamento;
import com.financas.enums.TipoLancamento;
import com.financas.model.Lancamento;

@DataJpaTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class LancamentoRepositoryTest {
	
	@Autowired
	LancamentoRepository lancamentoRepository;
	
	@Autowired
	TestEntityManager testEntityManager;
	
	@Test
	public void deveSalvarUmLancamento() {
		Lancamento lancamento = criarlancamento();
		
		lancamento = lancamentoRepository.save(lancamento);
		
		assertThat(lancamento.getId()).isNotNull();
	}
	
	@Test
	public void deveDeletarUmLancamento() {
		Lancamento lancamento = criarEPersistirLancamento();
		
		lancamento = testEntityManager.find(Lancamento.class, lancamento.getId());
		
		lancamentoRepository.delete(lancamento);
		
		Lancamento lancamentoInexistente = testEntityManager.find(Lancamento.class, lancamento.getId());
		
		assertThat(lancamentoInexistente).isNull();
		
	}
	
	@Test
	public void deveAtualizarUmLancamento() {
		Lancamento lancamento = criarEPersistirLancamento();
		lancamento.setAno(2020);
		lancamento.setDescricao("Atualizar lançamento");
		lancamento.setStatus(StatusLancamento.CANCELADO);

		lancamentoRepository.save(lancamento);
		
		Lancamento lancamentoAtualizado = testEntityManager.find(Lancamento.class, lancamento.getId());
		
		assertThat(lancamentoAtualizado.getAno()).isEqualTo(2020);
		assertThat(lancamentoAtualizado.getDescricao()).isEqualTo("Atualizar lançamento");
		assertThat(lancamentoAtualizado.getStatus()).isEqualTo(StatusLancamento.CANCELADO);
	}
	
	@Test
	public void deveBuscarUmLancamentoPorId() {
		Lancamento lancamento = criarEPersistirLancamento();
		
		Optional<Lancamento> lancamentoEncontrado = lancamentoRepository.findById(lancamento.getId());
		
		assertThat(lancamentoEncontrado.isPresent()).isTrue();
	}
	
	public static Lancamento criarlancamento() {
		return Lancamento.builder()
				   .descricao("lancamento qualquer")
				   .mes(4)
				   .ano(2021)
				   .valor(BigDecimal.valueOf(10))
				   .dataCadastro(LocalDate.now())
				   .tipo(TipoLancamento.DESPESA)
				   .status(StatusLancamento.PENDENTE)
				   .build();
	}
	
	private Lancamento criarEPersistirLancamento() {
		Lancamento lancamento = criarlancamento();
		testEntityManager.persist(lancamento);
		
		return lancamento;
	}
}
