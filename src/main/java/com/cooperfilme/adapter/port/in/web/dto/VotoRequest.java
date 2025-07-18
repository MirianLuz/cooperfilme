package com.cooperfilme.adapter.port.in.web.dto;

import lombok.Data;

@Data
public class VotoRequest {
    private String emailAprovador;
    private boolean aprovado;
    private String justificativa;
}