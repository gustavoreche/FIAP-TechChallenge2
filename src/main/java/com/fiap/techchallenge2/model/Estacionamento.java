package com.fiap.techchallenge2.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_estacionamento")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Estacionamento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String placa;
    private LocalDateTime entrada;
    private LocalDateTime saida;
    private BigDecimal valorPago;
    private boolean multado;
    private LocalDateTime horaFiscalizacao;

}
