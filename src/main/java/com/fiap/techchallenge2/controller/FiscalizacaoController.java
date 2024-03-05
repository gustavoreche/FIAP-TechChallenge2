package com.fiap.techchallenge2.controller;

import com.fiap.techchallenge2.model.FiscalizacaoAplicaEnum;
import com.fiap.techchallenge2.model.dto.RelatorioFiscalizacaoDTO;
import com.fiap.techchallenge2.service.FiscalizacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.fiap.techchallenge2.controller.EstacionamentoController.REGEX_PLACA;

@Tag(
		name = "Fiscalização",
		description = "Serviços referente a fiscalização na área monitorada pelo parquímetro"
)
@RestController
@RequestMapping(FiscalizacaoController.URL_FISCALIZACAO)
public class FiscalizacaoController {

	public static final String URL_FISCALIZACAO = "/fiscalizacao";
	public static final String URL_FISCALIZACAO_APLICA = URL_FISCALIZACAO.concat("/aplica/{placa}");

	private final FiscalizacaoService service;

	public FiscalizacaoController(final FiscalizacaoService service) {
		this.service = service;
	}

	@Operation(
			summary = "Serviço que verifica se a multa será aplicada ou não."
	)
	@PutMapping("/aplica/{placa}")
	public ResponseEntity<FiscalizacaoAplicaEnum> aplica(@PathVariable("placa") @Pattern(regexp = REGEX_PLACA, message = "A placa inserida nao esta no padrao antigo, nem no novo padrao") final String placa) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(this.service.aplica(placa));
	}

	@Operation(
			summary = """
					Serviço que gera informações/insights. Veja os detalhes:
					""",
			description = """
					Busque as informações pelos seguintes parâmetros:
					
						- placa do carro (se não passar uma placa, pesquisa por todas as placas)
						- dia e hora do início da pesquisa (se não passar uma data, pesquisa por 15 dias atrás)
						- dia e hora do fim da pesquisa (se não passar uma data, pesquisa pela data atual)
						
					Observação: O tempo máximo de pesquisa é no período de 1 mês.
					"""
	)
	@GetMapping
	public ResponseEntity<List<RelatorioFiscalizacaoDTO>> solicita(@RequestParam(required = false) @Pattern(regexp = REGEX_PLACA, message = "A placa inserida nao esta no padrao antigo, nem no novo padrao") final String placa,
																   @Parameter(example = "2024-01-25T21:48:54") @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDateTime).now().minusDays(15)}") final LocalDateTime diaEHoraInicio,
																   @Parameter(example = "2024-02-28T21:48:54") @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDateTime).now()}") final LocalDateTime diaEHoraFim) {
		this.service.solicita(placa, diaEHoraInicio, diaEHoraFim);
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(this.service.solicita(placa, diaEHoraInicio, diaEHoraFim));
	}

}
