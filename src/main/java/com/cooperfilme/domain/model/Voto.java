package com.cooperfilme.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Voto {
    private String emailAprovador;
    private boolean aprovado;
    private String justificativa;

    public Voto(Usuario usuario, boolean aprovado) {
        this.emailAprovador = usuario.getEmail();
        this.aprovado = aprovado;
    }
}