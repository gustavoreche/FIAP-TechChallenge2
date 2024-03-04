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
    private BigDecimal valorPago;


    public Estacionamento inicia(String placa,
                                 LocalDateTime horarioDeEntrada,
                                 LocalDateTime horarioDeSaida,
                                 BigDecimal valorPago) {
        var estacionamento = new Estacionamento();
        estacionamento.setPlaca(placa);
        estacionamento.setEntrada(horarioDeEntrada);
        estacionamento.setSaida(horarioDeSaida);
        estacionamento.setValorPago(valorPago);
        return estacionamento;
    }
}
