package com.cooperfilme.application.service;

import com.cooperfilme.adapter.port.out.UsuarioRepository;
import com.cooperfilme.adapter.security.JwtConstants;
import com.cooperfilme.domain.enums.Cargo;
import com.cooperfilme.domain.model.Usuario;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class AuthServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loginSucessoDeveRetornarTokenJwt() {
        String email = "user@cooperfilme.com";
        String senhaRaw = "123456";
        String senhaEncoded = "$2a$10$xyz..."; // exemplo bcrypt hash

        Usuario usuario = Usuario.builder()
                .email(email)
                .senha(senhaEncoded)
                .cargo(Cargo.ANALISTA)
                .build();

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(senhaRaw, senhaEncoded)).thenReturn(true);

        String token = authService.login(email, senhaRaw);

        assertNotNull(token);
        assertTrue(token.startsWith("ey")); // JWT tokens em base64 normalmente começam assim

        // Você pode validar claims decodificando o token se quiser
        var claims = Jwts.parserBuilder()
                .setSigningKey(JwtConstants.JWT_SECRET)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(email, claims.getSubject());
        assertEquals("ANALISTA", claims.get("cargo"));
    }

    @Test
    void loginDeveLancarErroQuandoUsuarioNaoEncontrado() {
        String email = "naoexiste@cooperfilme.com";

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authService.login(email, "senha"));

        assertEquals("Usuário não encontrado", ex.getMessage());
    }

    @Test
    void loginDeveLancarErroQuandoSenhaInvalida() {
        String email = "user@cooperfilme.com";
        String senhaRaw = "senhaErrada";
        String senhaEncoded = "$2a$10$xyz...";

        Usuario usuario = Usuario.builder()
                .email(email)
                .senha(senhaEncoded)
                .cargo(Cargo.ANALISTA)
                .build();

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(senhaRaw, senhaEncoded)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authService.login(email, senhaRaw));

        assertEquals("Senha inválida", ex.getMessage());
    }
}