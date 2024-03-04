package com.fiap.techchallenge2.repository;

import com.fiap.techchallenge2.model.dto.Estacionamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstacionamentoRepository extends JpaRepository<Estacionamento, Long> {

    Estacionamento findByPlaca(String placa);

}
