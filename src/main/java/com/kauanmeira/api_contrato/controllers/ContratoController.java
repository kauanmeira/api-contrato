package com.kauanmeira.api_contrato.controllers;

import com.kauanmeira.api_contrato.domain.contrato.ContratoService;
import com.kauanmeira.api_contrato.domain.parte.ParteEnvolvida;
import com.kauanmeira.api_contrato.domain.parte.ParteEnvolvidaService;
import com.kauanmeira.api_contrato.dto.contrato.AtualizarContratoDTO;
import com.kauanmeira.api_contrato.dto.contrato.ContratoDTO;
import com.kauanmeira.api_contrato.dto.parteEnvolvida.ParteEnvolvidaDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/contrato")
public class ContratoController {

    @Autowired
    private ContratoService contratoService;

    @Autowired
    private ParteEnvolvidaService parteEnvolvidaService;

    @PostMapping
    public ContratoDTO criarContrato(@Valid @RequestBody ContratoDTO contrato) {
        List<ParteEnvolvida> partesEnvolvidas = new ArrayList<>();

        for (ParteEnvolvidaDTO parteEnvolvidaDTO : contrato.getPartesEnvolvidas()) {
            partesEnvolvidas.add(parteEnvolvidaService.cadastrar(parteEnvolvidaDTO));
        }
        return contratoService.cadastrar(contrato, partesEnvolvidas);
    }

    @PutMapping("/atualizar/{numeroContrato}")
    public ResponseEntity<String> atualizar(@PathVariable Long numeroContrato, @Valid @RequestBody AtualizarContratoDTO atualizarContratoDTO) {
        contratoService.atualizar(atualizarContratoDTO, numeroContrato);
        return ResponseEntity.status(HttpStatus.OK).body("Contrato atualizado com sucesso!");
    }

    @PutMapping("/arquivar/{numeroContrato}")
    public ResponseEntity<String> arquivar(@PathVariable Long numeroContrato) {
        contratoService.arquivar(numeroContrato);
        return ResponseEntity.status(HttpStatus.OK).body("Contrato arquivado com sucesso!");
    }
    @PutMapping("/desarquivar/{numeroContrato}")
    public ResponseEntity<String> desarquivar(@PathVariable Long numeroContrato) {
        contratoService.desarquivar(numeroContrato);
        return ResponseEntity.status(HttpStatus.OK).body("Contrato arquivado com sucesso!");
    }

    @GetMapping("/{numeroContrato}")
    public ResponseEntity<ContratoDTO> buscarContratoPorNumero(@PathVariable Long numeroContrato) {
        return ResponseEntity.ok(contratoService.buscarPorNumero(numeroContrato));
    }

    @GetMapping("/buscar-por-data")
    public ResponseEntity<List<ContratoDTO>> buscarContratoPorDataCriacao(@RequestParam LocalDate dataCriacao) {
        return ResponseEntity.ok(contratoService.buscarPorData(dataCriacao));
    }

    @GetMapping("/buscar-por-inscricao")
    public ResponseEntity<List<ContratoDTO>> buscarContratoPorInscricao(@RequestParam(value = "inscricao") String inscricaoFederal) {
        return ResponseEntity.ok(contratoService.buscarPorInscricao(inscricaoFederal));
    }
}