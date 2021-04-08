package com.financas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

	private String nome;
	
	private String email;
	
	private String senha;
}