package com.biblioteca.desafio.service;

import com.biblioteca.desafio.dto.UsuarioDto;
import com.biblioteca.desafio.model.Usuario;
import com.biblioteca.desafio.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    public List<Usuario> listarUsuario(){
        return repository.findAll();
    }

    public Optional<Usuario> encontrarPorId(Integer id) {
        return repository.findById(id);
    }

    public Usuario criarUsuario(UsuarioDto dto) {
        Usuario usuario = new Usuario();
        if(dto.dataCadastro().isAfter(LocalDate.now())){
            throw new IllegalArgumentException("A data de cadastro não pode ser posterior a hoje.");
        }else {
            usuario.setNome(dto.nome());
            usuario.setEmail(dto.email());
            usuario.setDataCadastro(dto.dataCadastro());
            usuario.setTelefone(dto.telefone());
            return repository.save(usuario);
        }
    }

    public Usuario atualizarUsuario(UsuarioDto dto, Integer id) {
        Optional<Usuario> optionalUsuario = repository.findById(id);

        if (optionalUsuario.isPresent()) {
            if(dto.dataCadastro().isAfter(LocalDate.now())){
                throw new IllegalArgumentException("A data de cadastro não pode ser posterior a hoje.");
            }else {
                Usuario usuario = optionalUsuario.get();
                usuario.setNome(dto.nome());
                usuario.setEmail(dto.email());
                usuario.setDataCadastro(dto.dataCadastro());
                usuario.setTelefone(dto.telefone());
                return repository.save(usuario);
            }
        }
        throw new IllegalArgumentException("Usuario não encontrado.");
    }

    public void deletarUsuario(Integer id) {
        repository.deleteById(id);
    }


}
