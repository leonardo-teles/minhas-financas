package com.financas.service.impl;

import org.springframework.stereotype.Service;

import com.financas.model.Usuario;
import com.financas.repository.UsuarioRepository;
import com.financas.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	private UsuarioRepository usuarioRepository;
	
	public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
		super();
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		return null;
	}

	@Override
	public Usuario salvarUsuario(Usuario usuario) {
		return null;
	}

	@Override
	public void validarEmail(String email) {
		
	}

}
