package com.cooperfilme.domain.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Cliente {
    private final String nome;
    private final String email;
    private final String telefone;
}