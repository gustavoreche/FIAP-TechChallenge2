package com.fiap.techchallenge2.service;

import com.fiap.techchallenge2.model.dto.ComprovanteEntradaDTO;
import com.fiap.techchallenge2.model.dto.EstacionamentoDTO;

import java.time.LocalDateTime;

public interface EstacionamentoService {

    ComprovanteEntradaDTO inicia(final EstacionamentoDTO iniciaDTO);
    String atualiza(final EstacionamentoDTO atualizaDTO);
    String finaliza(final String placa,
                    final LocalDateTime horaFim);

}
