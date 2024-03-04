package com.fiap.techchallenge2.service.impl;

import com.fiap.techchallenge2.model.dto.ComprovanteEntradaDTO;
import com.fiap.techchallenge2.model.dto.Estacionamento;
import com.fiap.techchallenge2.model.dto.EstacionamentoDTO;
import com.fiap.techchallenge2.repository.EstacionamentoRepository;
import com.fiap.techchallenge2.service.EstacionamentoService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class EstacionamentoServiceImpl implements EstacionamentoService {

    private final EstacionamentoRepository repository;

    public EstacionamentoServiceImpl(final EstacionamentoRepository repository) {
        this.repository = repository;
    }


    @Override
    //TODO: VER CONCORRENCIA E PAGAMENTO POR MEIA HORA ESCOLHIDA
    public ComprovanteEntradaDTO inicia(final EstacionamentoDTO iniciaDTO) {
        if(Objects.nonNull(this.repository.findByPlacaAndSaidaIsNull(iniciaDTO.placa()))) {
            throw new RuntimeException("Este veículo ainda nao foi finalizado");

        }
        LocalDateTime horarioDeEntrada = LocalDateTime.now();
        LocalDateTime horarioPrevistoDeSaida = horarioDeEntrada.plusMinutes(iniciaDTO.tempo().getMinutos());
        this.repository.save(new Estacionamento().inicia(
                iniciaDTO.placa(),
                horarioDeEntrada,
                iniciaDTO.tempo()
                )
        );
        return new ComprovanteEntradaDTO(iniciaDTO.placa(), horarioDeEntrada, horarioPrevistoDeSaida);
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
