package com.fiap.techchallenge2.service;

import com.fiap.techchallenge2.model.dto.EstacionamentoIniciaDTO;

public interface EstacionamentoService {

    void inicia(EstacionamentoIniciaDTO iniciaDTO);
    String atualiza();
    String finaliza();

}
