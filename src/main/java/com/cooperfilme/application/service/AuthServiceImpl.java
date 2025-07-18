package com.cooperfilme.application.service;

import com.cooperfilme.adapter.port.in.AuthService;
import com.cooperfilme.adapter.port.out.UsuarioRepository;
import com.cooperfilme.adapter.security.JwtConstants;
import com.cooperfilme.domain.model.Usuario;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String login(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new RuntimeException("Senha inválida");
        }

        Date agora = new Date();
        Date expiracao = new Date(agora.getTime() + 1000 * 60 * 60);

        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("cargo", usuario.getCargo().name())
                .setIssuedAt(agora)
                .setExpiration(expiracao)
                .signWith(JwtConstants.JWT_SECRET, JwtConstants.ALGORITHM)
                .compact();
    }
}