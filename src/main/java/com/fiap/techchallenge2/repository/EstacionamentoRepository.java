package com.fiap.techchallenge2.repository;

import com.fiap.techchallenge2.model.Estacionamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EstacionamentoRepository extends JpaRepository<Estacionamento, Long>, JpaSpecificationExecutor<Estacionamento> {

    Estacionamento findTop1ByPlacaOrderByEntradaDesc(String placa);

}
