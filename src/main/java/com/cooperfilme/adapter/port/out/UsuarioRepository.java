package com.cooperfilme.adapter.port.out;

import com.cooperfilme.domain.model.Usuario;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findById(UUID id);
    Usuario save(Usuario usuario);
}