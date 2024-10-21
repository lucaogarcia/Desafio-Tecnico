package com.biblioteca.desafio.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record LivroDto(
   Integer id,
   String titulo,
   String autor,
   String isbn,
   LocalDate dataPublicacao,
   String categoria
) {}
