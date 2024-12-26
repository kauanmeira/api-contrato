package com.kauanmeira.api_contrato.services;

import com.kauanmeira.api_contrato.domain.contrato.*;
import com.kauanmeira.api_contrato.domain.parte.ParteEnvolvida;
import com.kauanmeira.api_contrato.domain.parte.ParteEnvolvidaMapper;
import com.kauanmeira.api_contrato.domain.parte.TipoParte;
import com.kauanmeira.api_contrato.dto.contrato.AtualizarContratoDTO;
import com.kauanmeira.api_contrato.dto.contrato.ContratoDTO;
import com.kauanmeira.api_contrato.exceptions.AttusException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
class ContratoServiceTest {
    @InjectMocks
    private ContratoService contratoService;
    @Mock
    private ContratoRepository contratoRepository;
    private ContratoMapper contratoMapper = ContratoMapper.INSTANCE;
    private ParteEnvolvidaMapper parteEnvolvidaMapper = ParteEnvolvidaMapper.INSTANCE;

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
        Assertions.assertNotNull(contratoCadastrado);
    }

    @Test
    void cadastrarContratoComInscricoesDuplicadasTeste() {
        Contrato contratoSalvo = new Contrato();
        ParteEnvolvida parteEnvolvida = partesEnvolvidas.get(0);
        partesEnvolvidas.add(parteEnvolvida);

        Mockito.when(contratoRepository.save(Mockito.any(Contrato.class))).thenReturn(contratoSalvo);
        Assertions.assertThrows(AttusException.class, () -> contratoService.cadastrar(contratoDTO, partesEnvolvidas));
    }

    @Test
    void buscarContratoPorNumeroTeste(){
        Mockito.when(contratoRepository.findContratoByNumeroContrato(1l)).thenReturn(Optional.ofNullable(contrato));
        ContratoDTO contratoFiltrado = contratoService.buscarPorNumero(1l);
        Assertions.assertNotNull(contratoFiltrado);
    }
    @Test
    void buscarContratoPorDataCriacao(){
        Mockito.when(contratoRepository.findContratoByDataCriacao(contrato.getDataCriacao())).thenReturn(List.of(contrato));
        List<ContratoDTO> contratosFiltrados = contratoService.buscarPorData(contrato.getDataCriacao());
        Assertions.assertNotNull(contratosFiltrados);
        Assertions.assertFalse(contratosFiltrados.isEmpty());
    }
    @Test
    void buscarContratoPorDataCriacaoSemResultadoNoResponse(){
        Mockito.when(contratoRepository.findContratoByDataCriacao(LocalDate.of(2024,12,24))).thenReturn(new ArrayList<>());
        Assertions.assertThrows(AttusException.class, () -> contratoService.buscarPorData(LocalDate.of(2024,12,24)));
    }
    @Test
    void buscarContratoPorInscricaoFederal(){
        ParteEnvolvida parteBuscaInscricao = partesEnvolvidas.get(0);
        Mockito.when(contratoRepository.findByPartesEnvolvidas_InscricaoFederal(parteBuscaInscricao.getInscricaoFederal())).thenReturn(List.of(contrato));
        List<ContratoDTO> contratosFiltrados = contratoService.buscarPorInscricao(parteBuscaInscricao.getInscricaoFederal());
        Assertions.assertNotNull(contratosFiltrados);
        Assertions.assertFalse(contratosFiltrados.isEmpty());
    }
    @Test
    void buscarContratoPorInscricaoSemResultadoNoResponse(){
        ParteEnvolvida parteBuscaInscricaoSemResultado = partesEnvolvidas.get(0);
        parteBuscaInscricaoSemResultado.setInscricaoFederal("44859491840");
        Mockito.when(contratoRepository.findByPartesEnvolvidas_InscricaoFederal(parteBuscaInscricaoSemResultado.getInscricaoFederal())).thenReturn(new ArrayList<>());
        Assertions.assertThrows(AttusException.class, () -> contratoService.buscarPorInscricao(parteBuscaInscricaoSemResultado.getInscricaoFederal()));
    }

    @Test
    void atualizarContratoComSucessoTeste(){
        AtualizarContratoDTO atualizarContratoDTO = new AtualizarContratoDTO();

        atualizarContratoDTO.setDescricaoContrato("Descrição alterada");
        atualizarContratoDTO.setStatusContrato(StatusContrato.SUSPENSO);
        atualizarContratoDTO.setDataCriacao(LocalDate.now());
        atualizarContratoDTO.setArquivado(true);

        Mockito.when(contratoRepository.findContratoByNumeroContrato(contrato.getNumeroContrato())).thenReturn(Optional.of(contrato));
        Contrato contratoAtualizado = contratoService.atualizar(atualizarContratoDTO, contrato.getNumeroContrato());

        Mockito.verify(contratoRepository, Mockito.times(1)).findContratoByNumeroContrato(contrato.getNumeroContrato());
        Assertions.assertEquals(atualizarContratoDTO.getDescricaoContrato(),contratoAtualizado.getDescricaoContrato());
        Assertions.assertEquals(atualizarContratoDTO.getDataCriacao(),contratoAtualizado.getDataCriacao());
        Assertions.assertEquals(atualizarContratoDTO.getStatusContrato(),contratoAtualizado.getStatusContrato());
        Assertions.assertEquals(atualizarContratoDTO.getArquivado(),contratoAtualizado.isArquivado());
    }
}
