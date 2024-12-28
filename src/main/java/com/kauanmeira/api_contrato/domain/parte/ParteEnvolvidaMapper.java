package com.kauanmeira.api_contrato.domain.parte;


import com.kauanmeira.api_contrato.dto.parte.AtualizarParteEnvolvidaDTO;
import com.kauanmeira.api_contrato.dto.parte.ParteEnvolvidaDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ParteEnvolvidaMapper {
    ParteEnvolvidaMapper INSTANCE = Mappers.getMapper(ParteEnvolvidaMapper.class);

    ParteEnvolvida toObject(ParteEnvolvidaDTO parteEnvolvidaDTO);

    ParteEnvolvidaDTO toDTO(ParteEnvolvida parteEnvolvida);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ParteEnvolvida updateFromDTO(AtualizarParteEnvolvidaDTO atualizarParteEnvolvidaDTO, @MappingTarget ParteEnvolvida parteEnvolvida);
}

