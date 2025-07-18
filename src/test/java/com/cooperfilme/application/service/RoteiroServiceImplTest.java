package com.cooperfilme.application.service;

import com.cooperfilme.adapter.port.in.web.dto.AtualizarStatusRequest;
import com.cooperfilme.adapter.port.in.web.dto.EnviarRoteiroRequest;
import com.cooperfilme.adapter.port.in.web.dto.VotoRequest;
import com.cooperfilme.adapter.port.out.RoteiroRepository;
import com.cooperfilme.adapter.port.out.UsuarioRepository;
import com.cooperfilme.domain.enums.Cargo;
import com.cooperfilme.domain.enums.StatusRoteiro;
import com.cooperfilme.domain.model.Cliente;
import com.cooperfilme.domain.model.Roteiro;
import com.cooperfilme.domain.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class RoteiroServiceImplTest {

    @Mock
    private RoteiroRepository roteiroRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private RoteiroServiceImpl roteiroService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveEnviarRoteiroComStatusAguardandoAnalise() {
        EnviarRoteiroRequest request = new EnviarRoteiroRequest();
        request.setTitulo("Título");
        request.setConteudo("Conteúdo");
        request.setCliente(new Cliente("Cliente A", "cliente@exemplo.com", "12345"));

        when(roteiroRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Roteiro salvo = roteiroService.enviarRoteiro(request);

        assertNotNull(salvo.getId());
        assertEquals("Título", salvo.getTitulo());
        assertEquals(StatusRoteiro.AGUARDANDO_ANALISE, salvo.getStatus());
        verify(roteiroRepository, times(1)).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoConsultarStatusInexistente() {
        UUID id = UUID.randomUUID();
        when(roteiroRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> roteiroService.consultarStatus(id));
    }

    @Test
    void deveAtualizarStatusComUsuarioValido() {
        UUID id = UUID.randomUUID();
        Roteiro roteiro = Roteiro.builder()
                .id(id)
                .status(StatusRoteiro.AGUARDANDO_ANALISE)
                .build();

        Usuario analista = Usuario.builder()
                .id(UUID.randomUUID())
                .cargo(Cargo.ANALISTA)
                .email("analista@exemplo.com")
                .build();

        AtualizarStatusRequest req = new AtualizarStatusRequest();
        req.setResponsavelEmail(analista.getEmail());
        req.setNovoStatus(StatusRoteiro.EM_ANALISE);
        req.setJustificativa("Iniciando análise");

        when(roteiroRepository.findById(id)).thenReturn(Optional.of(roteiro));
        when(usuarioRepository.findByEmail(analista.getEmail())).thenReturn(Optional.of(analista));
        when(roteiroRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Roteiro atualizado = roteiroService.atualizarStatus(id, req);

        assertEquals(StatusRoteiro.EM_ANALISE, atualizado.getStatus());
        assertEquals(analista.getId(), atualizado.getResponsavelAtual());
        assertEquals("Iniciando análise", atualizado.getJustificativaStatus());
        verify(roteiroRepository, times(1)).save(any());
    }

    @Test
    void deveLancarErroEmTransicaoInvalida() {
        UUID id = UUID.randomUUID();
        Roteiro roteiro = Roteiro.builder()
                .id(id)
                .status(StatusRoteiro.AGUARDANDO_ANALISE)
                .build();

        Usuario usuario = Usuario.builder()
                .cargo(Cargo.ANALISTA)
                .email("usuario@exemplo.com")
                .build();

        AtualizarStatusRequest req = new AtualizarStatusRequest();
        req.setResponsavelEmail(usuario.getEmail());
        req.setNovoStatus(StatusRoteiro.APROVADO); // Salto inválido

        when(roteiroRepository.findById(id)).thenReturn(Optional.of(roteiro));
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> roteiroService.atualizarStatus(id, req));
        assertTrue(ex.getMessage().contains("Transição inválida"));
    }

    @Test
    void deveLancarErroSeUsuarioNaoForAprovadorAoVotar() {
        UUID id = UUID.randomUUID();

        Usuario usuario = Usuario.builder()
                .cargo(Cargo.ANALISTA)
                .email("analista@exemplo.com")
                .build();

        Roteiro roteiro = Roteiro.builder()
                .status(StatusRoteiro.AGUARDANDO_APROVACAO)
                .build();

        VotoRequest votoReq = new VotoRequest();
        votoReq.setEmailAprovador(usuario.getEmail());
        votoReq.setAprovado(true);

        when(roteiroRepository.findById(id)).thenReturn(Optional.of(roteiro));
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

        assertThrows(SecurityException.class, () -> roteiroService.votar(id, votoReq));
    }
}