package com.financas.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.financas.exception.ErroAutenticacaoException;
import com.financas.exception.RegraNegocioException;
import com.financas.model.Usuario;
import com.financas.repository.UsuarioRepository;
import com.financas.service.impl.UsuarioServiceImpl;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	@SpyBean
	UsuarioServiceImpl usuarioService;

	@MockBean
	UsuarioRepository usuarioRepository;
	
	@Test(expected = Test.None.class)
	public void deveSalvarUmUsuario() {
		//cenário
		Mockito.doNothing().when(usuarioService).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder().id(1L).nome("usuario").email("email@email.com").senha("senha").build();
		
		Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		//ação
		Usuario usuarioSalvo = usuarioService.salvarUsuario(new Usuario());
		
		//verificação
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1L);
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("usuario");
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
		
	}
	
	@Test(expected = RegraNegocioException.class)
	public void naoDeveSalvarUmUsuarioComEmailJaCadstrado() {
		//cenário
		String email = "email@email.com";
		Usuario usuario = Usuario.builder().email(email).build();
		Mockito.doThrow(RegraNegocioException.class).when(usuarioService).validarEmail(email);
		
		//ação
		usuarioService.salvarUsuario(usuario);
		
		//verificação
		Mockito.verify(usuarioRepository, Mockito.never()).save(usuario);
	}
	
	@Test(expected = Test.None.class)
	public void deveAutenticarUmUsuarioComSucesso() {
		//cenário
		String email = "email@email.com";
		String senha = "senha";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1L).build();
		Mockito.when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		//ação
		Usuario resultado = usuarioService.autenticar(email, senha);
		
		//verificação
		Assertions.assertThat(resultado).isNotNull();
	}
	
	@Test
	public void deveLancarErroQuandoNaoEncontraUsuarioCadastradoComEmailInformado() {
		//cenário
		Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		//ação
		Throwable exception = Assertions.catchThrowable(() ->  usuarioService.autenticar("email@email.com", "senha"));
		
		//verificação
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacaoException.class).hasMessage("Usuário não encontrado para o e-mail informado");
	}
	
	@Test
	public void deveLancarErroQuandoSenhaNaoBater() {
		//cenário 
		String senha = "senha";
		Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
		Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		
		//ação
		Throwable exception = Assertions.catchThrowable(() ->  usuarioService.autenticar("email@emailcom", "123"));
		
		//verificação
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacaoException.class).hasMessage("Senha inválida");
		
	}
	
	@Test(expected = Test.None.class)
	public void deveValidarEmail() {
		//cenário
		Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		//ação
		usuarioService.validarEmail("email@email.com");
	}
	
	@Test(expected = RegraNegocioException.class)
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		//cenário
		Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		//ação
		usuarioService.validarEmail("email@email.com");
	}
}
