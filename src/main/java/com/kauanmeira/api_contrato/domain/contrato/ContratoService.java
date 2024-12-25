package com.kauanmeira.api_contrato.domain.contrato;

import com.kauanmeira.api_contrato.domain.parte.ParteEnvolvida;
import com.kauanmeira.api_contrato.dto.AtualizarContratoDTO;
import com.kauanmeira.api_contrato.dto.ContratoDTO;
import com.kauanmeira.api_contrato.exceptions.AttusException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContratoService {
    private final ContratoRepository contratoRepository;

    private final ContratoMapper contratoMapper = ContratoMapper.INSTANCE;

    public ContratoDTO cadastrar(ContratoDTO contratoDTO, List<ParteEnvolvida> parteEnvolvidas) {
        Contrato contrato = contratoMapper.toObject(contratoDTO);
        contrato.setPartesEnvolvidas(parteEnvolvidas);
        return contratoMapper.toDTO(gravar(contrato));
    }

    public ContratoDTO buscarPorNumero(Long numero) {
        Contrato contrato = contratoRepository.findContratoByNumeroContrato(numero)
                .orElseThrow(() -> new AttusException(HttpStatus.NOT_FOUND, "Contrato não encontrada para o número informado."));
        return contratoMapper.toDTO(contrato);
    }

    public List<ContratoDTO> buscarPorData(LocalDate dataCriacao) {
        List<Contrato> contratos = contratoRepository.findContratoByDataCriacao(dataCriacao);
        if (contratos.isEmpty()) {
            throw new AttusException(HttpStatus.NOT_FOUND, "Contrato não encontrada para a inscrição federal informada.");
        }
        return contratos.stream().map(contratoMapper::toDTO).collect(Collectors.toList());
    }

    public List<ContratoDTO> buscarPorInscricao(String inscricaoFederal) {
        List<Contrato> contratos = contratoRepository.findByPartesEnvolvidas_InscricaoFederal(inscricaoFederal);
        if (contratos.isEmpty()) {
            throw new AttusException(HttpStatus.NOT_FOUND, "Contrato não encontrada para a inscrição federal informada.");
        }
        return contratos.stream().map(contratoMapper::toDTO).collect(Collectors.toList());
    }

    private Contrato gravar(Contrato contrato) {
        return this.contratoRepository.save(contrato);
    }

    public void atualizar(AtualizarContratoDTO atualizarContratoDTO, Long numeroContrato) {
        Contrato contratoExistente = contratoRepository.findContratoByNumeroContrato(numeroContrato).orElseThrow(() -> new AttusException(HttpStatus.NOT_FOUND, "Contrato não encontrado para o número inserido."));
        Contrato contrato = contratoMapper.updateFromDTO(atualizarContratoDTO, contratoExistente);
        gravar(contrato);
    }

    public void arquivar(Long numeroContrato) {
        Contrato contratoExistente = contratoRepository.findContratoByNumeroContrato(numeroContrato).orElseThrow(() -> new AttusException(HttpStatus.NOT_FOUND, "Contrato não encontrado para o número inserido."));
        contratoExistente.setStatusContrato(StatusContrato.ARQUIVADO);
        gravar(contratoExistente);
    }

}