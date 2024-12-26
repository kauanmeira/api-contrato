package com.kauanmeira.api_contrato.domain.evento;

import com.kauanmeira.api_contrato.dto.evento.AtualizarEventoDTO;
import com.kauanmeira.api_contrato.dto.evento.EventoDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EventoMapper {
    EventoMapper INSTANCE = Mappers.getMapper(EventoMapper.class);

    Evento toObject(EventoDTO eventoDTO);

    @Mapping(source = "contrato.numeroContrato",target = "numeroContrato")
    EventoDTO toDTO(Evento evento);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Evento updateFromDTO(AtualizarEventoDTO atualizarEventoDTO, @MappingTarget Evento evento);
}

