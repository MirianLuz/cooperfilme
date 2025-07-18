package com.cooperfilme.adapter.port.in.web.dto;

import com.cooperfilme.domain.enums.StatusRoteiro;
import lombok.Data;

@Data
public class AtualizarStatusRequest {
    private StatusRoteiro novoStatus;
    private String justificativa;
    private String responsavelEmail;
}