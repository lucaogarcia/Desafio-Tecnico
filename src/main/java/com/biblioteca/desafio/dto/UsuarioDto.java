package com.biblioteca.desafio.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UsuarioDto(
            Integer id,
            String nome,
            String email,
            LocalDate dataCadastro,
            String telefone
) {}
