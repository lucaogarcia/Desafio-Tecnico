package com.biblioteca.desafio.repository;

import com.biblioteca.desafio.model.Emprestimo;
import com.biblioteca.desafio.model.Livro;
import com.biblioteca.desafio.model.Usuario;
import com.biblioteca.desafio.repositories.EmprestimoRepository;
import com.biblioteca.desafio.repositories.LivroRepository;
import com.biblioteca.desafio.repositories.UsuarioRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class EmprestimoRepositoryTest {

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Test
    public void salvarEmprestimoRepositoryRetornarEmprestimo() {
        Usuario usuario = Usuario.builder()
                .nome("Jose Almeida")
                .email("jose@gmail.com")
                .dataCadastro(LocalDate.parse("2023-06-05"))
                .telefone("44987612346").build();

        Livro livro = Livro.builder()
                .titulo("O Alquimista")
                .autor("Paulo Coelho")
                .isbn("978-0061122415")
                .dataPublicacao(LocalDate.parse("1988-04-15"))
                .categoria("Romance").build();

        Emprestimo emprestimo = Emprestimo.builder()
                .dataEmprestimo(LocalDate.parse("2023-06-01"))
                .dataDevolucao(LocalDate.parse("2023-06-15"))
                .usuarioId(usuario)
                .livroId(livro)
                .status(true).build();

        usuarioRepository.save(usuario);
        livroRepository.save(livro);

        Emprestimo emprestimoSalvo = emprestimoRepository.save(emprestimo);

        Assertions.assertThat(emprestimoSalvo).isNotNull();
        Assertions.assertThat(emprestimoSalvo.getId()).isGreaterThan(0);
    }

    @Test
    public void salvarEmprestimoRepositoryRetornarListaEmprestimos() {
        Usuario usuario1 = Usuario.builder()
                .nome("Jose Almeida")
                .email("jose@gmail.com")
                .dataCadastro(LocalDate.parse("2023-06-05"))
                .telefone("44987612346").build();

        Livro livro1 = Livro.builder()
                .titulo("O Alquimista")
                .autor("Paulo Coelho")
                .isbn("978-0061122415")
                .dataPublicacao(LocalDate.parse("1988-04-15"))
                .categoria("Romance").build();

        Emprestimo emprestimo1 = Emprestimo.builder()
                .dataEmprestimo(LocalDate.parse("2023-06-01"))
                .dataDevolucao(LocalDate.parse("2023-06-15"))
                .usuarioId(usuario1)
                .livroId(livro1)
                .status(true).build();

        Usuario usuario2 = Usuario.builder()
                .nome("Carlos Nobrega")
                .email("nobrega23@gmail.com")
                .dataCadastro(LocalDate.parse("2024-08-23"))
                .telefone("44989073421").build();

        Livro livro2 = Livro.builder()
                .titulo("1984")
                .autor("George Orwell")
                .dataPublicacao(LocalDate.parse("1949-06-08"))
                .isbn("978-0451524935")
                .categoria("Distopia").build();

        Emprestimo emprestimo2 = Emprestimo.builder()
                .dataEmprestimo(LocalDate.parse("2023-07-01"))
                .dataDevolucao(LocalDate.parse("2023-07-15"))
                .usuarioId(usuario2)
                .livroId(livro2)
                .status(true).build();


        usuarioRepository.save(usuario1);
        usuarioRepository.save(usuario2);
        livroRepository.save(livro1);
        livroRepository.save(livro2);
        emprestimoRepository.save(emprestimo1);
        emprestimoRepository.save(emprestimo2);

        List<Emprestimo> listaEmprestimos = emprestimoRepository.findAll();

        Assertions.assertThat(listaEmprestimos).isNotNull();
        Assertions.assertThat(listaEmprestimos.size()).isEqualTo(2);
    }

    @Test
    public void encontreEmprestimoRepositoryPorId() {
        Usuario usuario = Usuario.builder()
                .nome("Jose Almeida")
                .email("jose@gmail.com")
                .dataCadastro(LocalDate.parse("2023-06-05"))
                .telefone("44987612346").build();

        Livro livro = Livro.builder()
                .titulo("O Alquimista")
                .autor("Paulo Coelho")
                .isbn("978-0061122415")
                .dataPublicacao(LocalDate.parse("1988-04-15"))
                .categoria("Romance").build();

        Emprestimo emprestimo = Emprestimo.builder()
                .dataEmprestimo(LocalDate.parse("2023-06-01"))
                .dataDevolucao(LocalDate.parse("2023-06-15"))
                .usuarioId(usuario)
                .livroId(livro)
                .status(true).build();

        usuarioRepository.save(usuario);
        livroRepository.save(livro);
        emprestimoRepository.save(emprestimo);

        Optional<Emprestimo> emprestimoSalvo = emprestimoRepository.findById(emprestimo.getId());

        Assertions.assertThat(emprestimoSalvo).isNotNull();
    }

    @Test
    public void emprestimoAtualiza() {
        Usuario usuario = Usuario.builder()
                .nome("Jose Almeida")
                .email("jose@gmail.com")
                .dataCadastro(LocalDate.parse("2023-06-05"))
                .telefone("44987612346").build();

        Livro livro = Livro.builder()
                .titulo("O Alquimista")
                .autor("Paulo Coelho")
                .isbn("978-0061122415")
                .dataPublicacao(LocalDate.parse("1988-04-15"))
                .categoria("Romance").build();

        Emprestimo emprestimo = Emprestimo.builder()
                .dataEmprestimo(LocalDate.parse("2023-06-01"))
                .dataDevolucao(LocalDate.parse("2023-06-15"))
                .usuarioId(usuario)
                .livroId(livro)
                .status(true).build();

        usuarioRepository.save(usuario);
        livroRepository.save(livro);
        emprestimoRepository.save(emprestimo);

        Optional<Emprestimo> emprestimoSalvo = emprestimoRepository.findById(emprestimo.getId());

        Emprestimo emprestimoAtualizado = emprestimoSalvo.get();
        emprestimoAtualizado.setDataEmprestimo(LocalDate.parse("2023-06-05"));
        emprestimoAtualizado.setDataDevolucao(LocalDate.parse("2023-06-20"));

        emprestimoRepository.save(emprestimoAtualizado);

        Assertions.assertThat(emprestimoAtualizado.getDataEmprestimo()).isNotNull();
        Assertions.assertThat(emprestimoAtualizado.getDataDevolucao()).isNotNull();
    }

    @Test
    public void deletarEmprestimoEmprestimoEstaVazio() {
        Usuario usuario = Usuario.builder()
                .nome("Jose Almeida")
                .email("jose@gmail.com")
                .dataCadastro(LocalDate.parse("2023-06-05"))
                .telefone("44987612346").build();

        Livro livro = Livro.builder()
                .titulo("O Alquimista")
                .autor("Paulo Coelho")
                .isbn("978-0061122415")
                .dataPublicacao(LocalDate.parse("1988-04-15"))
                .categoria("Romance").build();

        Emprestimo emprestimo = Emprestimo.builder()
                .dataEmprestimo(LocalDate.parse("2023-06-01"))
                .dataDevolucao(LocalDate.parse("2023-06-15"))
                .usuarioId(usuario)
                .livroId(livro)
                .status(true).build();

        usuarioRepository.save(usuario);
        livroRepository.save(livro);
        emprestimoRepository.save(emprestimo);

        emprestimoRepository.deleteById(emprestimo.getId());
        Optional<Emprestimo> emprestimoRetorno = emprestimoRepository.findById(emprestimo.getId());

        Assertions.assertThat(emprestimoRetorno).isEmpty();
    }

    @Test
    public void VerificaEmprestimoAtivo(){
        Usuario usuario = Usuario.builder()
                .nome("Jose Almeida")
                .email("jose@gmail.com")
                .dataCadastro(LocalDate.parse("2023-06-05"))
                .telefone("44987612346").build();

        Livro livro = Livro.builder()
                .titulo("O Alquimista")
                .autor("Paulo Coelho")
                .isbn("978-0061122415")
                .dataPublicacao(LocalDate.parse("1988-04-15"))
                .categoria("Romance").build();

        Emprestimo emprestimo = Emprestimo.builder()
                .dataEmprestimo(LocalDate.parse("2023-06-01"))
                .dataDevolucao(LocalDate.parse("2023-06-15"))
                .usuarioId(usuario)
                .livroId(livro)
                .status(true).build();

        usuarioRepository.save(usuario);
        livroRepository.save(livro);
        emprestimoRepository.save(emprestimo);

        Assertions.assertThat(emprestimoRepository.existsByLivroIdAndStatus(livro,true)).isEqualTo(true);
    }
}