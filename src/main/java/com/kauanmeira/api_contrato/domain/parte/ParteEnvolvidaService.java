package com.kauanmeira.api_contrato.domain.parte;

import com.kauanmeira.api_contrato.domain.utils.InscricaoFederalValidator;
import com.kauanmeira.api_contrato.dto.parte.AtualizarParteEnvolvidaDTO;
import com.kauanmeira.api_contrato.dto.parte.ParteEnvolvidaDTO;
import com.kauanmeira.api_contrato.exceptions.AttusException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParteEnvolvidaService {

    private final ParteEnvolvidaRepository parteEnvolvidaRepository;
    private static final ParteEnvolvidaMapper parteEnvolvidaMapper = ParteEnvolvidaMapper.INSTANCE;

    public ParteEnvolvida cadastrar(ParteEnvolvidaDTO parteEnvolvidaDTO) {
        ParteEnvolvida parteEnvolvida = parteEnvolvidaMapper.toObject(parteEnvolvidaDTO);
        String inscricaoFederal = parteEnvolvidaDTO.getInscricaoFederal();
        parteEnvolvida.setInscricaoFederal(validarInscricaoFederal(inscricaoFederal));
        return gravar(parteEnvolvida);
    }

    public ParteEnvolvida gravar(ParteEnvolvida parteEnvolvida) {
        try {
            return this.parteEnvolvidaRepository.save(parteEnvolvida);
        } catch (Exception e) {
            throw new AttusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao salvar Parte Envolvida no banco de dados: " + e.getMessage());
        }
    }


    public void atualizarParteEnvolvida(AtualizarParteEnvolvidaDTO atualizarParteEnvolvidaDTO, Long id) {
        ParteEnvolvida parteEnvolvidaExistente = parteEnvolvidaRepository.findById(id).orElseThrow(() -> new AttusException(HttpStatus.NOT_FOUND, "ParteEnvolvida não encontrado para o Id inserido."));
        ParteEnvolvida parteEnvolvida = parteEnvolvidaMapper.updateFromDTO(atualizarParteEnvolvidaDTO, parteEnvolvidaExistente);
        gravar(parteEnvolvida);
    }


    public String validarInscricaoFederal(String inscricaoFederal) {
        if (inscricaoFederal != null) {
            inscricaoFederal = inscricaoFederal.trim().replaceAll("\\D", "");

            if (inscricaoFederal.length() == 11) {
                if (!InscricaoFederalValidator.isValid(inscricaoFederal)) {
                    throw new AttusException(HttpStatus.BAD_REQUEST, "CPF inválido: " + inscricaoFederal);
                }
            } else if (inscricaoFederal.length() == 14) {
                if (!InscricaoFederalValidator.isValid(inscricaoFederal)) {
                    throw new AttusException(HttpStatus.BAD_REQUEST, "CNPJ inválido: " + inscricaoFederal );
                }
            } else {
                throw new AttusException(HttpStatus.BAD_REQUEST, "Inscrição Federal inválida. Deve ter 11 ou 14 dígitos: " + inscricaoFederal);
            }
            return inscricaoFederal;
        }
        return null;
    }

}