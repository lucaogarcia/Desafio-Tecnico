package com.biblioteca.desafio.controller;

import com.biblioteca.desafio.dto.UsuarioDto;
import com.biblioteca.desafio.model.Usuario;
import com.biblioteca.desafio.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos(){
        List<Usuario> lista = service.listarUsuario();
        return ResponseEntity.ok().body(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> encontrarPorId(@PathVariable Integer id) {
        Optional<Usuario> usuario = service.encontrarPorId(id);
        return usuario.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Usuario> criarUsuario(@RequestBody UsuarioDto dto) {
        Usuario usuario = service.criarUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Integer id,@RequestBody UsuarioDto dto) {
        Usuario usuario = service.atualizarUsuario(dto, id);
        return ResponseEntity.of(Optional.ofNullable(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagarUsuario(@PathVariable Integer id) {
        service.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}