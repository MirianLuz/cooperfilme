package com.cooperfilme.domain.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class Usuario {
    private final UUID id;
    private final String nome;
    private final String email;
    private final Cargo cargo;
    private final String senha;
}