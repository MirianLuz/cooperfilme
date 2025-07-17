package com.cooperfilme.application.service;

import com.cooperfilme.adapter.port.in.EnviarRoteiroUseCase;
import com.cooperfilme.adapter.port.out.RoteiroRepositoryPort;
import com.cooperfilme.domain.enums.StatusRoteiro;
import com.cooperfilme.domain.model.Cliente;
import com.cooperfilme.domain.model.Roteiro;

import java.time.LocalDateTime;
import java.util.UUID;

public class EnviarRoteiroService implements EnviarRoteiroUseCase {

    private final RoteiroRepositoryPort roteiroRepository;

    public EnviarRoteiroService(RoteiroRepositoryPort roteiroRepository) {
        this.roteiroRepository = roteiroRepository;
    }

    @Override
    public Roteiro enviar(String titulo, String texto, Cliente cliente) {
        Roteiro roteiro = new Roteiro();
        roteiro.setId(UUID.randomUUID());
        roteiro.setTitulo(titulo);
        roteiro.setTexto(texto);
        roteiro.setCliente(cliente);
        roteiro.setStatus(StatusRoteiro.AGUARDANDO_ANALISE);
        roteiro.setDataCriacao(LocalDateTime.now());

        return roteiroRepository.salvar(roteiro);
    }
}