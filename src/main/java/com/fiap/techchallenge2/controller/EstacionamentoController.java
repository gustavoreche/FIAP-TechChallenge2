package com.fiap.techchallenge2.controller;

import com.fiap.techchallenge2.model.dto.EstacionamentoIniciaDTO;
import com.fiap.techchallenge2.service.EstacionamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.fiap.techchallenge2.controller.EstacionamentoController.URL_ESTACIONAMENTO;

@RestController
@RequestMapping(URL_ESTACIONAMENTO)
public class EstacionamentoController {

	public static final String URL_ESTACIONAMENTO = "/estaciomento";
	public static final String URL_INICIA = URL_ESTACIONAMENTO.concat("/inicia");
	public static final String URL_FINALIZA = URL_ESTACIONAMENTO.concat("/finaliza");

	private final EstacionamentoService service;

	public EstacionamentoController(final EstacionamentoService service) {
		this.service = service;
	}
	
	@PostMapping("/inicia")
	public ResponseEntity<Void> inicia(@RequestBody @Valid final EstacionamentoIniciaDTO iniciaDTO) {
		this.service.inicia(iniciaDTO);
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.build();
	}

//	@PutMapping("/finaliza")
//	public ResponseEntity<String> finaliza(@PathVariable("atendimentoId") Long atendimentoId,
//										   @RequestBody @Valid ValorDaPropostaDTO valorDaPropostaDTO) {
//		return ResponseEntity
//				.status(HttpStatus.OK)
//				.body(this.service.finaliza());
//	}

}
