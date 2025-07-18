package com.cooperfilme.adapter.port.in.web.dto;

import com.cooperfilme.domain.model.Cliente;
import lombok.Data;

@Data
public class EnviarRoteiroRequest {
    private String titulo;
    private String conteudo;
    private Cliente cliente;
}