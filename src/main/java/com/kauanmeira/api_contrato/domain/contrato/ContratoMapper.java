package com.kauanmeira.api_contrato.domain.contrato;

import com.kauanmeira.api_contrato.dto.contrato.AtualizarContratoDTO;
import com.kauanmeira.api_contrato.dto.contrato.ContratoDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ContratoMapper {
    ContratoMapper INSTANCE = Mappers.getMapper(ContratoMapper.class);

    Contrato toObject(ContratoDTO contratoDTO);

    ContratoDTO toDTO(Contrato contrato);

    @Mapping(target = "partesEnvolvidas", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Contrato updateFromDTO(AtualizarContratoDTO atualizarContratoDTO, @MappingTarget Contrato contrato);
}

