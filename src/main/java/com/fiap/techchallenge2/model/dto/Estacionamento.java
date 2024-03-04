package com.fiap.techchallenge2.model.dto;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_estacionamento")
@Data
public class Estacionamento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String placa;
    private LocalDateTime entrada;
    private LocalDateTime saida;
    private boolean atualizouHorario;
    private int minutosAtualizados;
    private BigDecimal valorPagar;


    public Estacionamento inicia(String placa,
                                 LocalDateTime horarioDeEntrada,
                                 LocalDateTime horarioDeSaida) {
        var estacionamento = new Estacionamento();
        estacionamento.setPlaca(placa);
        estacionamento.setEntrada(horarioDeEntrada);
        estacionamento.setSaida(horarioDeSaida);
        estacionamento.setAtualizouHorario(false);
        estacionamento.setMinutosAtualizados(0);
        estacionamento.setValorPagar(null);
        return estacionamento;
    }
}
