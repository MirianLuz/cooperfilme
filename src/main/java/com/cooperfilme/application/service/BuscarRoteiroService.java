package com.cooperfilme.application.service;

import com.cooperfilme.adapter.port.in.BuscarRoteiroUseCase;
import com.cooperfilme.adapter.port.out.RoteiroRepositoryPort;
import com.cooperfilme.domain.model.Roteiro;

import java.util.List;
import java.util.UUID;

public class BuscarRoteiroService implements BuscarRoteiroUseCase {

    private final RoteiroRepositoryPort roteiroRepository;

    public BuscarRoteiroService(RoteiroRepositoryPort roteiroRepository) {
        this.roteiroRepository = roteiroRepository;
    }

    @Override
    public Roteiro buscarPorId(UUID id) {
        return roteiroRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Roteiro não encontrado"));
    }

    @Override
    public List<Roteiro> listarTodos() {
        return roteiroRepository.buscarTodos();
    }
}