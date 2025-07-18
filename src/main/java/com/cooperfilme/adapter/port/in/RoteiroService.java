package com.cooperfilme.adapter.port.in;

import com.cooperfilme.adapter.port.in.web.dto.EnviarRoteiroRequest;
import com.cooperfilme.adapter.port.in.web.dto.AtualizarStatusRequest;
import com.cooperfilme.adapter.port.in.web.dto.VotoRequest;
import com.cooperfilme.domain.model.Roteiro;

import java.util.List;
import java.util.UUID;

public interface RoteiroService {
    Roteiro enviarRoteiro(EnviarRoteiroRequest request);
    String consultarStatus(UUID id);
    Roteiro consultarDados(UUID id);
    List<Roteiro> listarRoteiros();
    Roteiro atualizarStatus(UUID id, AtualizarStatusRequest request);
    Roteiro votar(UUID id, VotoRequest request);
}