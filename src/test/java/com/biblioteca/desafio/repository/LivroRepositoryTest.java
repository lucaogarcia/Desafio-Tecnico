package com.biblioteca.desafio.repository;

import com.biblioteca.desafio.model.Livro;
import com.biblioteca.desafio.repositories.LivroRepository;
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
public class LivroRepositoryTest {

    @Autowired
    private LivroRepository livroRepository;

    @Test
    public void salvarLivroRepositoryRetornarLivro() {
        Livro livro = Livro.builder()
                .titulo("O Alquimista")
                .autor("Paulo Coelho")
                .isbn("978-0061122415")
                .dataPublicacao(LocalDate.parse("1988-04-15"))
                .categoria("Romance").build();

        Livro livroSalvo = livroRepository.save(livro);

        Assertions.assertThat(livroSalvo).isNotNull();
        Assertions.assertThat(livroSalvo.getId()).isGreaterThan(0);
    }

    @Test
    public void salvarLivroRepositoryRetornarListaLivros() {
        Livro livro1 = Livro.builder()
                .titulo("O Alquimista")
                .autor("Paulo Coelho")
                .isbn("978-0061122415")
                .dataPublicacao(LocalDate.parse("1988-04-15"))
                .categoria("Romance").build();

        Livro livro2 = Livro.builder()
                .titulo("1984")
                .autor("George Orwell")
                .dataPublicacao(LocalDate.parse("1949-06-08"))
                .isbn("978-0451524935")
                .categoria("Distopia").build();

        livroRepository.save(livro1);
        livroRepository.save(livro2);

        List<Livro> listaLivros = livroRepository.findAll();

        Assertions.assertThat(listaLivros).isNotNull();
        Assertions.assertThat(listaLivros.size()).isEqualTo(2);
    }

    @Test
    public void encontreLivroRepositoryPorId() {
        Livro livro = Livro.builder()
                .titulo("O Alquimista")
                .autor("Paulo Coelho")
                .isbn("978-0061122415")
                .dataPublicacao(LocalDate.parse("1988-04-15"))
                .categoria("Romance").build();

        livroRepository.save(livro);

        Optional<Livro> livroSalvo = livroRepository.findById(livro.getId());

        Assertions.assertThat(livroSalvo).isNotNull();
    }

    @Test
    public void livroAtualiza() {
        Livro livro = Livro.builder()
                .titulo("O Alquimista")
                .autor("Paulo Coelho")
                .isbn("978-0061122415")
                .dataPublicacao(LocalDate.parse("1988-04-15"))
                .categoria("Romance").build();

        livroRepository.save(livro);

        Optional<Livro> livroSalvo = livroRepository.findById(livro.getId());

        Livro livroAtualizado = livroSalvo.get();
        livroAtualizado.setTitulo("O Alquimista - Edição Especial");
        livroAtualizado.setAutor("Paulo Coelho");
        livroAtualizado.setDataPublicacao(LocalDate.parse("1988-04-15"));
        livroAtualizado.setIsbn("978-0061122415");
        livroAtualizado.setCategoria("Ficção Europeia");

        livroRepository.save(livroAtualizado);

        Assertions.assertThat(livroAtualizado.getTitulo()).isNotNull();
        Assertions.assertThat(livroAtualizado.getIsbn()).isNotNull();
    }

    @Test
    public void deletarLivroLivroEstaVazio() {
        Livro livro = Livro.builder()
                .titulo("O Alquimista")
                .autor("Paulo Coelho")
                .isbn("978-0061122415")
                .dataPublicacao(LocalDate.parse("1988-04-15"))
                .categoria("Romance").build();

        livroRepository.save(livro);

        livroRepository.deleteById(livro.getId());
        Optional<Livro> livroRetorno = livroRepository.findById(livro.getId());

        Assertions.assertThat(livroRetorno).isEmpty();
    }
}