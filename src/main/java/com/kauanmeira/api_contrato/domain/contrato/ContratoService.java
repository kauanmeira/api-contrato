package com.kauanmeira.api_contrato.domain.contrato;

import com.kauanmeira.api_contrato.domain.parte.ParteEnvolvida;
import com.kauanmeira.api_contrato.dto.contrato.AtualizarContratoDTO;
import com.kauanmeira.api_contrato.dto.contrato.ContratoDTO;
import com.kauanmeira.api_contrato.exceptions.AttusException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContratoService {
    private final ContratoRepository contratoRepository;
    private static final ContratoMapper contratoMapper = ContratoMapper.INSTANCE;
    private static final String MENSAGEM_CONTRATO_NAO_ENCONTRADO = "Contrato não encontrado para o número inserido.";


    public ContratoDTO cadastrar(ContratoDTO contratoDTO, List<ParteEnvolvida> partesEnvolvidas) {
        validarPartesEnvolvidas(partesEnvolvidas);
        Contrato contrato = contratoMapper.toObject(contratoDTO);
        contrato.setPartesEnvolvidas(partesEnvolvidas);
        return contratoMapper.toDTO(gravar(contrato));
    }

    private void validarPartesEnvolvidas(List<ParteEnvolvida> parteEnvolvidas) {
        List<String> inscricoesDuplicadas = parteEnvolvidas.stream()
                .collect(Collectors.groupingBy(ParteEnvolvida::getInscricaoFederal, Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .toList();

        if (!inscricoesDuplicadas.isEmpty()) {
            throw new AttusException(
                    HttpStatus.BAD_REQUEST,
                    "Existem partes envolvidas com inscrições federais duplicadas: " + String.join(", ", inscricoesDuplicadas)
            );
        }
    }

    public ContratoDTO buscarPorNumero(Long numero) {
        Contrato contrato = contratoRepository.findContratoByNumeroContrato(numero)
                .orElseThrow(() -> new AttusException(HttpStatus.NOT_FOUND, "Contrato não encontrada para o número informado."));
        return contratoMapper.toDTO(contrato);
    }

    public List<ContratoDTO> buscarPorData(LocalDate dataCriacao) {
        List<Contrato> contratos = contratoRepository.findContratoByDataCriacao(dataCriacao);
        if (contratos.isEmpty()) {
            throw new AttusException(HttpStatus.NOT_FOUND, "Contrato não encontrada para a data informada.");
        }
        return contratos.stream().map(contratoMapper::toDTO).toList();
    }

    public List<ContratoDTO> buscarPorInscricao(String inscricaoFederal) {
        List<Contrato> contratos = contratoRepository.findByPartesEnvolvidas_InscricaoFederal(inscricaoFederal);
        if (contratos.isEmpty()) {
            throw new AttusException(HttpStatus.NOT_FOUND, "Contrato não encontrada para a inscrição federal informada.");
        }
        return contratos.stream().map(contratoMapper::toDTO).toList();
    }

    private Contrato gravar(Contrato contrato) {
        return this.contratoRepository.save(contrato);
    }

    public Contrato atualizar(AtualizarContratoDTO atualizarContratoDTO, Long numeroContrato) {
        Contrato contratoExistente = contratoRepository.findContratoByNumeroContrato(numeroContrato)
                .orElseThrow(() -> new AttusException(HttpStatus.NOT_FOUND, MENSAGEM_CONTRATO_NAO_ENCONTRADO));
        Contrato contrato = contratoMapper.updateFromDTO(atualizarContratoDTO, contratoExistente);
        return gravar(contrato);
    }

    public ContratoDTO atualizarStatus(Long numeroContrato, StatusContrato statusContrato) {
        Contrato contratoExistente = contratoRepository.findContratoByNumeroContrato(numeroContrato)
                .orElseThrow(() -> new AttusException(HttpStatus.NOT_FOUND, MENSAGEM_CONTRATO_NAO_ENCONTRADO));

        contratoExistente.setStatusContrato(statusContrato);
        Contrato contratoAtualizado = gravar(contratoExistente);
        return contratoMapper.toDTO(contratoAtualizado);
    }

    public void arquivar(Long numeroContrato) {
        Contrato contratoExistente = contratoRepository.findContratoByNumeroContrato(numeroContrato)
                .orElseThrow(() -> new AttusException(HttpStatus.NOT_FOUND, MENSAGEM_CONTRATO_NAO_ENCONTRADO));
        contratoExistente.setArquivado(true);
        gravar(contratoExistente);
    }

    public void desarquivar(Long numeroContrato) {
        Contrato contratoExistente = contratoRepository.findContratoByNumeroContrato(numeroContrato)
                .orElseThrow(() -> new AttusException(HttpStatus.NOT_FOUND, MENSAGEM_CONTRATO_NAO_ENCONTRADO));
        contratoExistente.setArquivado(false);
        gravar(contratoExistente);
    }

}