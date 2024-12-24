package com.kauanmeira.api_contrato.domain.parte;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kauanmeira.api_contrato.domain.contrato.Contrato;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
@Table(name = "PARTE_ENVOLVIDA")
public class ParteEnvolvida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    private Long id;

    @Column(name = "INSCRICAO_FEDERAL")
    private String inscricaoFederal;

    @Column(name = "NOME_COMPLETO")
    private String nomeCompleto;

    @Column(name = "DATA_NASCIMENTO")
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_PARTE")
    private TipoParte tipoParte;

    @Column(name = "TELEFONE")
    private String telefone;

    @Email
    @Column(name = "EMAIL")
    private String email;

    @ManyToMany(mappedBy = "partesEnvolvidas")
    @JsonIgnore
    private List<Contrato> contratos = new ArrayList<>();
}
