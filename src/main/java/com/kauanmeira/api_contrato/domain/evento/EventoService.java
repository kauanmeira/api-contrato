package com.kauanmeira.api_contrato.domain.evento;

import com.kauanmeira.api_contrato.dto.evento.AtualizarEventoDTO;
import com.kauanmeira.api_contrato.dto.evento.EventoDTO;
import com.kauanmeira.api_contrato.exceptions.AttusException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;
    private static final EventoMapper eventoMapper = EventoMapper.INSTANCE;
    private static final String MENSAGEM_EVENTO_NAO_ENCONTRADO = "Evento não encontrado para o número informado: ";
    private static final String MENSAGEM_EVENTO_DUPLICADO = "Já existe um evento do mesmo tipo para este contrato: ";

    public Evento cadastrarEvento(Evento evento) {
        Optional<Evento> eventoExistente = eventoRepository.findByContrato_NumeroContratoAndTipoEvento(evento.getContrato().getNumeroContrato(), evento.getTipoEvento());
        if (eventoExistente.isPresent()) {
            throw new AttusException(HttpStatus.FORBIDDEN, MENSAGEM_EVENTO_DUPLICADO +
                    "Numero Contrato: " + evento.getContrato().getNumeroContrato() +
                    "Tipo Evento: " + evento.getTipoEvento());
        }
        return gravar(evento);
    }

    public void atualizarEvento(AtualizarEventoDTO atualizarEventoDTO, Long id) {
        Evento eventoExistente = eventoRepository.findById(id).orElseThrow(() -> new AttusException(HttpStatus.NOT_FOUND, MENSAGEM_EVENTO_NAO_ENCONTRADO + id));
        Evento evento = eventoMapper.updateFromDTO(atualizarEventoDTO, eventoExistente);
        gravar(evento);
    }

    public List<EventoDTO> buscarEventosPorNumeroContrato(Long numeroContrato) {
        List<EventoDTO> eventos = eventoRepository.findByContrato_NumeroContrato(numeroContrato);

        if (!eventos.isEmpty()) return eventos;
        else
            throw new AttusException(HttpStatus.NOT_FOUND, MENSAGEM_EVENTO_NAO_ENCONTRADO + numeroContrato);
    }

    public Evento buscarEventosDoContratoPorTipo(Long numeroContrato, TipoEvento tipoEvento) {
        Optional<Evento> evento = eventoRepository.findByContrato_NumeroContratoAndTipoEvento(numeroContrato, tipoEvento);

        if (evento.isPresent()) return evento.get();
        else
            throw new AttusException(HttpStatus.NOT_FOUND, MENSAGEM_EVENTO_NAO_ENCONTRADO + numeroContrato);
    }

    private Evento gravar(Evento evento) {
        return this.eventoRepository.save(evento);
    }

}