package com.cooperfilme.adapter.port.in;

import com.cooperfilme.domain.enums.StatusRoteiro;

import java.util.UUID;

public interface AtualizarStatusRoteiroUseCase {
    void atualizarStatus(UUID roteiroId, StatusRoteiro novoStatus, UUID usuarioId);
}