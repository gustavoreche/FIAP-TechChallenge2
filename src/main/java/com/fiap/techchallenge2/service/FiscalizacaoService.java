package com.fiap.techchallenge2.service;

import com.fiap.techchallenge2.model.FiscalizacaoAplicaEnum;
import com.fiap.techchallenge2.model.dto.RelatorioFiscalizacaoDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface FiscalizacaoService {

    FiscalizacaoAplicaEnum aplica(final String placa);

    List<RelatorioFiscalizacaoDTO> solicita(final String placa,
                                            final LocalDateTime diaEHoraInicio,
                                            final LocalDateTime diaEHoraFim);
}
