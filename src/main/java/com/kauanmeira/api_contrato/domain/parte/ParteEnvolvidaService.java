package com.kauanmeira.api_contrato.domain.parte;

import com.kauanmeira.api_contrato.dto.parte.AtualizarParteEnvolvidaDTO;
import com.kauanmeira.api_contrato.dto.parte.ParteEnvolvidaDTO;
import com.kauanmeira.api_contrato.exceptions.AttusException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ParteEnvolvidaService {

    private final ParteEnvolvidaRepository parteEnvolvidaRepository;
    private static final ParteEnvolvidaMapper parteEnvolvidaMapper = ParteEnvolvidaMapper.INSTANCE;

    public ParteEnvolvida cadastrar(ParteEnvolvidaDTO parteEnvolvidaDTO) {
        ParteEnvolvida parteEnvolvida = parteEnvolvidaMapper.toObject(parteEnvolvidaDTO);
        String inscricaoFederal = parteEnvolvidaDTO.getInscricaoFederal();
        validarInscricaoFederal(inscricaoFederal);
        parteEnvolvida.setInscricaoFederal(inscricaoFederal);
        return gravar(parteEnvolvida);
    }

    private ParteEnvolvida gravar(ParteEnvolvida parteEnvolvida) {
        return this.parteEnvolvidaRepository.save(parteEnvolvida);
    }

    public void atualizarParteEnvolvida(AtualizarParteEnvolvidaDTO atualizarParteEnvolvidaDTO, Long id) {
        ParteEnvolvida parteEnvolvidaExistente = parteEnvolvidaRepository.findById(id).orElseThrow(() -> new AttusException(HttpStatus.NOT_FOUND, "ParteEnvolvida não encontrado para o Id inserido."));
        ParteEnvolvida parteEnvolvida = parteEnvolvidaMapper.updateFromDTO(atualizarParteEnvolvidaDTO, parteEnvolvidaExistente);
        gravar(parteEnvolvida);
    }


    private void validarInscricaoFederal(String inscricaoFederal) {
        if (inscricaoFederal != null && !inscricaoFederal.isBlank()) {
            Pattern pattern = Pattern.compile("^\\d+$");
            if (!pattern.matcher(inscricaoFederal).matches()) {
                throw new AttusException(HttpStatus.BAD_REQUEST, "A inscrição federal deve conter apenas caracteres numéricos.");
            }
        }
    }

}