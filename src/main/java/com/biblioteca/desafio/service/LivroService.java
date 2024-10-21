package com.biblioteca.desafio.service;

import com.biblioteca.desafio.dto.LivroDto;
import com.biblioteca.desafio.model.Livro;
import com.biblioteca.desafio.repositories.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    @Autowired
    private LivroRepository repository;

    public List<Livro> listarLivro() {
        return repository.findAll();
    }

    public Optional<Livro> encontrarPorId(Integer id) {
        return repository.findById(id);
    }

    public Livro criarLivro(LivroDto dto) {
        Livro livro = new Livro();
        livro.setTitulo(dto.titulo());
        livro.setAutor(dto.autor());
        livro.setIsbn(dto.isbn());
        livro.setDataPublicacao(dto.dataPublicacao());
        livro.setCategoria(dto.categoria());
        return repository.save(livro);
    }

    public Livro atualizarLivro(LivroDto dto, Integer id) {
        Optional<Livro> optionalLivro = repository.findById(id);
        if (optionalLivro.isPresent()) {
            Livro livro = optionalLivro.get();
            livro.setTitulo(dto.titulo());
            livro.setAutor(dto.autor());
            livro.setIsbn(dto.isbn());
            livro.setDataPublicacao(dto.dataPublicacao());
            livro.setCategoria(dto.categoria());
            return repository.save(livro);
        }
        throw new IllegalArgumentException("Livro não encontrado.");
    }

    public void apagarLivro(Integer id) {
        repository.deleteById(id);
    }
}
