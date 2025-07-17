package com.cooperfilme.domain.model;

import com.cooperfilme.domain.enums.StatusRoteiro;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Roteiro {
    private UUID id;
    private String titulo;
    private String texto;
    private Cliente cliente;
    private StatusRoteiro status;
    private UUID responsavelAtual;
    private LocalDateTime dataCriacao;
    private List<HistoricoStatus> historicoStatus = new ArrayList<>();

    public void adicionarHistorico(StatusRoteiro novoStatus, UUID responsavel) {
        historicoStatus.add(new HistoricoStatus(novoStatus, LocalDateTime.now(), responsavel));
        this.status = novoStatus;
        this.responsavelAtual = responsavel;
    }
}