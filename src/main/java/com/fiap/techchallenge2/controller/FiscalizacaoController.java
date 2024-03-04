package com.fiap.techchallenge2.controller;

import com.fiap.techchallenge2.model.FiscalizacaoAplicaEnum;
import com.fiap.techchallenge2.service.FiscalizacaoService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static com.fiap.techchallenge2.controller.EstacionamentoController.REGEX_PLACA;

@RestController
@RequestMapping(FiscalizacaoController.URL_FISCALIZACAO)
public class FiscalizacaoController {

	public static final String URL_FISCALIZACAO = "/fiscalizacao";
	public static final String URL_FISCALIZACAO_APLICA = URL_FISCALIZACAO.concat("/aplica/{placa}");

	private final FiscalizacaoService service;

	public FiscalizacaoController(final FiscalizacaoService service) {
		this.service = service;
	}

	@PutMapping("/aplica/{placa}")
	public ResponseEntity<FiscalizacaoAplicaEnum> aplica(@PathVariable("placa") @Pattern(regexp = REGEX_PLACA, message = "A placa inserida nao esta no padrao antigo, nem no novo padrao") final String placa) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(this.service.aplica(placa));
	}

	@GetMapping
	public ResponseEntity<Void> solicita(@RequestParam(required = false) @Pattern(regexp = REGEX_PLACA, message = "A placa inserida nao esta no padrao antigo, nem no novo padrao") final String placa,
										 @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDateTime).now().minusDays(15)}") final LocalDateTime diaEHoraInicio,
										 @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDateTime).now()}") final LocalDateTime diaEHoraFim) {
		this.service.solicita(placa, diaEHoraInicio, diaEHoraFim);
		return ResponseEntity
				.status(HttpStatus.OK)
				.build();
	}

}
