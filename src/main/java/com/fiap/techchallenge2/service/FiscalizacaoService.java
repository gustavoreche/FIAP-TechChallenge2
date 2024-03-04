package com.fiap.techchallenge2.service;

import com.fiap.techchallenge2.model.FiscalizacaoAplicaEnum;

import java.time.LocalDateTime;

public interface FiscalizacaoService {

    FiscalizacaoAplicaEnum aplica(final String placa);

    String solicita(final String placa,
                    final LocalDateTime diaEHoraInicio,
                    final LocalDateTime diaEHoraFim);
}
