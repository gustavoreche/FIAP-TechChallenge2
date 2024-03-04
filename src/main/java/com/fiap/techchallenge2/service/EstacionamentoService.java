package com.fiap.techchallenge2.service;

import com.fiap.techchallenge2.model.dto.ComprovanteEntradaDTO;
import com.fiap.techchallenge2.model.dto.EstacionamentoDTO;

public interface EstacionamentoService {

    ComprovanteEntradaDTO inicia(final EstacionamentoDTO iniciaDTO);

}
