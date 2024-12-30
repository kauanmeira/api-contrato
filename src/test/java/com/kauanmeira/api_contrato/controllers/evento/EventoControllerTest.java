package com.kauanmeira.api_contrato.controllers.evento;

import com.kauanmeira.api_contrato.controllers.EventoController;
import com.kauanmeira.api_contrato.domain.contrato.Contrato;
import com.kauanmeira.api_contrato.domain.contrato.ContratoService;
import com.kauanmeira.api_contrato.domain.evento.Evento;
import com.kauanmeira.api_contrato.domain.evento.EventoService;
import com.kauanmeira.api_contrato.domain.evento.TipoEvento;
import com.kauanmeira.api_contrato.dto.contrato.ContratoDTO;
import com.kauanmeira.api_contrato.dto.evento.AtualizarEventoDTO;
import com.kauanmeira.api_contrato.dto.evento.EventoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class EventoControllerTest {

    @Mock
    private EventoService eventoService;
    @Mock
    private ContratoService contratoService;

    @InjectMocks
    private EventoController eventoController;

    private MockMvc mockMvc;


    private Evento evento;
    private EventoDTO eventoDTO;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(contratoService.buscarPorNumero(123L)).thenReturn(new ContratoDTO());

        evento = new Evento();
        evento.setTipoEvento(TipoEvento.ASSINATURA);
        evento.setDataRegistro(LocalDate.now());
        evento.setDescricaoEvento("Evento de teste");

        eventoDTO = new EventoDTO();
        eventoDTO.setTipoEvento(TipoEvento.ASSINATURA);
        eventoDTO.setDataRegistro(LocalDate.now());
        eventoDTO.setDescricaoEvento("Evento de teste");

        Contrato contratoMock = new Contrato();
        contratoMock.setNumeroContrato(123L);
        evento.setContrato(contratoMock);

        mockMvc = MockMvcBuilders.standaloneSetup(eventoController).build();
    }


    @Test
    void cadastrarEvento() throws Exception {
        when(eventoService.cadastrarEvento(any(Evento.class))).thenAnswer(invocation -> {
            Evento eventoMock = invocation.getArgument(0);
            eventoMock.setContrato(new Contrato());
            eventoMock.getContrato().setNumeroContrato(123L);
            return eventoMock;
        });

        mockMvc.perform(MockMvcRequestBuilders.post("/evento")
                        .contentType("application/json")
                        .content("{\"tipoEvento\": \"ASSINATURA\", \"dataRegistro\": \"2024-12-30\", \"descricaoEvento\": \"Evento de teste\", \"numeroContrato\": 123}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tipoEvento").value("ASSINATURA"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.numeroContrato").value(123));

        verify(eventoService).cadastrarEvento(any(Evento.class));
    }


    @Test
    void atualizarEvento() throws Exception {
        doNothing().when(eventoService).atualizarEvento(any(AtualizarEventoDTO.class), any(Long.class));

        mockMvc.perform(MockMvcRequestBuilders.put("/evento/atualizar/1")
                        .contentType("application/json")
                        .content("{\"descricaoEvento\": \"Evento atualizado\", \"dataRegistro\": \"2024-12-30\", \"tipoEvento\": \"RESCISAO\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Evento atualizado com sucesso!"));

        verify(eventoService).atualizarEvento(any(AtualizarEventoDTO.class), any(Long.class));
    }

    @Test
    void buscarEventosPorNumeroContrato() throws Exception {
        LocalDate today = LocalDate.now();
        eventoDTO.setNumeroContrato(123L);
        eventoDTO.setDescricaoEvento("Evento de teste");
        eventoDTO.setTipoEvento(TipoEvento.ASSINATURA);
        eventoDTO.setDataRegistro(today);

        when(eventoService.buscarEventosPorNumeroContrato(123L)).thenReturn(List.of(eventoDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/evento/123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].numeroContrato").value(123L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].descricaoEvento").value("Evento de teste"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].tipoEvento").value("ASSINATURA"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].dataRegistro").value(today.toString()));

        verify(eventoService).buscarEventosPorNumeroContrato(123L);
    }


    @Test
    void buscarEventoPorTipo() throws Exception {
        when(eventoService.buscarEventosDoContratoPorTipo(123L, TipoEvento.ASSINATURA)).thenReturn(evento);

        mockMvc.perform(MockMvcRequestBuilders.get("/evento/por-tipo/123")
                        .param("tipo", "ASSINATURA"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tipoEvento").value("ASSINATURA"));

        verify(eventoService).buscarEventosDoContratoPorTipo(123L, TipoEvento.ASSINATURA);
    }

    @Test
    void cadastrarEventoComErro() throws Exception {
        EventoDTO eventoInvalido = new EventoDTO();
        eventoInvalido.setTipoEvento(null);
        eventoInvalido.setDataRegistro(null);
        eventoInvalido.setDescricaoEvento("");

        mockMvc.perform(MockMvcRequestBuilders.post("/evento")
                        .contentType("application/json")
                        .content("{\"tipoEvento\": null, \"dataRegistro\": null, \"descricaoEvento\": \"\", \"numeroContrato\": null}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
