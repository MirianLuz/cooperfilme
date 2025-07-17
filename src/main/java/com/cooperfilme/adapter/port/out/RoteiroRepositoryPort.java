package com.cooperfilme.adapter.port.out;

import com.cooperfilme.domain.model.Roteiro;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoteiroRepositoryPort {
    Roteiro salvar(Roteiro roteiro);
    Optional<Roteiro> buscarPorId(UUID id);
    List<Roteiro> buscarTodos();
}