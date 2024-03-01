package com.fiap.techchallenge2.controller;

import com.fiap.techchallenge2.model.dto.EstacionamentoDTO;
import com.fiap.techchallenge2.service.EstacionamentoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static com.fiap.techchallenge2.controller.EstacionamentoController.URL_ESTACIONAMENTO;

@RestController
@RequestMapping(URL_ESTACIONAMENTO)
public class EstacionamentoController {

	public static final String REGEX_PLACA = "^[a-zA-Z]{3}\\d{4}$|^[a-zA-Z]{3}\\d[a-zA-Z]\\d{2}$";

	public static final String URL_ESTACIONAMENTO = "/estaciomento";
	public static final String URL_INICIA = URL_ESTACIONAMENTO.concat("/inicia");
	public static final String URL_ATUALIZA = URL_ESTACIONAMENTO.concat("/atualiza/{placa}");
	public static final String URL_FINALIZA = URL_ESTACIONAMENTO.concat("/finaliza/{placa}");

	private final EstacionamentoService service;

	public EstacionamentoController(final EstacionamentoService service) {
		this.service = service;
	}
	
	@PostMapping("/inicia")
	public ResponseEntity<Void> inicia(@RequestBody @Valid final EstacionamentoDTO iniciaDTO) {
		this.service.inicia(iniciaDTO);
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.build();
	}

	@PutMapping("/atualiza/{placa}")
	public ResponseEntity<String> atualiza(@RequestBody @Valid final EstacionamentoDTO atualizaDTO) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(this.service.atualiza(atualizaDTO));
	}

	@PutMapping("/finaliza/{placa}")
	public ResponseEntity<String> finaliza(@PathVariable("placa")
											   @Pattern(regexp = REGEX_PLACA, message = "A placa inserida nao esta no padrao antigo, nem no novo padrao") final String placa) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(this.service.finaliza(placa, LocalDateTime.now()));
	}

}
