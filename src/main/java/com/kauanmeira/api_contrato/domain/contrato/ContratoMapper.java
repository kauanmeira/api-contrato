package com.kauanmeira.api_contrato.domain.contrato;

import com.kauanmeira.api_contrato.domain.dto.ContratoDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ContratoMapper {
    ContratoMapper INSTANCE = Mappers.getMapper(ContratoMapper.class);

    Contrato toObject(ContratoDTO contratoDTO);

    ContratoDTO toDTO(Contrato contrato);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Contrato updateFromDTO(ContratoDTO contratoDTO, @MappingTarget Contrato contrato);
}
