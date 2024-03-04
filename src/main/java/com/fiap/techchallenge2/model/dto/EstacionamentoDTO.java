package com.fiap.techchallenge2.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fiap.techchallenge2.model.TempoPermanenciaEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import static com.fiap.techchallenge2.controller.EstacionamentoController.REGEX_PLACA;

public record EstacionamentoDTO(
		@NotBlank(message = "A placa nao pode ser vazia")
		@Pattern(regexp = REGEX_PLACA, message = "A placa inserida nao esta no padrao antigo, nem no novo padrao")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String placa,

		@NotNull(message = "O tempo nao pode ser vazio")
		@JsonInclude(JsonInclude.Include.NON_NULL)
        TempoPermanenciaEnum tempo
) {}
