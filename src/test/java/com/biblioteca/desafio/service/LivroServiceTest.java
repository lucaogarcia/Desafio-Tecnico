package com.biblioteca.desafio.service;

import com.biblioteca.desafio.dto.LivroDto;
import com.biblioteca.desafio.model.Livro;
import com.biblioteca.desafio.repositories.LivroRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LivroServiceTest {

    @Mock
    private LivroRepository livroRepository;

    @InjectMocks
    private LivroService livroService;

    private Livro livro;

    private LivroDto livroDto;

    private List<Livro> livros;

    @BeforeEach
    public void init(){
        livro = Livro.builder()
                .titulo("O Alquimista")
                .autor("Paulo Coelho")
                .dataPublicacao(LocalDate.parse("1988-04-15"))
                .isbn("9780061122415")
                .categoria("Romance").build();

        livroDto = LivroDto.builder()
                .titulo("O Alquimista")
                .autor("Paulo Coelho")
                .dataPublicacao(LocalDate.parse("1988-04-15"))
                .isbn("9780061122415")
                .categoria("Romance").build();

        livros = List.of(
                Livro.builder()
                        .titulo("O Alquimista")
                        .autor("Paulo Coelho")
                        .dataPublicacao(LocalDate.parse("1988-04-15"))
                        .isbn("9780061122415")
                        .categoria("Romance").build(),
                Livro.builder()
                        .titulo("1984")
                        .autor("George Orwell")
                        .dataPublicacao(LocalDate.parse("1949-06-08"))
                        .isbn("9780451524935")
                        .categoria("Distopia").build()
        );
    }

    @Test
    public void LivroServiceCriaLivroRetornaLivro() {
        when(livroRepository.save(Mockito.any(Livro.class))).thenReturn(livro);

        Livro livroSalvo = livroService.criarLivro(livroDto);

        Assertions.assertThat(livroSalvo).isNotNull();
        Assertions.assertThat(livroSalvo.getTitulo()).isEqualTo("O Alquimista");
        Assertions.assertThat(livroSalvo.getAutor()).isEqualTo("Paulo Coelho");
        Assertions.assertThat(livroSalvo.getDataPublicacao()).isEqualTo(LocalDate.parse("1988-04-15"));
        Assertions.assertThat(livroSalvo.getIsbn()).isEqualTo("9780061122415");
        Assertions.assertThat(livroSalvo.getCategoria()).isEqualTo("Romance");
    }

    @Test
    public void LivroServiceListaLivros() {
        when(livroRepository.findAll()).thenReturn(livros);

        List<Livro> livroSalvo = livroService.listarLivro();

        Assertions.assertThat(livroSalvo).isNotNull();
        Assertions.assertThat(livroSalvo.size()).isEqualTo(2);
        Assertions.assertThat(livroSalvo).containsAll(livros);
    }

    @Test
    public void livroServiceEncontrarPorId() {
        when(livroRepository.findById(1)).thenReturn(Optional.of(livro));

        Optional<Livro> livroSalvo = livroService.encontrarPorId(1);

        Assertions.assertThat(livroSalvo).isPresent();
        Assertions.assertThat(livroSalvo.get().getTitulo()).isEqualTo("O Alquimista");
        Assertions.assertThat(livroSalvo.get().getAutor()).isEqualTo("Paulo Coelho");
        Assertions.assertThat(livroSalvo.get().getDataPublicacao()).isEqualTo(LocalDate.parse("1988-04-15"));
        Assertions.assertThat(livroSalvo.get().getIsbn()).isEqualTo("9780061122415");
        Assertions.assertThat(livroSalvo.get().getCategoria()).isEqualTo("Romance");
    }

    @Test
    public void LivroAtualizado() {
        LivroDto livroDto = LivroDto.builder()
                .titulo("O Alquimista - Edição Especial")
                .autor("Paulo Coelho")
                .dataPublicacao(LocalDate.parse("1988-04-15"))
                .isbn("9780061122415")
                .categoria("Ficção Europeia").build();

        when(livroRepository.findById(1)).thenReturn(Optional.ofNullable(livro));
        when(livroRepository.save(Mockito.any(Livro.class))).thenReturn(livro);

        Livro livroAtualizado = livroService.atualizarLivro(livroDto, 1);

        Assertions.assertThat(livroAtualizado).isNotNull();
        Assertions.assertThat(livroAtualizado.getTitulo()).isEqualTo("O Alquimista - Edição Especial");
        Assertions.assertThat(livroAtualizado.getAutor()).isEqualTo("Paulo Coelho");
        Assertions.assertThat(livroAtualizado.getDataPublicacao()).isEqualTo(LocalDate.parse("1988-04-15"));
        Assertions.assertThat(livroAtualizado.getIsbn()).isEqualTo("9780061122415");
        Assertions.assertThat(livroAtualizado.getCategoria()).isEqualTo("Ficção Europeia");
    }

    @Test
    public void DeletarLivro() {
        assertAll(() -> livroService.apagarLivro(1));
    }
}