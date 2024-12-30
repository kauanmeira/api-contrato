package com.kauanmeira.api_contrato.controllers;

import com.kauanmeira.api_contrato.domain.parte.ParteEnvolvidaService;
import com.kauanmeira.api_contrato.dto.parte.AtualizarParteEnvolvidaDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parte-envolvida")
public class ParteEnvolvidaController {

    private final ParteEnvolvidaService parteEnvolvidaService;

    public ParteEnvolvidaController(ParteEnvolvidaService parteEnvolvidaService) {
        this.parteEnvolvidaService = parteEnvolvidaService;
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<String> atualizarParteEnvolvida(@PathVariable Long id, @Valid @RequestBody AtualizarParteEnvolvidaDTO atualizarParteEnvolvidaDTO) {
        try{
            parteEnvolvidaService.atualizarParteEnvolvida(atualizarParteEnvolvidaDTO, id);
            return ResponseEntity.status(HttpStatus.OK).body("Parte Envolvida atualizada com sucesso!");
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro na atualização.");
        }
    }
}
