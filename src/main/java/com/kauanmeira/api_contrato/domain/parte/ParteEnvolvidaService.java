package com.kauanmeira.api_contrato.domain.parte;

import com.kauanmeira.api_contrato.exceptions.AttusException;
import com.kauanmeira.api_contrato.dto.AtualizarParteEnvolvidaDTO;
import com.kauanmeira.api_contrato.dto.ParteEnvolvidaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ParteEnvolvidaService {

    private final ParteEnvolvidaRepository parteEnvolvidaRepository;
    private final ParteEnvolvidaMapper parteEnvolvidaMapper = ParteEnvolvidaMapper.INSTANCE;

    public ParteEnvolvida cadastrar(ParteEnvolvidaDTO parteEnvolvidaDTO) {
        ParteEnvolvida parteEnvolvida = parteEnvolvidaMapper.toObject(parteEnvolvidaDTO);
        String inscricaoFederal = parteEnvolvidaDTO.getInscricaoFederal();
        validarInscricaoFederal(inscricaoFederal);
        parteEnvolvida.setInscricaoFederal(inscricaoFederal);
        return gravar(parteEnvolvida);
    }

    public ParteEnvolvidaDTO buscarPorId(Long id) {
        ParteEnvolvida parteEnvolvida = parteEnvolvidaRepository.findById(id)
                .orElseThrow(() -> new AttusException(HttpStatus.NOT_FOUND, "Parte Envolvida não encontrada para o Id informado."));
        return parteEnvolvidaMapper.toDTO(parteEnvolvida);
    }

    private ParteEnvolvida gravar(ParteEnvolvida parteEnvolvida) {
        return this.parteEnvolvidaRepository.save(parteEnvolvida)                ;
    }

    public void atualizar(AtualizarParteEnvolvidaDTO atualizarParteEnvolvidaDTO, Long id) {
        ParteEnvolvida parteEnvolvidaExistente = parteEnvolvidaRepository.findById(id).orElseThrow(() -> new AttusException(HttpStatus.NOT_FOUND, "ParteEnvolvida não encontrado para o Id inserido."));
        ParteEnvolvida parteEnvolvida = parteEnvolvidaMapper.updateFromDTO(atualizarParteEnvolvidaDTO, parteEnvolvidaExistente);
        gravar(parteEnvolvida);
    }

    public void deletar(Long id) {
        ParteEnvolvida parteEnvolvida = parteEnvolvidaRepository.findById(id).orElseThrow(() ->
                new AttusException(HttpStatus.NOT_FOUND, "ParteEnvolvida não encontrada para o Id inserido."));
        parteEnvolvidaRepository.delete(parteEnvolvida);
    }

    private String validarInscricaoFederal(String inscricaoFederal) {
        if (inscricaoFederal != null && !inscricaoFederal.isBlank()) {
            Pattern pattern = Pattern.compile("^\\d+$");
            if (!pattern.matcher(inscricaoFederal).matches()) {
                throw new AttusException(HttpStatus.BAD_REQUEST, "A inscrição federal deve conter apenas caracteres numéricos.");
            }
        }
        return inscricaoFederal;
    }

}