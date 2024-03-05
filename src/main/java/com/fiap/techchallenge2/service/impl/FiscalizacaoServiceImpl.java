package com.fiap.techchallenge2.service.impl;

import com.fiap.techchallenge2.model.Estacionamento;
import com.fiap.techchallenge2.model.FiscalizacaoAplicaEnum;
import com.fiap.techchallenge2.model.dto.RelatorioFiscalizacaoDTO;
import com.fiap.techchallenge2.repository.EstacionamentoRepository;
import com.fiap.techchallenge2.repository.EstacionamentoSpecification;
import com.fiap.techchallenge2.service.FiscalizacaoService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
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
        var carro = this.repository.findTop1ByPlacaOrderByIdDesc(placa);
        if(this.existeCarroNaBaseDeDados(carro)) {
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
        else if(this.fiscalizacaoNoMesmoDia(carro, diaDaFiscalizacao)) {
            if(carro.isMultado()) {
                return FiscalizacaoAplicaEnum.JA_MULTADO;
            }
            else if(horaFiscalizacao.isAfter(carro.getSaida())) {
                carro.setMultado(true);
                carro.setHoraFiscalizacao(horaFiscalizacao);
                this.repository.save(carro);
                return FiscalizacaoAplicaEnum.MULTADO;
            }
        }
        else if (this.fiscalizacaoComDadosDoDiaAnterior(carro, diaDaFiscalizacao)) {
            if(Objects.isNull(carro.getSaida()) ||
                    horaFiscalizacao.isAfter(carro.getSaida())) {
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
                return FiscalizacaoAplicaEnum.MULTADO;
            }
        }

        carro.setHoraFiscalizacao(horaFiscalizacao);
        this.repository.save(carro);
        return FiscalizacaoAplicaEnum.NAO_MULTADO;
    }

    @Override
    public List<RelatorioFiscalizacaoDTO> solicita(final String placa,
                                                   final LocalDateTime diaEHoraInicio,
                                                   final LocalDateTime diaEHoraFim) {
        var periodoEmDias = diaEHoraInicio.until(diaEHoraFim, ChronoUnit.DAYS);
        if(periodoEmDias > 30) {
            throw new RuntimeException("O período de pesquisa está maior que 1 mês");
        }
        else if(periodoEmDias < 0 || diaEHoraInicio.until(diaEHoraFim, ChronoUnit.SECONDS) < 0) {
            throw new RuntimeException("Data inicio maior que data fim");
        }
        var resultadosDaBusca = this.repository.findAll(
                Specification
                        .where(EstacionamentoSpecification.placa(Objects.isNull(placa) ? "" : placa))
                        .and(EstacionamentoSpecification.diaEHoraInicio(diaEHoraInicio))
                        .and(EstacionamentoSpecification.diaEHoraFim(diaEHoraFim)
                        )
        );
        return resultadosDaBusca
                .stream()
                .map(registro -> new RelatorioFiscalizacaoDTO(
                        registro.getPlaca(),
                        registro.getEntrada(),
                        registro.getSaida(),
                        registro.getValorPago(),
                        registro.isMultado(),
                        registro.getHoraFiscalizacao()
                    )
                )
                .toList();
    }

    private boolean existeCarroNaBaseDeDados(Estacionamento carro) {
        return Objects.isNull(carro);
    }

    private boolean fiscalizacaoNoMesmoDia(Estacionamento carro,
                                           LocalDate diaDaFiscalizacao) {
        return (Objects.nonNull(carro.getHoraFiscalizacao())
                && diaDaFiscalizacao.equals(carro.getHoraFiscalizacao().toLocalDate()))
                ||
                (Objects.nonNull(carro.getEntrada())
                        && diaDaFiscalizacao.equals(carro.getEntrada().toLocalDate()));
    }

    private boolean fiscalizacaoComDadosDoDiaAnterior(Estacionamento carro,
                                                      LocalDate diaDaFiscalizacao) {
        return (Objects.nonNull(carro.getHoraFiscalizacao())
                && diaDaFiscalizacao.isAfter(carro.getHoraFiscalizacao().toLocalDate()))
                ||
                (Objects.nonNull(carro.getEntrada())
                        && diaDaFiscalizacao.isAfter(carro.getEntrada().toLocalDate()));
    }

}
