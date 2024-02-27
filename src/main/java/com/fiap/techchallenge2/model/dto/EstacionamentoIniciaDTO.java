package com.fiap.techchallenge2.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EstacionamentoIniciaDTO(
		@NotBlank(message = "A placa nao pode ser vazia")
		@Pattern(regexp = "^[a-zA-Z]{3}\\d{4}$|^[a-zA-Z]{3}\\d[a-zA-Z]\\d{2}$", message = "A placa inserida nao esta no padrao antigo, nem no novo padrao")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String placa
) {}
