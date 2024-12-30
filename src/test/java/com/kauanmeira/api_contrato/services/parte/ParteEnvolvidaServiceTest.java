package com.kauanmeira.api_contrato.services.parte;

import com.kauanmeira.api_contrato.domain.parte.ParteEnvolvida;
import com.kauanmeira.api_contrato.domain.parte.ParteEnvolvidaRepository;
import com.kauanmeira.api_contrato.domain.parte.ParteEnvolvidaService;
import com.kauanmeira.api_contrato.domain.parte.TipoParte;
import com.kauanmeira.api_contrato.domain.utils.InscricaoFederalValidator;
import com.kauanmeira.api_contrato.dto.parte.AtualizarParteEnvolvidaDTO;
import com.kauanmeira.api_contrato.dto.parte.ParteEnvolvidaDTO;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class ParteEnvolvidaServiceTest {
    @InjectMocks
    private ParteEnvolvidaService parteEnvolvidaService;
    @Mock
    private ParteEnvolvidaRepository parteEnvolvidaRepository;

    private ParteEnvolvidaDTO parteEnvolvidaDTO;
    private ParteEnvolvida parteEnvolvida;
    private AtualizarParteEnvolvidaDTO atualizarParteEnvolvidaDTO;

    private static final String MENSAGEM_PARTE_NAO_ENCONTRADA = "ParteEnvolvida não encontrado para o Id inserido.";
    private static final String MENSAGEM_CPF_INVALIDO = "CPF inválido: ";
    private static final String MENSAGEM_CNPJ_INVALIDO = "CNPJ inválido: ";
    private static final String MENSAGEM_INSCRICAO_INVALIDA = "Inscrição Federal inválida. Deve ter 11 ou 14 dígitos: ";

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        parteEnvolvidaDTO = new ParteEnvolvidaDTO();
        parteEnvolvidaDTO.setNomeCompleto("Kauan Gabriel Paiva Meira");
        parteEnvolvidaDTO.setTipoParte(TipoParte.CLIENTE);
        parteEnvolvidaDTO.setInscricaoFederal("46149592880");
        parteEnvolvidaDTO.setDataNascimento(LocalDate.of(2002, 4, 19));
        parteEnvolvidaDTO.setEmail("kauan.paiva.meira@gmail.com");
        parteEnvolvidaDTO.setTelefone("17988214036");

        parteEnvolvida = new ParteEnvolvida();
        parteEnvolvida.setNomeCompleto(parteEnvolvidaDTO.getNomeCompleto());
        parteEnvolvida.setTipoParte(parteEnvolvidaDTO.getTipoParte());
        parteEnvolvida.setInscricaoFederal(parteEnvolvidaDTO.getInscricaoFederal());
        parteEnvolvida.setDataNascimento(parteEnvolvidaDTO.getDataNascimento());
        parteEnvolvida.setEmail(parteEnvolvidaDTO.getEmail());
        parteEnvolvida.setTelefone(parteEnvolvidaDTO.getTelefone());

        atualizarParteEnvolvidaDTO = new AtualizarParteEnvolvidaDTO();
        atualizarParteEnvolvidaDTO.setNomeCompleto("Nome Atualizado");
        atualizarParteEnvolvidaDTO.setEmail("novo.email@gmail.com");
        atualizarParteEnvolvidaDTO.setTelefone("17999887766");
    }

    @Test
    void cadastrarParteComSucesso() {
        ParteEnvolvida parteSalva = new ParteEnvolvida();
        Mockito.when(parteEnvolvidaRepository.save(Mockito.any(ParteEnvolvida.class))).thenReturn(parteSalva);

        ParteEnvolvida parteCadastrada = parteEnvolvidaService.cadastrar(parteEnvolvidaDTO);
        assertNotNull(parteCadastrada);
    }

    @Test
    void cadastrarParteComCpfInvalido() {
        parteEnvolvidaDTO.setInscricaoFederal("12345678900");

        AttusException exception = assertThrows(AttusException.class, () -> parteEnvolvidaService.cadastrar(parteEnvolvidaDTO));
        assertEquals(MENSAGEM_CPF_INVALIDO + parteEnvolvidaDTO.getInscricaoFederal(), exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getCode());
    }

    @Test
    void cadastrarParteComCnpjInvalido() {
        Mockito.mockStatic(InscricaoFederalValidator.class);
        Mockito.when(InscricaoFederalValidator.isValid("12345678000195")).thenReturn(false);

        parteEnvolvidaDTO.setInscricaoFederal("12345678000195");

        AttusException exception = assertThrows(AttusException.class, () -> parteEnvolvidaService.cadastrar(parteEnvolvidaDTO));
        assertEquals(MENSAGEM_CNPJ_INVALIDO + parteEnvolvidaDTO.getInscricaoFederal(), exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getCode());
    }

    @Test
    void cadastrarParteComInscricaoFederalInvalida() {
        parteEnvolvidaDTO.setInscricaoFederal("12345");

        AttusException exception = assertThrows(AttusException.class, () -> parteEnvolvidaService.cadastrar(parteEnvolvidaDTO));
        assertEquals(MENSAGEM_INSCRICAO_INVALIDA + parteEnvolvidaDTO.getInscricaoFederal(), exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getCode());
    }

    @Test
    void atualizarParteEnvolvidaComSucesso() {
        Long id = 1L;
        Mockito.when(parteEnvolvidaRepository.findById(id)).thenReturn(Optional.of(parteEnvolvida));
        Mockito.when(parteEnvolvidaRepository.save(Mockito.any(ParteEnvolvida.class))).thenReturn(parteEnvolvida);

        parteEnvolvidaService.atualizarParteEnvolvida(atualizarParteEnvolvidaDTO, id);

        Mockito.verify(parteEnvolvidaRepository, Mockito.times(1)).save(Mockito.any(ParteEnvolvida.class));
    }

    @Test
    void atualizarParteEnvolvidaNaoEncontrada() {
        Long id = 1L;
        Mockito.when(parteEnvolvidaRepository.findById(id)).thenReturn(Optional.empty());

        AttusException exception = assertThrows(AttusException.class, () -> parteEnvolvidaService.atualizarParteEnvolvida(atualizarParteEnvolvidaDTO, id));

        assertEquals(MENSAGEM_PARTE_NAO_ENCONTRADA, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getCode());
    }

    @Test
    void gravarParteComErro() {
        Mockito.when(parteEnvolvidaRepository.save(Mockito.any(ParteEnvolvida.class)))
                .thenThrow(new RuntimeException("Erro de banco de dados"));

        AttusException exception = assertThrows(AttusException.class, () -> parteEnvolvidaService.gravar(parteEnvolvida));

        assertEquals("Erro ao salvar Parte Envolvida no banco de dados: Erro de banco de dados", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getCode());
    }
    @Test
    void validarInscricaoFederalComNull() {
        String resultado = parteEnvolvidaService.validarInscricaoFederal(null);
        assertNull(resultado, "O resultado deveria ser null para inscrição federal nula.");
    }

}