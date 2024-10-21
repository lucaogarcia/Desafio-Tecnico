package com.biblioteca.desafio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "livro")
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String titulo;

    @NotNull
    private String autor;

    @NotNull
    private String isbn;

    @Column(name = "data_publicacao")
    @NotNull
    private LocalDate dataPublicacao;

    @NotNull
    private String categoria;
}
