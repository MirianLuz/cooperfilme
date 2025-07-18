package com.cooperfilme.domain.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Cliente {
    private String nome;
    private String email;
    private String telefone;
}