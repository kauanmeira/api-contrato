package com.kauanmeira.api_contrato.domain.contrato;

import com.kauanmeira.api_contrato.domain.parte.ParteEnvolvida;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "CONTRATO")

public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NUMERO_CONTRATO", unique = true)
    private Long numeroContrato;

    @Column(name = "DATA_CRIACAO")
    private LocalDate dataCriacao;

    @Column(name = "DESCRICAO_CONTRATO")
    private String descricaoContrato;

    @ManyToMany
    @JoinTable(name = "CONTRATO_PARTE",
            joinColumns = @JoinColumn(name = "CONTRATO_ID"),
            inverseJoinColumns = @JoinColumn(name = "PARTE_ID"))
    private List<ParteEnvolvida> partesEnvolvidas = new ArrayList<>();

    @Column(name = "STATUS_CONTRATO")
    @Enumerated(EnumType.ORDINAL)
    private StatusContrato statusContrato;


}
