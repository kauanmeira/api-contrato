package com.kauanmeira.api_contrato.controllers;

import com.kauanmeira.api_contrato.domain.contrato.ContratoMapper;
import com.kauanmeira.api_contrato.domain.contrato.ContratoService;
import com.kauanmeira.api_contrato.domain.evento.Evento;
import com.kauanmeira.api_contrato.domain.evento.EventoMapper;
import com.kauanmeira.api_contrato.domain.evento.EventoService;
import com.kauanmeira.api_contrato.domain.evento.TipoEvento;
import com.kauanmeira.api_contrato.dto.contrato.ContratoDTO;
import com.kauanmeira.api_contrato.dto.evento.AtualizarEventoDTO;
import com.kauanmeira.api_contrato.dto.evento.EventoDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evento")
@RequiredArgsConstructor
public class EventoController {
    private final EventoService eventoService;
    private final ContratoService contratoService;
    private static final EventoMapper eventoMapper = EventoMapper.INSTANCE;
    private static final ContratoMapper contratoMapper = ContratoMapper.INSTANCE;


    @PostMapping
    public EventoDTO cadastrarEvento(@Valid @RequestBody EventoDTO eventoDTO) {
        Evento evento = eventoMapper.toObject(eventoDTO);

        ContratoDTO contrato = contratoService.buscarPorNumero(eventoDTO.getNumeroContrato());
        evento.setContrato(contratoMapper.toObject(contrato));

        return eventoMapper.toDTO(eventoService.cadastrarEvento(evento));
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<String> atualizarEvento(@PathVariable Long id, @Valid @RequestBody AtualizarEventoDTO atualizarEventoDTO) {
        eventoService.atualizarEvento(atualizarEventoDTO, id);
        return ResponseEntity.status(HttpStatus.OK).body("Evento atualizado com sucesso!");
    }


    @GetMapping("/{numeroContrato}")
    public List<Evento> buscarEventosDoContratoPorNumero(@PathVariable Long numeroContrato) {
        return eventoService.buscarEventosPorNumeroContrato(numeroContrato);
    }

    @GetMapping("/por-tipo/{numeroContrato}")
    public Evento buscarEventosDoContratoPorTipo(@PathVariable Long numeroContrato, @RequestParam("tipo") TipoEvento tipoEvento) {
        return eventoService.buscarEventosDoContratoPorTipo(numeroContrato, tipoEvento);
    }
}