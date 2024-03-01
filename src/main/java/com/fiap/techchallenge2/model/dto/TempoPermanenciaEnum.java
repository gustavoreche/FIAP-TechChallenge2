package com.fiap.techchallenge2.model.dto;

import lombok.Getter;

@Getter
public enum TempoPermanenciaEnum {

    MEIA_HORA(30),
    UMA_HORA(30),
    UMA_HORA_E_MEIA(30),
    DUAS_HORAS(30),
    DUAS_HORAS_E_MEIA(30),
    TRES_HORAS(30),
    TRES_HORAS_E_MEIA(30),
    QUATRO_HORAS(30),
    QUATRO_HORAS_E_MEIA(30),
    CINCO_HORAS(30)
    ;

    private final int minutos;

    TempoPermanenciaEnum(int minutos) {
        this.minutos = minutos;
    }

}
