package com.financas.resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financas.dto.UsuarioDTO;
import com.financas.exception.ErroAutenticacaoException;
import com.financas.exception.RegraNegocioException;
import com.financas.model.Usuario;
import com.financas.service.LancamentoService;
import com.financas.service.UsuarioService;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UsuarioResource.class)
public class UsuarioResourceTest {

	static final String API = "/api/usuarios";
	static final MediaType JSON = MediaType.APPLICATION_JSON;
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	UsuarioService usuarioService;
	
	@MockBean
	LancamentoService lancamentoService;
	
	@Test
	public void deveAutenticarUmUsuario() throws Exception {
		//cenário
		String email = "usuario@email.com";
		String senha = "123";
		
		UsuarioDTO usuarioDTO = UsuarioDTO.builder().email(email).senha(senha).build();
		Usuario usuario = Usuario.builder().id(1L).email(email).senha(senha).build();

		Mockito.when(usuarioService.autenticar(email, senha)).thenReturn(usuario);
		
		String json = new ObjectMapper().writeValueAsString(usuarioDTO);
		
		//execução e verificação
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
			.post(API.concat("/autenticar"))
			.accept(JSON)
			.contentType(JSON)
			.content(json);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
			.andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
			.andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()));
	}
	
	@Test
	public void deveRetornarBadRequestAoObterErroDeAutenticacao() throws Exception {
		//cenário
		String email = "usuario@email.com";
		String senha = "123";
		
		UsuarioDTO usuarioDTO = UsuarioDTO.builder().email(email).senha(senha).build();
		Mockito.when(usuarioService.autenticar(email, senha)).thenThrow(ErroAutenticacaoException.class);
		
		String json = new ObjectMapper().writeValueAsString(usuarioDTO);
		
		//execução e verificação
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
			.post(API.concat("/autenticar"))
			.accept(JSON)
			.contentType(JSON)
			.content(json);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	public void deveCriarUmNovoUsuario() throws Exception {
		//cenário
		String email = "usuario@email.com";
		String senha = "123";
		
		UsuarioDTO usuarioDTO = UsuarioDTO.builder().email("usuario@email.com").senha("123").build();
		Usuario usuario = Usuario.builder().id(1L).email(email).senha(senha).build();

		Mockito.when(usuarioService.salvarUsuario(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		String json = new ObjectMapper().writeValueAsString(usuarioDTO);
		
		//execução e verificação
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
			.post(API)
			.accept(JSON)
			.contentType(JSON)
			.content(json);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
			.andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
			.andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()));
	}
	
	@Test
	public void deveRetornarBadRequestAoTentarCriarUmUsuarioInvalido() throws Exception {
		//cenário
		UsuarioDTO usuarioDTO = UsuarioDTO.builder().email("usuario@email.com").senha("123").build();

		Mockito.when(usuarioService.salvarUsuario(Mockito.any(Usuario.class))).thenThrow(RegraNegocioException.class);
		
		String json = new ObjectMapper().writeValueAsString(usuarioDTO);
		
		//execução e verificação
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
			.post(API)
			.accept(JSON)
			.contentType(JSON)
			.content(json);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
}
