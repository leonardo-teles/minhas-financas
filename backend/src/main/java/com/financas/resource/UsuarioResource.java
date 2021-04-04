package com.financas.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.financas.dto.UsuarioDTO;
import com.financas.exception.RegraNegocioException;
import com.financas.model.Usuario;
import com.financas.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {

	private UsuarioService usuarioService;
	
	public UsuarioResource(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	@PostMapping
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ResponseEntity salvar(@RequestBody UsuarioDTO usuarioDTO) {
		Usuario usuario = Usuario.builder().nome(usuarioDTO.getNome()).email(usuarioDTO.getEmail()).senha(usuarioDTO.getSenha()).build();
		
		try {
			Usuario usuarioSalvo = usuarioService.salvarUsuario(usuario);
			
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
