package com.cooperfilme.domain.model;

import com.cooperfilme.domain.enums.StatusRoteiro;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class HistoricoStatus {
    private final StatusRoteiro status;
    private final LocalDateTime dataHora;
    private final UUID responsavelId;
}
