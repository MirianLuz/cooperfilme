package com.cooperfilme.adapter.port.in.web;

import com.cooperfilme.adapter.port.in.RoteiroService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@Configuration
public class RoteiroServiceMockConfig {
    @Bean
    public RoteiroService roteiroService() {
        return mock(RoteiroService.class);
    }
}
