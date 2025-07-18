package com.cooperfilme.adapter.port.in.web.dto;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class LoginRequest {
    private String email;
    private String senha;
}