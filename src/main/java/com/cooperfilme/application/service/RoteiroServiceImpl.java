package com.cooperfilme.application.service;

import com.cooperfilme.adapter.port.in.RoteiroService;
import com.cooperfilme.adapter.port.in.web.dto.EnviarRoteiroRequest;
import com.cooperfilme.adapter.port.in.web.dto.AtualizarStatusRequest;
import com.cooperfilme.adapter.port.in.web.dto.VotoRequest;
import com.cooperfilme.adapter.port.out.RoteiroRepository;
import com.cooperfilme.adapter.port.out.UsuarioRepository;
import com.cooperfilme.domain.enums.Cargo;
import com.cooperfilme.domain.enums.StatusRoteiro;
import com.cooperfilme.domain.model.Roteiro;
import com.cooperfilme.domain.model.Usuario;
import com.cooperfilme.domain.model.Voto;
import com.cooperfilme.utils.StatusTransicaoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional
public class RoteiroServiceImpl implements RoteiroService {

    private final RoteiroRepository roteiroRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public Roteiro enviarRoteiro(EnviarRoteiroRequest request) {
        Roteiro roteiro = Roteiro.builder()
                .id(UUID.randomUUID())
                .titulo(request.getTitulo())
                .conteudo(request.getConteudo())
                .cliente(request.getCliente())
                .status(StatusRoteiro.AGUARDANDO_ANALISE)
                .criadoEm(LocalDateTime.now())
                .votos(new ArrayList<>())
                .build();

        return roteiroRepository.save(roteiro);
    }

    @Override
    public String consultarStatus(UUID id) {
        return roteiroRepository.findById(id)
                .map(r -> r.getStatus().name())
                .orElseThrow(() -> new NoSuchElementException("Roteiro não encontrado"));
    }

    @Override
    public Roteiro consultarDados(UUID id) {
        return roteiroRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Roteiro não encontrado"));
    }

    @Override
    public List<Roteiro> listarRoteiros() {
        return roteiroRepository.findAll();
    }

    @Override
    public Roteiro atualizarStatus(UUID id, AtualizarStatusRequest request) {
        Roteiro roteiro = roteiroRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Roteiro não encontrado"));

        Usuario usuario = usuarioRepository.findByEmail(request.getResponsavelEmail())
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));

        StatusRoteiro novoStatus = StatusRoteiro.valueOf(request.getNovoStatus().toString().toUpperCase());

        if (!StatusTransicaoValidator.isTransicaoValida(roteiro.getStatus(), novoStatus)) {
            throw new IllegalArgumentException(
                    "Transição inválida de " + roteiro.getStatus() + " para " + novoStatus
            );
        }

        validarNovoStatus(novoStatus, usuario);

        roteiro.setStatus(novoStatus);
        roteiro.setJustificativaStatus(request.getJustificativa());
        roteiro.setResponsavelAtual(usuario.getId());
        roteiro.setAtualizadoEm(LocalDateTime.now());

        return roteiroRepository.save(roteiro);
    }

    private static void validarNovoStatus(StatusRoteiro novoStatus, Usuario usuario) {
        if (novoStatus == StatusRoteiro.APROVADO || novoStatus == StatusRoteiro.RECUSADO) {
            if (usuario.getCargo() != Cargo.APROVADOR) {
                throw new SecurityException("Usuário não tem permissão para mudar o status para " + novoStatus);
            }
        } else if (novoStatus == StatusRoteiro.EM_ANALISE || novoStatus == StatusRoteiro.EM_APROVACAO) {
            if (usuario.getCargo() != Cargo.ANALISTA && usuario.getCargo() != Cargo.REVISOR) {
                throw new SecurityException("Usuário não tem permissão para mudar o status para " + novoStatus);
            }
        }
    }

    @Override
    public Roteiro votar(UUID id, VotoRequest request) {
        Roteiro roteiro = roteiroRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Roteiro não encontrado"));

        Usuario usuario = usuarioRepository.findByEmail(request.getEmailAprovador())
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));

        if (usuario.getCargo() != Cargo.APROVADOR) {
            throw new SecurityException("Usuário não tem permissão para votar");
        }

        if (roteiro.getStatus() != StatusRoteiro.AGUARDANDO_APROVACAO &&
                roteiro.getStatus() != StatusRoteiro.EM_APROVACAO) {
            throw new IllegalStateException("Roteiro não está em fase de aprovação");
        }

        roteiro.getVotos().add(new Voto(usuario, request.isAprovado()));

        atualizarStatusAprovacao(roteiro);
        roteiro.setJustificativaStatus(request.getJustificativa());

        return roteiroRepository.save(roteiro);
    }

    private void atualizarStatusAprovacao(Roteiro roteiro) {
        List<Voto> votos = roteiro.getVotos();

        if (votos.size() == 1) {
            roteiro.setStatus(StatusRoteiro.EM_APROVACAO);
        }

        long negativos = votos.stream().filter(v -> !v.isAprovado()).count();
        if (negativos > 0) {
            roteiro.setStatus(StatusRoteiro.RECUSADO);
            return;
        }

        if (votos.size() == 3 && negativos == 0) {
            roteiro.setStatus(StatusRoteiro.APROVADO);
        }
    }
}