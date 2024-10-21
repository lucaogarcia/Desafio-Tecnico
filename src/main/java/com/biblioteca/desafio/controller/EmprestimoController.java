package com.biblioteca.desafio.controller;

import com.biblioteca.desafio.dto.EmprestimoDto;
import com.biblioteca.desafio.model.Emprestimo;
import com.biblioteca.desafio.model.Livro;
import com.biblioteca.desafio.service.EmprestimoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/emprestimo")
public class EmprestimoController {

    @Autowired
    private EmprestimoService service;


    @GetMapping
    public ResponseEntity<List<Emprestimo>> listarTodos() {
        List<Emprestimo> lista = service.listarEmprestimos();
        return ResponseEntity.ok().body(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Emprestimo> encontrarPorId(@PathVariable Integer id) {
        Optional<Emprestimo> emprestimo = service.encontrarPorId(id);
        return emprestimo.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Emprestimo> criarEmprestimo(@RequestBody EmprestimoDto dto) {
        Emprestimo emprestimo = service.criarEmprestimo(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(emprestimo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Emprestimo> atualizarEmprestimo(@PathVariable Integer id, @RequestBody EmprestimoDto dto) {
        Emprestimo emprestimo = service.atualizarEmprestimo(id, dto);
        return ResponseEntity.of(Optional.ofNullable(emprestimo));
    }

    @GetMapping("/recomendarLivro/{id}")
    public ResponseEntity<List<Livro>> recomendarLivro(@PathVariable Integer id) {
        List<Livro> lista = service.recomendarLivro(id);
        return ResponseEntity.ok().body(lista);
    }
}
