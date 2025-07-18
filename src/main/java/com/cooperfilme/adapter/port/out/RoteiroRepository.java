package com.cooperfilme.adapter.port.out;

import com.cooperfilme.domain.model.Roteiro;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoteiroRepository {
    Roteiro save(Roteiro roteiro);
    Optional<Roteiro> findById(UUID id);
    List<Roteiro> findAll();
}