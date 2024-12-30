package com.kauanmeira.api_contrato.controllers.parte;

import com.kauanmeira.api_contrato.controllers.ParteEnvolvidaController;
import com.kauanmeira.api_contrato.domain.parte.ParteEnvolvidaService;
import com.kauanmeira.api_contrato.dto.parte.AtualizarParteEnvolvidaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class ParteEnvolvidaControllerTest {

    @Mock
    private ParteEnvolvidaService parteEnvolvidaService;

    @InjectMocks
    private ParteEnvolvidaController parteEnvolvidaController;

    private MockMvc mockMvc;

    private AtualizarParteEnvolvidaDTO atualizarParteEnvolvidaDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        atualizarParteEnvolvidaDTO = new AtualizarParteEnvolvidaDTO();
        atualizarParteEnvolvidaDTO.setNomeCompleto("Parte Atualizada");

        mockMvc = MockMvcBuilders.standaloneSetup(parteEnvolvidaController).build();
    }

    @Test
    void atualizarParteEnvolvida() throws Exception {
        doNothing().when(parteEnvolvidaService).atualizarParteEnvolvida(any(AtualizarParteEnvolvidaDTO.class), any(Long.class));

        mockMvc.perform(MockMvcRequestBuilders.put("/parte-envolvida/atualizar/1")
                        .contentType("application/json")
                        .content("{\"nomeCompleto\": \"Parte Atualizada\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Parte Envolvida atualizada com sucesso!"));

        verify(parteEnvolvidaService).atualizarParteEnvolvida(any(AtualizarParteEnvolvidaDTO.class), any(Long.class));
    }

    @Test
    void atualizarParteEnvolvidaComErro() throws Exception {
        doThrow(new RuntimeException("Erro na atualização")).when(parteEnvolvidaService).atualizarParteEnvolvida(any(AtualizarParteEnvolvidaDTO.class), any(Long.class));

        mockMvc.perform(MockMvcRequestBuilders.put("/parte-envolvida/atualizar/1")
                        .contentType("application/json")
                        .content("{\"nomeCompleto\": \"Parte Atualizada\"}"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().string("Erro na atualização."));

        verify(parteEnvolvidaService).atualizarParteEnvolvida(any(AtualizarParteEnvolvidaDTO.class), any(Long.class));
    }
}