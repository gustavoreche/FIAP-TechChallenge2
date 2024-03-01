package com.fiap.techchallenge2.service.impl;

import com.fiap.techchallenge2.service.FiscalizacaoService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FiscalizacaoServiceImpl implements FiscalizacaoService {

    @Override
    public String solicita(final String placa,
                           final LocalDateTime diaEHoraInicio,
                           final LocalDateTime diaEHoraFim) {
        System.out.println(placa + " " + diaEHoraInicio + " " + " " + diaEHoraFim);
        return "HELLO WORLD";
    }
}
