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
@Table(name = "emprestimo")
public class Emprestimo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(name = "usuario_id")
    @ManyToOne
    @NotNull
    private Usuario usuarioId;

    @JoinColumn(name = "livro_id")
    @ManyToOne
    @NotNull
    private Livro livroId;

    @Column(name = "data_emprestimo")
    @NotNull
    private LocalDate dataEmprestimo;

    @Column(name = "data_devolucao")
    @NotNull
    private LocalDate dataDevolucao;

    @NotNull
    private boolean status;
}
