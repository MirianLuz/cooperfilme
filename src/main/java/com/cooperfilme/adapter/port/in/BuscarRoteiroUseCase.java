package com.cooperfilme.adapter.port.in;

import com.cooperfilme.domain.model.Roteiro;

import java.util.List;
import java.util.UUID;

public interface BuscarRoteiroUseCase {
    Roteiro buscarPorId(UUID id);
    List<Roteiro> listarTodos();
}