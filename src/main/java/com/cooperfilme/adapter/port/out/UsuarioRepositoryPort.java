package com.cooperfilme.adapter.port.out;

import com.cooperfilme.domain.model.Usuario;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepositoryPort {
    Optional<Usuario> buscarPorId(UUID id);
    Optional<Usuario> buscarPorEmail(String email);
    Usuario salvar(Usuario usuario);
}