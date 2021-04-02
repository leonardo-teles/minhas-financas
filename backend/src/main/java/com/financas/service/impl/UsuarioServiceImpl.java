package com.financas.service.impl;

import org.springframework.stereotype.Service;

import com.financas.exception.RegraNegocioException;
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
		boolean existe = usuarioRepository.existsByEmail(email);
		
		if (existe) {
			throw new RegraNegocioException("Já existe um usuário cadastrado com este e-mail");
		}
	}

}
