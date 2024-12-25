package com.kauanmeira.api_contrato.domain.evento;

import com.kauanmeira.api_contrato.dto.EventoDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EventoMapper {
    EventoMapper INSTANCE = Mappers.getMapper(EventoMapper.class);

    Evento toObject(EventoDTO eventoDTO);

    EventoDTO toDTO(Evento evento);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Evento updateFromDTO(EventoDTO eventoDTO, @MappingTarget Evento evento);
}

