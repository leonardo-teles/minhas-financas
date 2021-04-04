package com.financas.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.financas.exception.RegraNegocioException;
import com.financas.model.Usuario;
import com.financas.repository.UsuarioRepository;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	@Autowired
	UsuarioService usuarioService;

	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Test(expected = Test.None.class)
	public void deveValidarEmail() {
		//cenário
		usuarioRepository.deleteAll();
		
		//ação
		usuarioService.validarEmail("email@email.com");
	}
	
	@Test(expected = RegraNegocioException.class)
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		//cenário
		Usuario usuario = Usuario.builder().nome("usuario").email("email@email.com").build();
		usuarioRepository.save(usuario);
		
		//ação
		usuarioService.validarEmail("email@email.com");
	}
}
