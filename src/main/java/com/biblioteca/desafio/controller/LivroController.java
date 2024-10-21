package com.biblioteca.desafio.controller;

import com.biblioteca.desafio.dto.LivroDto;
import com.biblioteca.desafio.model.Livro;
import com.biblioteca.desafio.repositories.LivroRepository;
import com.biblioteca.desafio.service.GoogleApiService;
import com.biblioteca.desafio.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/livro")
public class LivroController {

    @Autowired
    private LivroService service;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private GoogleApiService googleApiService;

    @GetMapping
    public ResponseEntity<List<Livro>> listarTodos() {
        List<Livro> lista = service.listarLivro();
        return ResponseEntity.ok().body(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livro> encontrarPorId(@PathVariable Integer id) {
        Optional<Livro> livro = service.encontrarPorId(id);
        return livro.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Livro> criarLivro(@RequestBody LivroDto dto) {
        Livro livro = service.criarLivro(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(livro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Livro> atualizarLivro(@PathVariable Integer id,@RequestBody LivroDto dto) {
        Livro livro = service.atualizarLivro(dto, id);
        return ResponseEntity.of(Optional.ofNullable(livro));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagarLivro(@PathVariable Integer id) {
        service.apagarLivro(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscarLivroGoogle/{titulo}")
    public ResponseEntity<String> buscarLivroGoogle(@PathVariable String titulo){
        try {
            String respostaBody = googleApiService.buscarLivroGoogle(titulo);
            return ResponseEntity.ok().body(respostaBody);
        } catch (IOException | InterruptedException e) {
            throw new IllegalArgumentException("Livro não encontrado." + e.getMessage());
        }

    }

    @PostMapping("/adicionarLivroGoogle/{titulo}/{id}")
    public ResponseEntity<Livro> adicionarLivroGoogle(@PathVariable String titulo,@PathVariable String id) {
        try {
            String respostaBody = googleApiService.buscarLivroGoogle(titulo);
            Livro livro = googleApiService.parseGoogleBooksResponse(respostaBody, id);
            Livro livroSalvo = livroRepository.save(livro);
            return ResponseEntity.ok().body(livroSalvo);
        } catch (IOException | InterruptedException e) {
            throw new IllegalArgumentException("Livro não encontrado." + e.getMessage());
        }
    }
}