package com.fiap.techchallenge2.service.impl;

import com.fiap.techchallenge2.model.dto.ComprovanteEntradaDTO;
import com.fiap.techchallenge2.model.dto.Estacionamento;
import com.fiap.techchallenge2.model.dto.EstacionamentoDTO;
import com.fiap.techchallenge2.repository.EstacionamentoRepository;
import com.fiap.techchallenge2.service.EstacionamentoService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class EstacionamentoServiceImpl implements EstacionamentoService {

    private final EstacionamentoRepository repository;

    public EstacionamentoServiceImpl(final EstacionamentoRepository repository) {
        this.repository = repository;
    }


    @Override
    public ComprovanteEntradaDTO inicia(final EstacionamentoDTO iniciaDTO) {
        LocalDateTime horarioDeEntrada = LocalDateTime.now();
        LocalDateTime horarioDeSaida = horarioDeEntrada.plusMinutes(iniciaDTO.tempo().getMinutos());
        this.repository.save(new Estacionamento().inicia(
                iniciaDTO.placa(),
                horarioDeEntrada,
                horarioDeSaida
                )
        );
        return new ComprovanteEntradaDTO(iniciaDTO.placa(), horarioDeEntrada, horarioDeSaida);
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
