package com.financas.repository;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.financas.model.Usuario;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Test
	public void deveVerificarAExistenciaDeUmEmail() {
		//cenário
		Usuario usuario = Usuario.builder().nome("usuario").email("usuario@email.com").build();
		usuarioRepository.save(usuario);
		
		//ação/execução
		boolean result = usuarioRepository.existsByEmail("usuario@email.com");
		
		//verificação
		Assertions.assertThat(result).isTrue();
		
	}
}
