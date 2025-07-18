package com.cooperfilme.adapter.port.out;

import com.cooperfilme.domain.model.Roteiro;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class RoteiroRepositoryImpl implements RoteiroRepository {

    private final Map<UUID, Roteiro> store = new ConcurrentHashMap<>();

    @Override
    public Roteiro save(Roteiro roteiro) {
        store.put(roteiro.getId(), roteiro);
        return roteiro;
    }

    @Override
    public Optional<Roteiro> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Roteiro> findAll() {
        return new ArrayList<>(store.values());
    }
}