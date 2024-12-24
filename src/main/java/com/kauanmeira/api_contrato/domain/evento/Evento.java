package com.kauanmeira.api_contrato.domain.evento;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "EVENTO")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_EVENTO")
    private TipoEvento tipoEvento;

    @Column(name = "DATA_REGISTRO")
    private LocalDate dataRegistro;

    @Column(name = "DESCRICAO_EVENTO")
    private String descricaoEvento;
}
