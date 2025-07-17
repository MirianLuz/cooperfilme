package com.cooperfilme.adapter.port.in;

import com.cooperfilme.domain.model.Cliente;
import com.cooperfilme.domain.model.Roteiro;

public interface EnviarRoteiroUseCase {
    Roteiro enviar(String titulo, String texto, Cliente cliente);
}