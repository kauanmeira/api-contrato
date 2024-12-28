package com.kauanmeira.api_contrato.services;

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
    @Mock
    private ContratoMapper contratoMapper = ContratoMapper.INSTANCE;

    private ContratoDTO contratoDTO;
    private Contrato contrato;
    private List<ParteEnvolvida> partesEnvolvidas;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        ParteEnvolvida parte1 = new ParteEnvolvida();
        parte1.setInscricaoFederal("46149592880");
        parte1.setNomeCompleto("Kauan Gabriel Paiva Meira");
        parte1.setDataNascimento(LocalDate.of(2002, 04, 19));
        parte1.setTipoParte(TipoParte.CLIENTE);
        parte1.setEmail("kauan.paiva.meira@gmail.com");
        parte1.setTelefone("17988214036");

        ParteEnvolvida parte2 = new ParteEnvolvida();
        parte2.setInscricaoFederal("31620202808");
        parte2.setNomeCompleto("Valdinei Ferreira Alves");
        parte2.setDataNascimento(LocalDate.of(1982, 03, 20));
        parte2.setTipoParte(TipoParte.ADVOGADO);
        parte2.setEmail("valdinei.ferreira@gmail.com");
        parte2.setTelefone("17988214036");

        contratoDTO = new ContratoDTO();
        contratoDTO.setNumeroContrato(1l);
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
        Mockito.when(contratoRepository.findContratoByDataCriacao(LocalDate.of(2024, 12, 24))).thenReturn(new ArrayList<>());

        AttusException exception = assertThrows(AttusException.class,
                () -> contratoService.buscarPorData(LocalDate.of(2024, 12, 24)));

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

        Mockito.when(contratoRepository.findByPartesEnvolvidas_InscricaoFederal(parteBuscaInscricaoSemResultado.getInscricaoFederal())).thenReturn(new ArrayList<>());

        AttusException exception = assertThrows(AttusException.class, () ->
                contratoService.buscarPorInscricao(parteBuscaInscricaoSemResultado.getInscricaoFederal()));

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
    void arquivarContratoComSucesso() {
        Long numeroContrato = contrato.getNumeroContrato();
        contrato.setArquivado(false);

        Mockito.when(contratoRepository.findContratoByNumeroContrato(numeroContrato))
                .thenReturn(Optional.of(contrato));
        Mockito.when(contratoRepository.save(Mockito.any(Contrato.class)))
                .thenReturn(contrato);

        contratoService.arquivar(numeroContrato);

        Mockito.verify(contratoRepository, Mockito.times(1))
                .findContratoByNumeroContrato(numeroContrato);
        Mockito.verify(contratoRepository, Mockito.times(1))
                .save(Mockito.any(Contrato.class));

        assertTrue(contrato.isArquivado(), "O contrato deveria estar arquivado.");
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

        AttusException exception = assertThrows(AttusException.class, () -> {
            contratoService.atualizarStatus(numeroContratoInexistente, StatusContrato.ENCERRADO);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getCode());
        assertEquals("Contrato não encontrado para o número inserido.", exception.getMessage());
    }

    @Test
    void arquivarContratoNaoEncontradoLancaExcecao() {
        Long numeroContrato = 10l;

        Mockito.when(contratoRepository.findContratoByNumeroContrato(numeroContrato))
                .thenReturn(Optional.empty());

        AttusException exception = assertThrows(AttusException.class, () -> {
            contratoService.arquivar(numeroContrato);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getCode(),
                "O status da exceção deveria ser NOT_FOUND.");
        assertEquals("Contrato não encontrado para o número inserido.", exception.getMessage(),
                "A mensagem da exceção deveria corresponder ao esperado.");

        Mockito.verify(contratoRepository, Mockito.times(1))
                .findContratoByNumeroContrato(numeroContrato);
        Mockito.verify(contratoRepository, Mockito.never())
                .save(Mockito.any(Contrato.class));
    }

    @Test
    void desarquivarContratoComSucesso() {
        Long numeroContrato = contrato.getNumeroContrato();
        contrato.setArquivado(true);

        Mockito.when(contratoRepository.findContratoByNumeroContrato(numeroContrato))
                .thenReturn(Optional.of(contrato));
        Mockito.when(contratoRepository.save(Mockito.any(Contrato.class)))
                .thenReturn(contrato);

        contratoService.desarquivar(numeroContrato);

        Mockito.verify(contratoRepository, Mockito.times(1))
                .findContratoByNumeroContrato(numeroContrato);
        Mockito.verify(contratoRepository, Mockito.times(1))
                .save(Mockito.any(Contrato.class));

        assertFalse(contrato.isArquivado(), "O contrato deveria estar arquivado.");
    }

    @Test
    void desarquivarContratoNaoEncontradoLancaExcecao() {
        Long numeroContrato = 10l;

        Mockito.when(contratoRepository.findContratoByNumeroContrato(numeroContrato))
                .thenReturn(Optional.empty());

        AttusException exception = assertThrows(AttusException.class, () -> {
            contratoService.desarquivar(numeroContrato);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getCode(),
                "O status da exceção deveria ser NOT_FOUND.");
        assertEquals("Contrato não encontrado para o número inserido.", exception.getMessage(),
                "A mensagem da exceção deveria corresponder ao esperado.");

        Mockito.verify(contratoRepository, Mockito.times(1))
                .findContratoByNumeroContrato(numeroContrato);
        Mockito.verify(contratoRepository, Mockito.never())
                .save(Mockito.any(Contrato.class));
    }

}
