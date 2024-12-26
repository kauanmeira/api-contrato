package com.kauanmeira.api_contrato.controllers;

import com.kauanmeira.api_contrato.domain.parte.ParteEnvolvidaService;
import com.kauanmeira.api_contrato.dto.parteEnvolvida.AtualizarParteEnvolvidaDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parte-envolvida")
public class ParteEnvolvidaController {

    @Autowired
    private ParteEnvolvidaService parteEnvolvidaService;

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<String> atualizarParteEnvolvida(@PathVariable Long id, @Valid @RequestBody AtualizarParteEnvolvidaDTO atualizarParteEnvolvidaDTO) {
        parteEnvolvidaService.atualizarParteEnvolvida(atualizarParteEnvolvidaDTO, id);
        return ResponseEntity.status(HttpStatus.OK).body("Parte Envolvida atualizada com sucesso!");
    }
}