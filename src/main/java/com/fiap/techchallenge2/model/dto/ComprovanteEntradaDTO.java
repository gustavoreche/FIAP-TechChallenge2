package com.fiap.techchallenge2.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ComprovanteEntradaDTO(
		String placa,
		LocalDateTime horarioDeEntrada,
		LocalDateTime horarioDeSaida,
		BigDecimal valorPago
) {}
