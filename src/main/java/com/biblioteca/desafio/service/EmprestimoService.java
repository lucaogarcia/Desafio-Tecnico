package com.biblioteca.desafio.service;

import com.biblioteca.desafio.dto.EmprestimoDto;
import com.biblioteca.desafio.model.Emprestimo;
import com.biblioteca.desafio.model.Livro;
import com.biblioteca.desafio.model.Usuario;
import com.biblioteca.desafio.repositories.EmprestimoRepository;
import com.biblioteca.desafio.repositories.LivroRepository;
import com.biblioteca.desafio.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EmprestimoService {

    @Autowired
    EmprestimoRepository repository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    LivroRepository livroRepository;


    public List<Emprestimo> listarEmprestimos(){
        return repository.findAll();
    }

    public Optional<Emprestimo> encontrarPorId(Integer id){
        return repository.findById(id);
    }

    public Emprestimo criarEmprestimo(EmprestimoDto dto) {
        Emprestimo emprestimo = new Emprestimo();

        if (dto.dataEmprestimo().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data do empréstimo não pode ser posterior a hoje.");
        }else{
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));

        Livro livro = livroRepository.findById(dto.livroId())
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        if(repository.existsByLivroIdAndStatus(livro,true)) {
                throw new IllegalArgumentException("O livro não está disponivel para empréstimo");
            }else {
                emprestimo.setUsuarioId(usuario);
                emprestimo.setLivroId(livro);
                emprestimo.setDataEmprestimo(dto.dataEmprestimo());
                emprestimo.setDataDevolucao(dto.dataDevolucao());
                emprestimo.setStatus(dto.status());

                return repository.save(emprestimo);
            }
        }
    }

    public Emprestimo atualizarEmprestimo(Integer id, EmprestimoDto dto){
        Optional<Emprestimo> optionalEmprestimo = repository.findById(id);

        if(optionalEmprestimo.isPresent()){
            if(dto.dataEmprestimo().isAfter(LocalDate.now())){
                throw new IllegalArgumentException("A data do empréstimo não pode ser posterior a hoje.");
            }else{
                Emprestimo emprestimo = optionalEmprestimo.get();

                Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                        .orElseThrow(() -> new RuntimeException("Usuario não encontrado."));

                Livro livro = livroRepository.findById(dto.livroId())
                        .orElseThrow(() -> new RuntimeException("Livro não encontrado."));

                emprestimo.setUsuarioId(usuario);
                emprestimo.setLivroId(livro);
                emprestimo.setDataEmprestimo(emprestimo.getDataEmprestimo());
                emprestimo.setDataDevolucao(dto.dataDevolucao());
                emprestimo.setStatus(dto.status());

                return repository.save(emprestimo);
            }
        }else
            throw new IllegalArgumentException("Emprestimo não encontrado.");
    }

    public List<Livro> recomendarLivro(Integer id){

        Emprestimo emprestimo = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Emprestimo não encontrado"));

        Livro livro = livroRepository.findById(emprestimo.getLivroId().getId())
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        return livroRepository.findAllByCategoria(livro.getCategoria());

    }

}
