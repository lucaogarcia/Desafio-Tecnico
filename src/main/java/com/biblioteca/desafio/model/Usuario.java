package com.biblioteca.desafio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;


import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name="Usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String nome;

    @Email(message = "Email inválido")
    @NotNull
    private String email;

    @Column(name = "data_cadastro")
    @NotNull
    private LocalDate dataCadastro;

    @NotNull
    private String telefone;
}
