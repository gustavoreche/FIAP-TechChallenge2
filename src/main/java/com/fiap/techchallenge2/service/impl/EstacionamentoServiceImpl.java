package com.fiap.techchallenge2.service.impl;

import com.fiap.techchallenge2.model.dto.EstacionamentoDTO;
import com.fiap.techchallenge2.service.EstacionamentoService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EstacionamentoServiceImpl implements EstacionamentoService {


    @Override
    public void inicia(final EstacionamentoDTO iniciaDTO) {
        //TODO: SALVAR NA BASE DE DADOS
        System.out.println("oi");
    }

    @Override
    public String atualiza(final EstacionamentoDTO atualizaDTO) {
        return "HELLO WORLD";
    }

    @Override
    public String finaliza(final String placa,
                           final LocalDateTime horaFim) {
        return "HELLO WORLD";
    }
}
