package com.cooperfilme.adapter.port.in.web;

import com.cooperfilme.adapter.port.in.RoteiroService;
import com.cooperfilme.adapter.port.in.web.dto.AtualizarStatusRequest;
import com.cooperfilme.adapter.port.in.web.dto.EnviarRoteiroRequest;
import com.cooperfilme.adapter.port.in.web.dto.VotoRequest;
import com.cooperfilme.domain.enums.StatusRoteiro;
import com.cooperfilme.domain.model.Cliente;
import com.cooperfilme.domain.model.Roteiro;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(RoteiroController.class)
@Import(RoteiroServiceMockConfig.class)
public class RoteiroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoteiroService roteiroService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void enviarRoteiro_deveRetornarRoteiroCriado() throws Exception {
        Cliente cliente = new Cliente("Cliente X", "joao@email.com", "7399999999");
        EnviarRoteiroRequest request = new EnviarRoteiroRequest();
        request.setTitulo("Título Teste");
        request.setConteudo("Conteúdo do roteiro");
        request.setCliente(cliente);

        Roteiro roteiroMock = Roteiro.builder()
                .id(UUID.randomUUID())
                .titulo(request.getTitulo())
                .conteudo(request.getConteudo())
                .cliente(request.getCliente())
                .status(StatusRoteiro.AGUARDANDO_ANALISE)
                .build();

        Mockito.when(roteiroService.enviarRoteiro(any())).thenReturn(roteiroMock);

        mockMvc.perform(post("/roteiros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(roteiroMock.getId().toString()))
                .andExpect(jsonPath("$.titulo").value("Título Teste"));
    }

    @Test
    void consultarStatus_deveRetornarStatus() throws Exception {
        UUID id = UUID.randomUUID();
        String statusMock = StatusRoteiro.AGUARDANDO_ANALISE.name();

        Mockito.when(roteiroService.consultarStatus(id)).thenReturn(statusMock);

        mockMvc.perform(get("/roteiros/{id}/status", id))
                .andExpect(status().isOk())
                .andExpect(content().string(statusMock));
    }

    @Test
    void consultarDados_deveRetornarRoteiro() throws Exception {
        UUID id = UUID.randomUUID();
        Cliente cliente = new Cliente("Cliente X", "joao@email.com", "7399999999");

        Roteiro roteiroMock = Roteiro.builder()
                .id(id)
                .titulo("Titulo")
                .conteudo("Conteudo")
                .cliente(cliente)
                .status(StatusRoteiro.AGUARDANDO_ANALISE)
                .build();

        Mockito.when(roteiroService.consultarDados(id)).thenReturn(roteiroMock);

        mockMvc.perform(get("/roteiros/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.titulo").value("Titulo"));
    }

    @Test
    void listarRoteiros_deveRetornarLista() throws Exception {
        Roteiro roteiro1 = Roteiro.builder()
                .id(UUID.randomUUID())
                .titulo("Roteiro 1")
                .build();

        Roteiro roteiro2 = Roteiro.builder()
                .id(UUID.randomUUID())
                .titulo("Roteiro 2")
                .build();

        Mockito.when(roteiroService.listarRoteiros()).thenReturn(List.of(roteiro1, roteiro2));

        mockMvc.perform(get("/roteiros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].titulo").value("Roteiro 1"))
                .andExpect(jsonPath("$[1].titulo").value("Roteiro 2"));
    }

    @Test
    void mudarStatus_deveAtualizarStatus() throws Exception {
        UUID id = UUID.randomUUID();

        AtualizarStatusRequest request = new AtualizarStatusRequest();
        request.setNovoStatus(StatusRoteiro.EM_ANALISE);
        request.setResponsavelEmail("analista@cooperfilme.com");
        request.setJustificativa("Mudando status");

        Roteiro roteiroMock = Roteiro.builder()
                .id(id)
                .status(StatusRoteiro.EM_ANALISE)
                .build();

        Mockito.when(roteiroService.atualizarStatus(eq(id), any())).thenReturn(roteiroMock);

        mockMvc.perform(post("/roteiros/{id}/mudar-status", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("EM_ANALISE"));
    }

    @Test
    void votar_deveRegistrarVoto() throws Exception {
        UUID id = UUID.randomUUID();

        VotoRequest request = new VotoRequest();
        request.setEmailAprovador("aprovador@cooperfilme.com");
        request.setAprovado(true);
        request.setJustificativa("Voto aprovado");

        Roteiro roteiroMock = Roteiro.builder()
                .id(id)
                .status(StatusRoteiro.APROVADO)
                .build();

        Mockito.when(roteiroService.votar(eq(id), any())).thenReturn(roteiroMock);

        mockMvc.perform(post("/roteiros/{id}/votar", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APROVADO"));
    }
}