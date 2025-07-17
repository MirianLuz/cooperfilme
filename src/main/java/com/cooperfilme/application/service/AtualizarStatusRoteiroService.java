package com.cooperfilme.application.service;

import com.cooperfilme.adapter.port.in.AtualizarStatusRoteiroUseCase;
import com.cooperfilme.adapter.port.out.RoteiroRepositoryPort;
import com.cooperfilme.adapter.port.out.UsuarioRepositoryPort;
import com.cooperfilme.domain.enums.Cargo;
import com.cooperfilme.domain.enums.StatusRoteiro;
import com.cooperfilme.domain.model.Roteiro;
import com.cooperfilme.domain.model.Usuario;
import com.cooperfilme.exception.BusinessException;

import java.util.UUID;

public class AtualizarStatusRoteiroService implements AtualizarStatusRoteiroUseCase {

    private final RoteiroRepositoryPort roteiroRepository;
    private final UsuarioRepositoryPort usuarioRepository;

    public AtualizarStatusRoteiroService(RoteiroRepositoryPort roteiroRepository,
                                         UsuarioRepositoryPort usuarioRepository) {
        this.roteiroRepository = roteiroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void atualizarStatus(UUID roteiroId, StatusRoteiro novoStatus, UUID usuarioId) {
        Roteiro roteiro = roteiroRepository.buscarPorId(roteiroId)
                .orElseThrow(() -> new BusinessException("Roteiro não encontrado"));
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        validarTransicao(roteiro.getStatus(), novoStatus, usuario.getCargo());

        roteiro.adicionarHistorico(novoStatus, usuarioId);
        roteiroRepository.salvar(roteiro);
    }

    private void validarTransicao(StatusRoteiro atual, StatusRoteiro novo, Cargo cargo) {
        if (atual == StatusRoteiro.AGUARDANDO_ANALISE && novo == StatusRoteiro.EM_REVISAO && cargo == Cargo.ANALISTA) return;
        if (atual == StatusRoteiro.EM_REVISAO && novo == StatusRoteiro.EM_APROVACAO && cargo == Cargo.REVISOR) return;
        if (atual == StatusRoteiro.EM_APROVACAO && (novo == StatusRoteiro.APROVADO || novo == StatusRoteiro.REJEITADO) && cargo == Cargo.APROVADOR) return;

        throw new BusinessException("Transição de status inválida para o cargo do usuário.");
    }
}