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
    private int minutosEscolhidosParaPermanencia;
    private LocalDateTime saida;
    private boolean atualizouHorario;
    private int minutosEscolhidosAtualizados;
    private BigDecimal valorPagar;


    public Estacionamento inicia(String placa,
                                 LocalDateTime horarioDeEntrada,
                                 TempoPermanenciaEnum tempoEscolhidoParaPermanencia) {
        var estacionamento = new Estacionamento();
        estacionamento.setPlaca(placa);
        estacionamento.setEntrada(horarioDeEntrada);
        estacionamento.setMinutosEscolhidosParaPermanencia(tempoEscolhidoParaPermanencia.getMinutos());
        estacionamento.setSaida(null);
        estacionamento.setAtualizouHorario(false);
        estacionamento.setMinutosEscolhidosAtualizados(0);
        estacionamento.setValorPagar(null);
        return estacionamento;
    }
}
