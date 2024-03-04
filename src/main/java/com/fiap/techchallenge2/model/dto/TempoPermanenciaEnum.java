package com.fiap.techchallenge2.model.dto;

import lombok.Getter;

@Getter
public enum TempoPermanenciaEnum {

    MEIA_HORA(30),
    UMA_HORA(60),
    UMA_HORA_E_MEIA(90),
    DUAS_HORAS(120),
    DUAS_HORAS_E_MEIA(150),
    TRES_HORAS(180),
    TRES_HORAS_E_MEIA(210),
    QUATRO_HORAS(240),
    QUATRO_HORAS_E_MEIA(270),
    CINCO_HORAS(300)
    ;

    private final int minutos;

    TempoPermanenciaEnum(int minutos) {
        this.minutos = minutos;
    }

}
