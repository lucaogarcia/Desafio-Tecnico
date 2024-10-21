package com.biblioteca.desafio.dto;


import lombok.Builder;

import java.time.LocalDate;

@Builder
public record EmprestimoDto(
   Integer id,
   Integer usuarioId,
   Integer livroId,
   LocalDate dataEmprestimo,
   LocalDate dataDevolucao,
   boolean status
){}
