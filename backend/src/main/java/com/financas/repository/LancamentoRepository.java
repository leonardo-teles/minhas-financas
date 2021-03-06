package com.financas.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.financas.enums.StatusLancamento;
import com.financas.enums.TipoLancamento;
import com.financas.model.Lancamento;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

	@Query(value = "SELECT SUM(l.valor) FROM Lancamento l JOIN l.usuario u WHERE u.id = :idUsuario AND l.tipo = :tipo AND l.status = :status GROUP BY u")
	BigDecimal obterSaldoPorTipoLancamentoEUsuarioEStatus(@Param("idUsuario") Long idUsuario, @Param("tipo") TipoLancamento tipo, @Param("status") StatusLancamento status);
}
