package com.kauanmeira.api_contrato.services.contrato;

import com.kauanmeira.api_contrato.domain.contrato.*;
import com.kauanmeira.api_contrato.domain.parte.ParteEnvolvida;
import com.kauanmeira.api_contrato.domain.parte.TipoParte;
import com.kauanmeira.api_contrato.dto.contrato.AtualizarContratoDTO;
import com.kauanmeira.api_contrato.dto.contrato.ContratoDTO;
import com.kauanmeira.api_contrato.exceptions.AttusException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class ContratoServiceTest {
    @InjectMocks
    private ContratoService contratoService;
    @Mock
    private ContratoRepository contratoRepository;
    private final ContratoMapper contratoMapper = ContratoMapper.INSTANCE;

    private ContratoDTO contratoDTO;
    private Contrato contrato;
    private List<ParteEnvolvida> partesEnvolvidas;
    private static final String MENSAGEM_CONTRATO_NAO_ENCONTRADO_NUMERO = "Contrato não encontrado para o número informado: ";
    private static final String MENSAGEM_CONTRATOS_NAO_ENCONTRADOS_NUMEROS = "Contratos não encontrados para os números informados: ";
    private static final String MENSAGEM_CONTRATO_JA_ARQUIVADO = "Os seguintes contratos já estão arquivados: ";
    private static final String MENSAGEM_CONTRATO_JA_DESARQUIVADO = "Os seguintes contratos não estão desarquivados: ";
    private static final String MENSAGEM_BAD_REQUEST = "O status da exceção deveria ser BAD_REQUEST.";
    private static final String MENSAGEM_NOT_FOUND = "O status da exceção deveria ser NOT_FOUND.";

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        ParteEnvolvida parte1 = new ParteEnvolvida();
        parte1.setInscricaoFederal("46149592880");
        parte1.setNomeCompleto("Kauan Gabriel Paiva Meira");
        parte1.setDataNascimento(LocalDate.of(2002, 4, 19));
        parte1.setTipoParte(TipoParte.CLIENTE);
        parte1.setEmail("kauan.paiva.meira@gmail.com");
        parte1.setTelefone("17988214036");

        ParteEnvolvida parte2 = new ParteEnvolvida();
        parte2.setInscricaoFederal("31620202808");
        parte2.setNomeCompleto("Valdinei Ferreira Alves");
        parte2.setDataNascimento(LocalDate.of(1982, 3, 20));
        parte2.setTipoParte(TipoParte.ADVOGADO);
        parte2.setEmail("valdinei.ferreira@gmail.com");
        parte2.setTelefone("17988214036");

        contratoDTO = new ContratoDTO();
        contratoDTO.setNumeroContrato(1L);
        contratoDTO.setDataCriacao(LocalDate.now());
        contratoDTO.setDescricaoContrato("o presente CONTRATO DE PRESTAÇÃO DE SERVIÇOS, que reger-se-á mediante as cláusulas e condições adiante estipuladas. CLÁUSULA PRIMEIRA - DO OBJETO.");
        contratoDTO.setStatusContrato(StatusContrato.ATIVO);

        partesEnvolvidas = new ArrayList<>();
        partesEnvolvidas.add(parte1);
        partesEnvolvidas.add(parte2);

        contratoDTO.setPartesEnvolvidas(new ArrayList<>());
        contrato = contratoMapper.toObject(contratoDTO);
    }

    @Test
    void cadastrarContratoComSucessoTeste() {
        Contrato contratoSalvo = new Contrato();
        Mockito.when(contratoRepository.save(Mockito.any(Contrato.class))).thenReturn(contratoSalvo);
        ContratoDTO contratoCadastrado = contratoService.cadastrar(contratoDTO, partesEnvolvidas);
        assertNotNull(contratoCadastrado);
    }

    @Test
    void cadastrarContratoComInscricoesDuplicadasTeste() {
        Contrato contratoSalvo = new Contrato();
        ParteEnvolvida parteEnvolvida = partesEnvolvidas.get(0);
        partesEnvolvidas.add(parteEnvolvida);

        Mockito.when(contratoRepository.save(Mockito.any(Contrato.class))).thenReturn(contratoSalvo);
        assertThrows(AttusException.class, () -> contratoService.cadastrar(contratoDTO, partesEnvolvidas));
    }

    @Test
    void buscarContratoPorNumeroTeste() {
        Mockito.when(contratoRepository.findContratoByNumeroContrato(contrato.getNumeroContrato())).thenReturn(Optional.ofNullable(contrato));
        ContratoDTO contratoFiltrado = contratoService.buscarPorNumero(contrato.getNumeroContrato());
        assertNotNull(contratoFiltrado);
    }

    @Test
    void buscarContratoPorDataCriacao() {
        Mockito.when(contratoRepository.findContratoByDataCriacao(contrato.getDataCriacao())).thenReturn(List.of(contrato));
        List<ContratoDTO> contratosFiltrados = contratoService.buscarPorData(contrato.getDataCriacao());
        assertNotNull(contratosFiltrados);
        assertFalse(contratosFiltrados.isEmpty());
    }

    @Test
    void buscarContratoPorDataCriacaoSemResultadoNoResponse() {
        LocalDate dataCriacao = LocalDate.of(2024, 12, 24);
        Mockito.when(contratoRepository.findContratoByDataCriacao(dataCriacao)).thenReturn(new ArrayList<>());

        AttusException exception = assertThrows(AttusException.class,
                () -> contratoService.buscarPorData(dataCriacao)
        );

        assertNotNull(exception);
    }

    @Test
    void buscarContratoPorInscricaoFederal() {
        ParteEnvolvida parteBuscaInscricao = partesEnvolvidas.get(0);
        Mockito.when(contratoRepository.findByPartesEnvolvidas_InscricaoFederal(parteBuscaInscricao.getInscricaoFederal())).thenReturn(List.of(contrato));
        List<ContratoDTO> contratosFiltrados = contratoService.buscarPorInscricao(parteBuscaInscricao.getInscricaoFederal());
        assertNotNull(contratosFiltrados);
        assertFalse(contratosFiltrados.isEmpty());
    }

    @Test
    void buscarContratoPorInscricaoSemResultadoNoResponse() {
        ParteEnvolvida parteBuscaInscricaoSemResultado = partesEnvolvidas.get(0);
        parteBuscaInscricaoSemResultado.setInscricaoFederal("44859491840");

        Mockito.when(contratoRepository.findByPartesEnvolvidas_InscricaoFederal(parteBuscaInscricaoSemResultado.getInscricaoFederal()))
                .thenReturn(new ArrayList<>());

        AttusException exception = assertThrows(AttusException.class, () -> contratoService.buscarPorInscricao(parteBuscaInscricaoSemResultado.getInscricaoFederal()));

        assertNotNull(exception);
    }


    @Test
    void atualizarContratoComSucessoTeste() {
        AtualizarContratoDTO atualizarContratoDTO = new AtualizarContratoDTO();
        atualizarContratoDTO.setDescricaoContrato("Descrição alterada");
        atualizarContratoDTO.setStatusContrato(StatusContrato.SUSPENSO);
        atualizarContratoDTO.setDataCriacao(LocalDate.now());
        atualizarContratoDTO.setArquivado(true);
        Contrato contratoAtualizado = contratoMapper.updateFromDTO(atualizarContratoDTO, contrato);

        Mockito.when(contratoRepository.findContratoByNumeroContrato(contrato.getNumeroContrato())).thenReturn(Optional.of(contrato));
        Mockito.when(contratoRepository.save(Mockito.any(Contrato.class))).thenReturn(contratoAtualizado);
        contratoAtualizado = contratoService.atualizar(atualizarContratoDTO, contrato.getNumeroContrato());

        Mockito.verify(contratoRepository, Mockito.times(1)).findContratoByNumeroContrato(contrato.getNumeroContrato());
        assertEquals(atualizarContratoDTO.getDescricaoContrato(), contratoAtualizado.getDescricaoContrato());
        assertEquals(atualizarContratoDTO.getDataCriacao(), contratoAtualizado.getDataCriacao());
        assertEquals(atualizarContratoDTO.getStatusContrato(), contratoAtualizado.getStatusContrato());
        assertEquals(atualizarContratoDTO.getArquivado(), contratoAtualizado.isArquivado());
    }

    @Test
    void atualizarStatusComSucesso() {
        Long numeroContrato = contrato.getNumeroContrato();
        StatusContrato novoStatus = StatusContrato.SUSPENSO;
        contrato.setStatusContrato(StatusContrato.ATIVO);

        Mockito.when(contratoRepository.findContratoByNumeroContrato(numeroContrato))
                .thenReturn(Optional.of(contrato));
        Mockito.when(contratoRepository.save(Mockito.any(Contrato.class)))
                .thenReturn(contrato);

        ContratoDTO contratoAtualizadoDTO = contratoService.atualizarStatus(numeroContrato, novoStatus);

        assertEquals(novoStatus, contratoAtualizadoDTO.getStatusContrato(), "O status do contrato deve ser atualizado.");

        Mockito.verify(contratoRepository, Mockito.times(1)).save(Mockito.any(Contrato.class));
        Mockito.verify(contratoRepository, Mockito.times(1)).findContratoByNumeroContrato(numeroContrato);
    }

    @Test
    void atualizarStatusContratoNaoEncontrado() {
        Long numeroContratoInexistente = 999L;

        Mockito.when(contratoRepository.findContratoByNumeroContrato(numeroContratoInexistente))
                .thenReturn(Optional.empty());

        AttusException exception = assertThrows(AttusException.class, () ->
                contratoService.atualizarStatus(numeroContratoInexistente, StatusContrato.ENCERRADO)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getCode());
        assertEquals(MENSAGEM_CONTRATO_NAO_ENCONTRADO_NUMERO + numeroContratoInexistente, exception.getMessage());
    }

    @Test
    void arquivarContratosComSucesso() {
        List<Long> numerosContratos = List.of(1L, 2L);

        // Criação de contratos com campo 'arquivado' como false
        List<Contrato> contratos = numerosContratos.stream()
                .map(numero -> {
                    Contrato c = new Contrato();
                    c.setNumeroContrato(numero);
                    c.setArquivado(false);
                    return c;
                }).toList();

        Mockito.when(contratoRepository.findAllByNumeroContratoIn(numerosContratos))
                .thenReturn(contratos);

        Mockito.when(contratoRepository.saveAll(Mockito.anyList()))
                .thenAnswer(invocation -> {
                    List<Contrato> savedContratos = invocation.getArgument(0);
                    savedContratos.forEach(c -> c.setArquivado(true));
                    return savedContratos;
                });

        contratoService.arquivarUmOuMaisContratos(numerosContratos);

        Mockito.verify(contratoRepository, Mockito.times(1))
                .findAllByNumeroContratoIn(numerosContratos);
        Mockito.verify(contratoRepository, Mockito.times(1))
                .saveAll(Mockito.anyList());

        for (Contrato contratoItem : contratos) {
            assertTrue(contratoItem.isArquivado(), "O contrato deveria estar arquivado.");
        }
    }


    @Test
    void arquivarContratosJaArquivadosLancaExcecao() {
        List<Long> numerosContratos = List.of(10L, 12L);

        List<Contrato> contratos = numerosContratos.stream()
                .map(numero -> {
                    Contrato c = new Contrato();
                    c.setNumeroContrato(numero);
                    c.setArquivado(true);
                    return c;
                })
                .toList();

        Mockito.when(contratoRepository.findAllByNumeroContratoIn(numerosContratos))
                .thenReturn(contratos);

        AttusException exception = assertThrows(AttusException.class, () -> contratoService.arquivarUmOuMaisContratos(numerosContratos));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getCode(), MENSAGEM_BAD_REQUEST);
        assertTrue(exception.getMessage().contains(MENSAGEM_CONTRATO_JA_ARQUIVADO + numerosContratos),
                "A mensagem da exceção deveria indicar que os contratos já estão arquivados.");

        Mockito.verify(contratoRepository, Mockito.times(1))
                .findAllByNumeroContratoIn(numerosContratos);
        Mockito.verify(contratoRepository, Mockito.never())
                .saveAll(Mockito.anyList());
    }

    @Test
    void arquivarContratosNaoEncontradoLancaExcecao() {
        List<Long> numerosContratos = List.of(100L, 200L);

        Mockito.when(contratoRepository.findAllByNumeroContratoIn(numerosContratos))
                .thenReturn(new ArrayList<>());

        AttusException exception = assertThrows(AttusException.class, () -> contratoService.arquivarUmOuMaisContratos(numerosContratos));

        assertEquals(HttpStatus.NOT_FOUND, exception.getCode(),
                MENSAGEM_NOT_FOUND);
        assertTrue(exception.getMessage().contains(MENSAGEM_CONTRATOS_NAO_ENCONTRADOS_NUMEROS + numerosContratos),
                "A mensagem da exceção deveria indicar que os contratos não foram encontrados.");

        Mockito.verify(contratoRepository, Mockito.times(1))
                .findAllByNumeroContratoIn(numerosContratos);
        Mockito.verify(contratoRepository, Mockito.never())
                .saveAll(Mockito.anyList());
    }

    @Test
    void arquivarContratosComListaNulaOuVaziaLancaExcecao() {
        AttusException exceptionNulo = assertThrows(AttusException.class, () -> contratoService.arquivarUmOuMaisContratos(null));

        assertEquals(HttpStatus.BAD_REQUEST, exceptionNulo.getCode(),
                MENSAGEM_BAD_REQUEST);
        assertTrue(exceptionNulo.getMessage().contains(MENSAGEM_CONTRATOS_NAO_ENCONTRADOS_NUMEROS),
                "A mensagem da exceção deveria indicar que os contratos não foram encontrados para a lista nula.");

        List<Long> numerosContratosVazia = List.of();

        AttusException exceptionVazia = assertThrows(AttusException.class, () -> contratoService.arquivarUmOuMaisContratos(numerosContratosVazia));

        assertEquals(HttpStatus.BAD_REQUEST, exceptionVazia.getCode(),
                MENSAGEM_BAD_REQUEST);
        assertTrue(exceptionVazia.getMessage().contains(MENSAGEM_CONTRATOS_NAO_ENCONTRADOS_NUMEROS),
                "A mensagem da exceção deveria indicar que os contratos não foram encontrados para a lista vazia.");
    }

    @Test
    void desarquivarContratosComSucesso() {
        List<Long> numerosContratos = List.of(1L, 2L);

        List<Contrato> contratos = numerosContratos.stream()
                .map(numero -> {
                    Contrato c = new Contrato();
                    c.setNumeroContrato(numero);
                    c.setArquivado(true);
                    return c;
                }).toList();


        Mockito.when(contratoRepository.findAllByNumeroContratoIn(numerosContratos))
                .thenReturn(contratos);
        Mockito.when(contratoRepository.saveAll(Mockito.anyList()))
                .thenReturn(contratos);

        contratoService.desarquivarUmOuMaisContratos(numerosContratos);

        Mockito.verify(contratoRepository, Mockito.times(1))
                .findAllByNumeroContratoIn(numerosContratos);
        Mockito.verify(contratoRepository, Mockito.times(1))
                .saveAll(Mockito.anyList());

        contratos.forEach(contratoItem -> assertFalse(contrato.isArquivado(),
                "Os contratos deveriam estar desarquivados."));
    }

    @Test
    void desarquivarContratosJaDesarquivadosLancaExcecao() {
        List<Long> numerosContratos = List.of(1L, 2L);

        List<Contrato> contratos = numerosContratos.stream()
                .map(numero -> {
                    Contrato c = new Contrato();
                    c.setNumeroContrato(numero);
                    c.setArquivado(false);
                    return c;
                }).toList();

        Mockito.when(contratoRepository.findAllByNumeroContratoIn(numerosContratos))
                .thenReturn(contratos);

        AttusException exception = assertThrows(AttusException.class, () -> contratoService.desarquivarUmOuMaisContratos(numerosContratos));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getCode(),
                MENSAGEM_BAD_REQUEST);

        assertTrue(exception.getMessage().contains(MENSAGEM_CONTRATO_JA_DESARQUIVADO + numerosContratos),
                "A mensagem da exceção deveria indicar que os contratos já estão desarquivados.");

        Mockito.verify(contratoRepository, Mockito.times(1))
                .findAllByNumeroContratoIn(numerosContratos);
        Mockito.verify(contratoRepository, Mockito.never())
                .saveAll(Mockito.anyList());
    }

    @Test
    void desarquivarContratosNaoEncontradoLancaExcecao() {
        List<Long> numerosContratos = List.of(300L, 400L);

        Mockito.when(contratoRepository.findAllByNumeroContratoIn(numerosContratos))
                .thenReturn(new ArrayList<>());

        AttusException exception = assertThrows(AttusException.class, () -> contratoService.desarquivarUmOuMaisContratos(numerosContratos));

        assertEquals(HttpStatus.NOT_FOUND, exception.getCode(),
                MENSAGEM_NOT_FOUND);
        assertTrue(exception.getMessage().contains(MENSAGEM_CONTRATOS_NAO_ENCONTRADOS_NUMEROS),
                "A mensagem da exceção deveria indicar que os contratos não foram encontrados.");

        Mockito.verify(contratoRepository, Mockito.times(1))
                .findAllByNumeroContratoIn(numerosContratos);
        Mockito.verify(contratoRepository, Mockito.never())
                .saveAll(Mockito.anyList());
    }

    @Test
    void desarquivarContratosComListaNulaOuVaziaLancaExcecao() {
        AttusException exceptionNulo = assertThrows(AttusException.class, () -> contratoService.desarquivarUmOuMaisContratos(null));

        assertEquals(HttpStatus.BAD_REQUEST, exceptionNulo.getCode(),
                MENSAGEM_BAD_REQUEST);
        assertTrue(exceptionNulo.getMessage().contains(MENSAGEM_CONTRATOS_NAO_ENCONTRADOS_NUMEROS),
                "A mensagem da exceção deveria indicar que os contratos não foram encontrados para a lista nula.");

        List<Long> numerosContratosVazia = List.of();

        AttusException exceptionVazia = assertThrows(AttusException.class, () -> contratoService.desarquivarUmOuMaisContratos(numerosContratosVazia));

        assertEquals(HttpStatus.BAD_REQUEST, exceptionVazia.getCode(),
                MENSAGEM_BAD_REQUEST);
        assertTrue(exceptionVazia.getMessage().contains(MENSAGEM_CONTRATOS_NAO_ENCONTRADOS_NUMEROS),
                "A mensagem da exceção deveria indicar que os contratos não foram encontrados para a lista vazia.");
    }
}
