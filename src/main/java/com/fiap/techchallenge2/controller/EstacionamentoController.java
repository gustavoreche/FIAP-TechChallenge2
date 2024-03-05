package com.fiap.techchallenge2.controller;

import com.fiap.techchallenge2.model.dto.ComprovanteEntradaDTO;
import com.fiap.techchallenge2.model.dto.EstacionamentoDTO;
import com.fiap.techchallenge2.service.EstacionamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.fiap.techchallenge2.controller.EstacionamentoController.URL_ESTACIONAMENTO;

@Tag(
		name = "Estacionamento",
		description = "Serviço referente ao estacionamento na área monitorada pelo parquímetro"
)
@RestController
@RequestMapping(URL_ESTACIONAMENTO)
public class EstacionamentoController {

	public static final String REGEX_PLACA = "^[a-zA-Z]{3}\\d{4}$|^[a-zA-Z]{3}\\d[a-zA-Z]\\d{2}$";

	public static final String URL_ESTACIONAMENTO = "/estaciomento";
	public static final String URL_INICIA = URL_ESTACIONAMENTO.concat("/inicia");

	private final EstacionamentoService service;

	public EstacionamentoController(final EstacionamentoService service) {
		this.service = service;
	}

	@Operation(
			summary = "Serviço para escolher a quantidade de horas que seu carro irá ficar estacionado."
	)
	@PostMapping("/inicia")
	public ResponseEntity<ComprovanteEntradaDTO> inicia(@RequestBody @Valid final EstacionamentoDTO iniciaDTO) {
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(this.service.inicia(iniciaDTO));
	}

}
