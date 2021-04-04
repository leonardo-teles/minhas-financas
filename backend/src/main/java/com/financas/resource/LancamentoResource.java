package com.financas.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.financas.dto.LancamentoDTO;
import com.financas.service.LancamentoService;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoResource {

	private LancamentoService lancamentoService;
	
	public LancamentoResource(LancamentoService lancamentoService) {
		this.lancamentoService = lancamentoService;
	}
	
	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoDTO lancamentoDTO) {
		
	}
}
