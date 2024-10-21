package com.biblioteca.desafio.repositories;

import com.biblioteca.desafio.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Integer> {
    List<Livro> findAllByCategoria(String categoria);
}
