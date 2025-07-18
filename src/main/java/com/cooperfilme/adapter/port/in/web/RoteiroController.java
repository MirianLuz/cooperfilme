package com.cooperfilme.adapter.port.in.web;

import com.cooperfilme.adapter.port.in.RoteiroService;
import com.cooperfilme.adapter.port.in.web.dto.EnviarRoteiroRequest;
import com.cooperfilme.adapter.port.in.web.dto.AtualizarStatusRequest;
import com.cooperfilme.adapter.port.in.web.dto.VotoRequest;
import com.cooperfilme.domain.model.Roteiro;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/roteiros")
@RequiredArgsConstructor
@Slf4j
public class RoteiroController {

    private final RoteiroService roteiroService;

    @PostMapping
    public ResponseEntity<Roteiro> enviarRoteiro(@RequestBody EnviarRoteiroRequest request) {
        log.info("Recebendo roteiro para envio: {}", request);
        return ResponseEntity.ok(roteiroService.enviarRoteiro(request));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<String> consultarStatus(@PathVariable UUID id) {
        log.info("Consultando status do roteiro: {}", id);
        return ResponseEntity.ok(roteiroService.consultarStatus(id));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ANALISTA', 'REVISOR', 'APROVADOR')")
    public ResponseEntity<Roteiro> consultarDados(@PathVariable UUID id) {
        log.info("Consultando dados do roteiro: {}", id);
        return ResponseEntity.ok(roteiroService.consultarDados(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ANALISTA', 'REVISOR', 'APROVADOR')")
    public ResponseEntity<List<Roteiro>> listarRoteiros() {
        log.info("Listando todos os roteiros");
        return ResponseEntity.ok(roteiroService.listarRoteiros());
    }

    @PostMapping("/{id}/mudar-status")
    @PreAuthorize("hasAnyRole('ANALISTA', 'REVISOR', 'APROVADOR')")
    public ResponseEntity<Roteiro> mudarStatus(@PathVariable UUID id, @RequestBody AtualizarStatusRequest request) {
        log.info("Mudando status do roteiro {} para {}", id, request);
        return ResponseEntity.ok(roteiroService.atualizarStatus(id, request));
    }

    @PostMapping("/{id}/votar")
    @PreAuthorize("hasRole('APROVADOR')")
    public ResponseEntity<Roteiro> votar(@PathVariable UUID id, @RequestBody VotoRequest request) {
        log.info("Aprovador votando no roteiro {}: {}", id, request);
        return ResponseEntity.ok(roteiroService.votar(id, request));
    }
}