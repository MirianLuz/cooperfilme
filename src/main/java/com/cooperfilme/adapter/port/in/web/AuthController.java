package com.cooperfilme.adapter.port.in.web;

import com.cooperfilme.adapter.port.in.AuthService;
import com.cooperfilme.adapter.port.in.web.dto.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String token = authService.login(request.getEmail(), request.getSenha());
        return ResponseEntity.ok(token);
    }
}