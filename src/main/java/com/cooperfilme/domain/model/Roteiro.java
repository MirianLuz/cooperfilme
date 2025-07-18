package com.cooperfilme.domain.model;

import com.cooperfilme.domain.enums.StatusRoteiro;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Roteiro {
    private UUID id;
    private String titulo;
    private String conteudo;
    private Cliente cliente;
    private StatusRoteiro status;
    private String justificativaStatus;
    private UUID responsavelAtual;

    private List<Voto> votos = new ArrayList<>();

    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public void adicionarVoto(Voto voto) {
        this.votos.add(voto);
    }
}