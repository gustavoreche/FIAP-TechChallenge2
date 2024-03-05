package com.fiap.techchallenge2.repository;

import com.fiap.techchallenge2.model.Estacionamento;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class EstacionamentoSpecification {

    public static Specification<Estacionamento> placa(String placa) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("placa"), "%" + placa + "%");
    }

    public static Specification<Estacionamento> diaEHoraInicio(LocalDateTime diaEHoraInicio) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("entrada"), diaEHoraInicio);
    }

    public static Specification<Estacionamento> diaEHoraFim(LocalDateTime diaEHoraFim) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("saida"), diaEHoraFim);
    }

}
