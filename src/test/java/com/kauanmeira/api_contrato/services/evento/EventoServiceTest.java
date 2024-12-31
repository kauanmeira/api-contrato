package com.kauanmeira.api_contrato.services.evento;

import com.kauanmeira.api_contrato.domain.contrato.Contrato;
import com.kauanmeira.api_contrato.domain.evento.Evento;
import com.kauanmeira.api_contrato.domain.evento.EventoRepository;
import com.kauanmeira.api_contrato.domain.evento.EventoService;
import com.kauanmeira.api_contrato.domain.evento.TipoEvento;
import com.kauanmeira.api_contrato.dto.evento.AtualizarEventoDTO;
import com.kauanmeira.api_contrato.dto.evento.EventoDTO;
import com.kauanmeira.api_contrato.exceptions.AttusException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(MockitoJUnitRunner.class)
class EventoServiceTest {
    @InjectMocks
    private EventoService eventoService;

    @Mock
    private EventoRepository eventoRepository;

    private Evento evento;
    private EventoDTO eventoDTO;
    private Contrato contrato;
    private static final String MENSAGEM_EVENTO_NAO_ENCONTRADO = "Evento não encontrado para o número informado: ";
    private static final String MENSAGEM_EVENTO_DUPLICADO = "Já existe um evento do mesmo tipo para este contrato: ";


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        contrato = new Contrato();
        contrato.setNumeroContrato(1L);

        evento = new Evento();
        evento.setDescricaoEvento("Descrição teste de evento vinculado a um contrato");
        evento.setDataRegistro(LocalDate.now());
        evento.setTipoEvento(TipoEvento.ASSINATURA);
        evento.setContrato(contrato);

        eventoDTO = new EventoDTO();
        eventoDTO.setDescricaoEvento("Descrição teste de evento vinculado a um contrato");
        eventoDTO.setDataRegistro(LocalDate.now());
        eventoDTO.setTipoEvento(TipoEvento.ASSINATURA);
        eventoDTO.setNumeroContrato(123L);
    }

    @Test
    void cadastrarEventoComSucesso() {
        Mockito.when(eventoRepository.findByContrato_NumeroContratoAndTipoEvento(
                        contrato.getNumeroContrato(), TipoEvento.ASSINATURA))
                .thenReturn(Optional.empty());
        Mockito.when(eventoRepository.save(Mockito.any(Evento.class))).thenReturn(evento);

        Evento eventoCadastrado = eventoService.cadastrarEvento(evento);

        assertNotNull(eventoCadastrado);
        assertEquals(evento.getDescricaoEvento(), eventoCadastrado.getDescricaoEvento());
        assertEquals(evento.getTipoEvento(), eventoCadastrado.getTipoEvento());
        assertEquals(evento.getContrato().getNumeroContrato(), eventoCadastrado.getContrato().getNumeroContrato());
    }

    @Test
    void cadastrarEventoDuplicado() {
        Mockito.when(eventoRepository.findByContrato_NumeroContratoAndTipoEvento(
                        contrato.getNumeroContrato(), TipoEvento.ASSINATURA))
                .thenReturn(Optional.of(evento));

        AttusException exception = assertThrows(AttusException.class, () -> eventoService.cadastrarEvento(evento));

        assertEquals(MENSAGEM_EVENTO_DUPLICADO +
                        "Numero Contrato: " + evento.getContrato().getNumeroContrato() + ", " +
                        "Tipo Evento: " + evento.getTipoEvento()
                , exception.getMessage());
    }

    @Test
    void atualizarEventoComSucesso() {
        AtualizarEventoDTO atualizarEventoDTO = new AtualizarEventoDTO();
        atualizarEventoDTO.setDescricaoEvento("Descrição alterada");
        atualizarEventoDTO.setTipoEvento(TipoEvento.RESCISAO);
        atualizarEventoDTO.setDataRegistro(LocalDate.now().minusDays(1));

        Mockito.when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        Mockito.when(eventoRepository.save(Mockito.any(Evento.class))).thenReturn(evento);

        eventoService.atualizarEvento(atualizarEventoDTO, 1L);

        Mockito.verify(eventoRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(eventoRepository, Mockito.times(1)).save(Mockito.any(Evento.class));

        assertEquals(atualizarEventoDTO.getDescricaoEvento(), evento.getDescricaoEvento());
        assertEquals(atualizarEventoDTO.getTipoEvento(), evento.getTipoEvento());
    }

    @Test
    void atualizarEventoNaoEncontrado() {
        Mockito.when(eventoRepository.findById(1L)).thenReturn(Optional.empty());

        EventoService eventoServiceMock = eventoService;

        AttusException exception = assertThrows(AttusException.class, () ->
                eventoServiceMock.atualizarEvento(new AtualizarEventoDTO(), 1L)
        );

        assertEquals(MENSAGEM_EVENTO_NAO_ENCONTRADO + 1L, exception.getMessage());
    }

    @Test
    void atualizarEventoDuplicado() {
        AtualizarEventoDTO atualizarEventoDTO = new AtualizarEventoDTO();
        atualizarEventoDTO.setDescricaoEvento("Nova descrição");
        atualizarEventoDTO.setTipoEvento(TipoEvento.ASSINATURA);
        atualizarEventoDTO.setDataRegistro(LocalDate.now());

        Mockito.when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));

        Evento eventoDuplicado = new Evento();
        eventoDuplicado.setId(2L);
        eventoDuplicado.setContrato(evento.getContrato());
        eventoDuplicado.setTipoEvento(evento.getTipoEvento());

        Mockito.when(eventoRepository.findByContrato_NumeroContratoAndTipoEvento(
                        evento.getContrato().getNumeroContrato(), TipoEvento.ASSINATURA))
                .thenReturn(Optional.of(eventoDuplicado));

        AttusException exception = assertThrows(AttusException.class, () ->
                eventoService.atualizarEvento(atualizarEventoDTO, 1L)
        );

        assertEquals(MENSAGEM_EVENTO_DUPLICADO +
                        "Numero Contrato: " + evento.getContrato().getNumeroContrato() + ", " +
                        "Tipo Evento: " + TipoEvento.ASSINATURA,
                exception.getMessage());

        Mockito.verify(eventoRepository, Mockito.times(0)).save(Mockito.any(Evento.class));
    }


    @Test
    void buscarEventosPorNumeroContratoComSucesso() {
        Mockito.when(eventoRepository.findByContrato_NumeroContrato(1L))
                .thenReturn(List.of(eventoDTO));

        List<EventoDTO> eventos = eventoService.buscarEventosPorNumeroContrato(1L);

        assertNotNull(eventos);
        assertFalse(eventos.isEmpty());
        assertEquals(1, eventos.size());
    }

    @Test
    void buscarEventosPorNumeroContratoNaoEncontrado() {
        Mockito.when(eventoRepository.findByContrato_NumeroContrato(1L))
                .thenReturn(List.of());

        AttusException exception = assertThrows(AttusException.class, () -> eventoService.buscarEventosPorNumeroContrato(1L));

        assertEquals(MENSAGEM_EVENTO_NAO_ENCONTRADO + contrato.getNumeroContrato(), exception.getMessage());
    }

    @Test
    void buscarEventosDoContratoPorTipoComSucesso() {
        Mockito.when(eventoRepository.findByContrato_NumeroContratoAndTipoEvento(
                        contrato.getNumeroContrato(), TipoEvento.ASSINATURA))
                .thenReturn(Optional.of(evento));

        Evento eventoEncontrado = eventoService.buscarEventosDoContratoPorTipo(1L, TipoEvento.ASSINATURA);

        assertNotNull(eventoEncontrado);
        assertEquals(evento.getDescricaoEvento(), eventoEncontrado.getDescricaoEvento());
    }

    @Test
    void buscarEventosDoContratoPorTipoNaoEncontrado() {
        Mockito.when(eventoRepository.findByContrato_NumeroContratoAndTipoEvento(
                        contrato.getNumeroContrato(), TipoEvento.ASSINATURA))
                .thenReturn(Optional.empty());

        AttusException exception = assertThrows(AttusException.class, () -> eventoService.buscarEventosDoContratoPorTipo(1L, TipoEvento.ASSINATURA));

        assertEquals(MENSAGEM_EVENTO_NAO_ENCONTRADO + contrato.getNumeroContrato(), exception.getMessage());
    }

}