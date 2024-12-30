package com.kauanmeira.api_contrato.controllers.contrato;

import com.kauanmeira.api_contrato.controllers.ContratoController;
import com.kauanmeira.api_contrato.domain.contrato.ContratoService;
import com.kauanmeira.api_contrato.domain.contrato.StatusContrato;
import com.kauanmeira.api_contrato.domain.parte.ParteEnvolvida;
import com.kauanmeira.api_contrato.domain.parte.ParteEnvolvidaService;
import com.kauanmeira.api_contrato.domain.parte.TipoParte;
import com.kauanmeira.api_contrato.dto.contrato.AtualizarContratoDTO;
import com.kauanmeira.api_contrato.dto.contrato.ContratoDTO;
import com.kauanmeira.api_contrato.dto.parte.ParteEnvolvidaDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class ContratoControllerTest {

    @Mock
    private ContratoService contratoService;

    @Mock
    private ParteEnvolvidaService parteEnvolvidaService;

    @InjectMocks
    private ContratoController contratoController;

    private Validator validator;

    private ContratoDTO contratoDTO;
    private ParteEnvolvidaDTO parteEnvolvidaDTO;
    private ParteEnvolvida parteEnvolvida;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        contratoDTO = new ContratoDTO();
        contratoDTO.setNumeroContrato(123L);
        contratoDTO.setDataCriacao(LocalDate.now());
        contratoDTO.setDescricaoContrato("Contrato de teste");
        contratoDTO.setStatusContrato(StatusContrato.ATIVO);
        contratoDTO.setPartesEnvolvidas(new ArrayList<>());

        parteEnvolvidaDTO = new ParteEnvolvidaDTO();
        parteEnvolvidaDTO.setNomeCompleto("Parte Teste");
        parteEnvolvidaDTO.setTipoParte(TipoParte.CLIENTE);

        parteEnvolvida = new ParteEnvolvida();
        parteEnvolvida.setNomeCompleto("Parte Teste");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        mockMvc = MockMvcBuilders.standaloneSetup(contratoController).build();
    }

    @Test
    void criarContrato() {
        contratoDTO.setPartesEnvolvidas(List.of(parteEnvolvidaDTO));
        when(parteEnvolvidaService.cadastrar(parteEnvolvidaDTO)).thenReturn(parteEnvolvida);
        when(contratoService.cadastrar(any(ContratoDTO.class), anyList())).thenReturn(contratoDTO);

        ContratoDTO resultado = contratoController.criarContrato(contratoDTO);

        assertNotNull(resultado);
        assertEquals(123L, resultado.getNumeroContrato());
        verify(parteEnvolvidaService).cadastrar(any(ParteEnvolvidaDTO.class));
        verify(contratoService).cadastrar(any(ContratoDTO.class), anyList());
    }

    @Test
    void atualizarStatusContrato() {
        when(contratoService.atualizarStatus(123L, StatusContrato.ATIVO)).thenReturn(contratoDTO);

        ResponseEntity<ContratoDTO> resultado = contratoController.atualizarStatus(123L, StatusContrato.ATIVO);

        assertNotNull(resultado);
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(123L, Objects.requireNonNull(resultado.getBody()).getNumeroContrato());

        verify(contratoService).atualizarStatus(123L, StatusContrato.ATIVO);
    }

    @Test
    void atualizarContrato() throws Exception {
        AtualizarContratoDTO atualizarContratoDTO = new AtualizarContratoDTO();
        atualizarContratoDTO.setDescricaoContrato("Novo contrato atualizado");

        mockMvc.perform(MockMvcRequestBuilders.put("/contrato/atualizar/123")
                        .contentType("application/json")
                        .content("{\"descricaoContrato\": \"Novo contrato atualizado\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Contrato atualizado com sucesso!"));

        verify(contratoService).atualizar(any(AtualizarContratoDTO.class), any(Long.class));
    }


    @Test
    void arquivarContratos() throws Exception {
        List<Long> numerosContratos = List.of(123L, 456L);
        mockMvc.perform(MockMvcRequestBuilders.put("/contrato/arquivar")
                        .param("numerosContratos", "123", "456"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Contratos arquivados com sucesso!"));

        verify(contratoService).arquivarUmOuMaisContratos(numerosContratos);
    }

    @Test
    void desarquivarContratos() throws Exception {
        List<Long> numerosContratos = List.of(123L, 456L);
        mockMvc.perform(MockMvcRequestBuilders.put("/contrato/desarquivar")
                        .param("numerosContratos", "123", "456"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Contratos desarquivados com sucesso!"));

        verify(contratoService).desarquivarUmOuMaisContratos(numerosContratos);
    }

    @Test
    void buscarContratoPorNumero() throws Exception {
        when(contratoService.buscarPorNumero(123L)).thenReturn(contratoDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/contrato/123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numeroContrato").value(123L));

        verify(contratoService).buscarPorNumero(123L);
    }

    @Test
    void buscarContratoPorDataCriacao() throws Exception {
        when(contratoService.buscarPorData(LocalDate.now())).thenReturn(List.of(contratoDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/contrato/buscar-por-data")
                        .param("dataCriacao", "2024-12-30"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].numeroContrato").value(123L));

        verify(contratoService).buscarPorData(LocalDate.now());
    }

    @Test
    void buscarContratoPorInscricao() throws Exception {
        when(contratoService.buscarPorInscricao("28.941.462/0001-22")).thenReturn(List.of(contratoDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/contrato/buscar-por-inscricao")
                        .param("inscricao", "28.941.462/0001-22"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].numeroContrato").value(123L));

        verify(contratoService).buscarPorInscricao("28.941.462/0001-22");
    }

    @Test
    void criarContratoComValidacaoErro() {
        ContratoDTO contratoInvalido = new ContratoDTO();
        contratoInvalido.setDataCriacao(null);
        contratoInvalido.setDescricaoContrato("");
        contratoInvalido.setStatusContrato(null);
        contratoInvalido.setPartesEnvolvidas(new ArrayList<>());

        Set<ConstraintViolation<ContratoDTO>> violations = validator.validate(contratoInvalido);

        assertFalse(violations.isEmpty(), "Deveria haver violações de validação.");
        assertEquals(4, violations.size(), "O número de violações deve ser 4.");
    }

}
