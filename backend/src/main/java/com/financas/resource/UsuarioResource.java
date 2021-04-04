package com.financas.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsuarioResource {

	@GetMapping("/")
	public String ola( ) {
		return "olá";
	}
}
