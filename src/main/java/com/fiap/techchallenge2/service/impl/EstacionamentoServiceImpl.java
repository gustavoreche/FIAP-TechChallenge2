package com.fiap.techchallenge2.service.impl;

import com.fiap.techchallenge2.model.dto.ComprovanteEntradaDTO;
import com.fiap.techchallenge2.model.dto.Estacionamento;
import com.fiap.techchallenge2.model.dto.EstacionamentoDTO;
import com.fiap.techchallenge2.repository.EstacionamentoRepository;
import com.fiap.techchallenge2.service.EstacionamentoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class EstacionamentoServiceImpl implements EstacionamentoService {

    private final EstacionamentoRepository repository;

    @Value("${valor.meia_hora}")
    private BigDecimal valorMeiaHora;

    public EstacionamentoServiceImpl(final EstacionamentoRepository repository) {
        this.repository = repository;
    }


    @Override
    public ComprovanteEntradaDTO inicia(final EstacionamentoDTO iniciaDTO) {
        var horarioDeEntrada = LocalDateTime.now();
        var horarioDeSaida = horarioDeEntrada.plusMinutes(iniciaDTO.tempo().getMinutos());
        var valorPago = this.valorMeiaHora.multiply(new BigDecimal(iniciaDTO.tempo().getMinutos()));
        this.repository.save(new Estacionamento().inicia(
                iniciaDTO.placa(),
                horarioDeEntrada,
                horarioDeSaida,
                valorPago
                )
        );
        return new ComprovanteEntradaDTO(
                iniciaDTO.placa(),
                horarioDeEntrada,
                horarioDeSaida,
                valorPago
        );
    }

}
