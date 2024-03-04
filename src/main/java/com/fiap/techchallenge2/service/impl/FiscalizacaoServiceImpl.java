package com.fiap.techchallenge2.service.impl;

import com.fiap.techchallenge2.model.Estacionamento;
import com.fiap.techchallenge2.model.FiscalizacaoAplicaEnum;
import com.fiap.techchallenge2.repository.EstacionamentoRepository;
import com.fiap.techchallenge2.service.FiscalizacaoService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class FiscalizacaoServiceImpl implements FiscalizacaoService {

    private final EstacionamentoRepository repository;

    public FiscalizacaoServiceImpl(final EstacionamentoRepository repository) {
        this.repository = repository;
    }

    @Override
    public FiscalizacaoAplicaEnum aplica(final String placa) {
        var horaFiscalizacao = LocalDateTime.now();
        var diaDaFiscalizacao = horaFiscalizacao.toLocalDate();
        var carro = this.repository.findTop1ByPlacaOrderByEntradaDesc(placa);
        if(Objects.isNull(carro)) {
            var estacionamento = Estacionamento
                    .builder()
                    .placa(placa)
                    .entrada(null)
                    .saida(null)
                    .valorPago(null)
                    .multado(true)
                    .horaFiscalizacao(horaFiscalizacao)
                    .build();
            this.repository.save(estacionamento);
            return FiscalizacaoAplicaEnum.MULTADO;
        }
        else if(Objects.nonNull(carro.getHoraFiscalizacao())
                && diaDaFiscalizacao.equals(carro.getHoraFiscalizacao().toLocalDate())) {
            return FiscalizacaoAplicaEnum.JA_MULTADO;
        }
        else if(horaFiscalizacao.isAfter(carro.getSaida())) {
            if(Objects.nonNull(carro.getHoraFiscalizacao())) {
                var estacionamento = Estacionamento
                        .builder()
                        .placa(carro.getPlaca())
                        .entrada(null)
                        .saida(null)
                        .valorPago(null)
                        .multado(true)
                        .horaFiscalizacao(horaFiscalizacao)
                        .build();
                this.repository.save(estacionamento);
            } else {
                carro.setMultado(true);
                carro.setHoraFiscalizacao(horaFiscalizacao);
                this.repository.save(carro);
            }
            return FiscalizacaoAplicaEnum.MULTADO;
        }

        carro.setHoraFiscalizacao(horaFiscalizacao);
        this.repository.save(carro);
        return FiscalizacaoAplicaEnum.NAO_MULTADO;
    }

    @Override
    public String solicita(final String placa,
                           final LocalDateTime diaEHoraInicio,
                           final LocalDateTime diaEHoraFim) {
        System.out.println(placa + " " + diaEHoraInicio + " " + " " + diaEHoraFim);
        return "HELLO WORLD";
    }
}
