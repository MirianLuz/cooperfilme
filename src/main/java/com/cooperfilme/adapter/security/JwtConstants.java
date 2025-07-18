package com.cooperfilme.adapter.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

public class JwtConstants {
    public static final SecretKey JWT_SECRET = Keys.hmacShaKeyFor(
            "chave-super-secreta-cooperfilme-tem-64-bytes-segura-para-HS512!!".getBytes(StandardCharsets.UTF_8)
    );

    public static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.HS512;
}