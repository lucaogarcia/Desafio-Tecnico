package com.biblioteca.desafio.repositories;

import com.biblioteca.desafio.model.Emprestimo;
import com.biblioteca.desafio.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EmprestimoRepository extends JpaRepository<Emprestimo, Integer> {

    boolean existsByLivroIdAndStatus(Livro livro, boolean status);
}
