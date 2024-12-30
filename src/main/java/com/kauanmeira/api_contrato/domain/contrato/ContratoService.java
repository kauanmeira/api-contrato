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
    private static final String MENSAGEM_CONTRATO_NAO_ENCONTRADO_NUMERO = "Contrato não encontrado para o número informado: ";
    private static final String MENSAGEM_CONTRATOS_NAO_ENCONTRADOS_NUMEROS = "Contratos não encontrados para os números informados: ";
    private static final String MENSAGEM_CONTRATO_NAO_ENCONTRADO_DATA = "Contrato não encontrado para o data informada: ";
    private static final String MENSAGEM_CONTRATO_NAO_ENCONTRADO_INSCRICAO = "Contrato não encontrado para o data informada: ";
    private static final String MENSAGEM_CONTRATO_JA_ARQUIVADO = "Os seguintes contratos já estão arquivados: ";
    private static final String MENSAGEM_CONTRATO_JA_DESARQUIVADO = "Os seguintes contratos não estão desarquivados: ";


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
                .orElseThrow(() -> new AttusException(HttpStatus.NOT_FOUND, MENSAGEM_CONTRATO_NAO_ENCONTRADO_NUMERO + numero));
        return contratoMapper.toDTO(contrato);
    }

    public List<ContratoDTO> buscarPorData(LocalDate dataCriacao) {
        List<Contrato> contratos = contratoRepository.findContratoByDataCriacao(dataCriacao);
        if (contratos.isEmpty()) {
            throw new AttusException(HttpStatus.NOT_FOUND, MENSAGEM_CONTRATO_NAO_ENCONTRADO_DATA + dataCriacao);
        }
        return contratos.stream().map(contratoMapper::toDTO).toList();
    }

    public List<ContratoDTO> buscarPorInscricao(String inscricaoFederal) {
        List<Contrato> contratos = contratoRepository.findByPartesEnvolvidas_InscricaoFederal(inscricaoFederal);
        if (contratos.isEmpty()) {
            throw new AttusException(HttpStatus.NOT_FOUND, MENSAGEM_CONTRATO_NAO_ENCONTRADO_INSCRICAO + inscricaoFederal);
        }
        return contratos.stream().map(contratoMapper::toDTO).toList();
    }

    private Contrato gravar(Contrato contrato) {
        try {
            return this.contratoRepository.save(contrato);
        } catch (Exception e) {
            throw new AttusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao salvar Contrato no banco de dados: " + e.getMessage());
        }
    }

    private List<Contrato> gravarContratos(List<Contrato> contratos) {
        try {
            return this.contratoRepository.saveAll(contratos);
        } catch (Exception e) {
            throw new AttusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao salvar Contratos no banco de dados: " + e.getMessage());
        }
    }

    public Contrato atualizar(AtualizarContratoDTO atualizarContratoDTO, Long numeroContrato) {
        Contrato contratoExistente = contratoRepository.findContratoByNumeroContrato(numeroContrato)
                .orElseThrow(() -> new AttusException(HttpStatus.NOT_FOUND, MENSAGEM_CONTRATO_NAO_ENCONTRADO_NUMERO + numeroContrato));
        Contrato contrato = contratoMapper.updateFromDTO(atualizarContratoDTO, contratoExistente);
        return gravar(contrato);
    }

    public ContratoDTO atualizarStatus(Long numeroContrato, StatusContrato statusContrato) {
        Contrato contratoExistente = contratoRepository.findContratoByNumeroContrato(numeroContrato)
                .orElseThrow(() -> new AttusException(HttpStatus.NOT_FOUND, MENSAGEM_CONTRATO_NAO_ENCONTRADO_NUMERO + numeroContrato));

        contratoExistente.setStatusContrato(statusContrato);
        Contrato contratoAtualizado = gravar(contratoExistente);
        return contratoMapper.toDTO(contratoAtualizado);
    }

    public void arquivarUmOuMaisContratos(List<Long> numerosContratos) {
        if (numerosContratos == null || numerosContratos.isEmpty()) {
            throw new AttusException(HttpStatus.BAD_REQUEST, MENSAGEM_CONTRATOS_NAO_ENCONTRADOS_NUMEROS + numerosContratos);
        }

        List<Contrato> contratosExistentes = contratoRepository.findAllByNumeroContratoIn(numerosContratos);

        verificarContratosJaArquivados(contratosExistentes);
        verificarContratosNaoEncontrados(contratosExistentes, numerosContratos);

        contratosExistentes.forEach(contrato -> contrato.setArquivado(true));
        gravarContratos(contratosExistentes);
    }

    private void verificarContratosJaArquivados(List<Contrato> contratosExistentes) {
        List<Long> jaArquivados = contratosExistentes.stream()
                .filter(Contrato::isArquivado)
                .map(Contrato::getNumeroContrato)
                .toList();

        if (!jaArquivados.isEmpty()) {
            throw new AttusException(HttpStatus.BAD_REQUEST, MENSAGEM_CONTRATO_JA_ARQUIVADO + jaArquivados);
        }
    }


    public void desarquivarUmOuMaisContratos(List<Long> numerosContratos) {
        if (numerosContratos == null || numerosContratos.isEmpty()) {
            throw new AttusException(HttpStatus.BAD_REQUEST, MENSAGEM_CONTRATOS_NAO_ENCONTRADOS_NUMEROS + numerosContratos);
        }

        List<Contrato> contratosExistentes = contratoRepository.findAllByNumeroContratoIn(numerosContratos);

        verificarContratosNaoArquivados(contratosExistentes);
        verificarContratosNaoEncontrados(contratosExistentes, numerosContratos);

        contratosExistentes.forEach(contrato -> contrato.setArquivado(false));
        gravarContratos(contratosExistentes);
    }

    private void verificarContratosNaoArquivados(List<Contrato> contratosExistentes) {
        List<Long> naoArquivados = contratosExistentes.stream()
                .filter(contrato -> !contrato.isArquivado())
                .map(Contrato::getNumeroContrato)
                .toList();

        if (!naoArquivados.isEmpty()) {
            throw new AttusException(HttpStatus.BAD_REQUEST, MENSAGEM_CONTRATO_JA_DESARQUIVADO + naoArquivados);
        }
    }

    private void verificarContratosNaoEncontrados(List<Contrato> contratosExistentes, List<Long> numerosContratos) {
        if (contratosExistentes.size() != numerosContratos.size()) {
            List<Long> contratosNaoEncontrados = numerosContratos.stream()
                    .filter(numero -> contratosExistentes.stream()
                            .noneMatch(contrato -> contrato.getNumeroContrato().equals(numero)))
                    .toList();
            throw new AttusException(HttpStatus.NOT_FOUND,
                    MENSAGEM_CONTRATOS_NAO_ENCONTRADOS_NUMEROS + contratosNaoEncontrados);
        }
    }


}