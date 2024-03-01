package com.fiap.techchallenge2.service;

import java.time.LocalDateTime;

public interface FiscalizacaoService {

    String solicita(final String placa,
                    final LocalDateTime diaEHoraInicio,
                    final LocalDateTime diaEHoraFim);
}
