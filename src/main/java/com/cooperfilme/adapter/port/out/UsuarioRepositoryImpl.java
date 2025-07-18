package com.cooperfilme.adapter.port.out;

import com.cooperfilme.domain.enums.Cargo;
import com.cooperfilme.domain.model.Usuario;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Slf4j
public class UsuarioRepositoryImpl implements UsuarioRepository {

    private final Map<String, Usuario> usuariosPorEmail = new ConcurrentHashMap<>();

    private final PasswordEncoder passwordEncoder;

    public UsuarioRepositoryImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        cadastrarUsuario("Ana Analista", "analista@cooperfilme.com", "senha123", Cargo.ANALISTA);
        cadastrarUsuario("Rafael Revisor", "revisor@cooperfilme.com", "senha123", Cargo.REVISOR);
        cadastrarUsuario("Aprovador 1", "aprovador1@cooperfilme.com", "senha123", Cargo.APROVADOR);
        cadastrarUsuario("Aprovador 2", "aprovador2@cooperfilme.com", "senha123", Cargo.APROVADOR);
        cadastrarUsuario("Aprovador 3", "aprovador3@cooperfilme.com", "senha123", Cargo.APROVADOR);

        log.info("Usuários padrão cadastrados: analista, revisor e 3 aprovadores.");
        log.info("Credenciais padrão:");
        log.info("analista@cooperfilme.com / senha123");
        log.info("revisor@cooperfilme.com / senha123");
        log.info("aprovador1@cooperfilme.com / senha123");
        log.info("aprovador2@cooperfilme.com / senha123");
        log.info("aprovador3@cooperfilme.com / senha123");
    }

    private void cadastrarUsuario(String nome, String email, String senha, Cargo cargo) {
        Usuario usuario = Usuario.builder()
                .id(UUID.randomUUID())
                .nome(nome)
                .email(email)
                .senha(passwordEncoder.encode(senha))
                .cargo(cargo)
                .build();
        usuariosPorEmail.put(email, usuario);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return Optional.ofNullable(usuariosPorEmail.get(email));
    }

    @Override
    public Optional<Usuario> findById(UUID id) {
        return usuariosPorEmail.values().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    @Override
    public Usuario save(Usuario usuario) {
        usuariosPorEmail.put(usuario.getEmail(), usuario);
        return usuario;
    }
}