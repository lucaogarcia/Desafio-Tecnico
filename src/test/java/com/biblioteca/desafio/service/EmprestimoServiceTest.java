package com.biblioteca.desafio.service;

import com.biblioteca.desafio.dto.EmprestimoDto;
import com.biblioteca.desafio.model.Emprestimo;
import com.biblioteca.desafio.model.Livro;
import com.biblioteca.desafio.model.Usuario;
import com.biblioteca.desafio.repositories.EmprestimoRepository;
import com.biblioteca.desafio.repositories.LivroRepository;
import com.biblioteca.desafio.repositories.UsuarioRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmprestimoServiceTest {

    @Mock
    private EmprestimoRepository emprestimoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private LivroRepository livroRepository;

    @InjectMocks
    private EmprestimoService emprestimoService;

    private Usuario usuario;

    private Livro livro;

    private Emprestimo emprestimo;

    private EmprestimoDto emprestimoDto;

    @BeforeEach
    public void init(){
        usuario = Usuario.builder()
                .nome("Jose Almeida")
                .email("jose@gmail.com")
                .dataCadastro(LocalDate.parse("2023-06-05"))
                .telefone("44987612346").build();

        livro = Livro.builder()
                .titulo("O Alquimista")
                .autor("Paulo Coelho")
                .isbn("978-0061122415")
                .dataPublicacao(LocalDate.parse("1988-04-15"))
                .categoria("Romance").build();

        emprestimo = Emprestimo.builder()
                .dataEmprestimo(LocalDate.parse("2023-01-01"))
                .dataDevolucao(LocalDate.parse("2023-01-15"))
                .usuarioId(usuario)
                .livroId(livro)
                .status(true)
                .build();

        emprestimoDto = EmprestimoDto.builder()
                .dataEmprestimo(LocalDate.parse("2023-01-01"))
                .dataDevolucao(LocalDate.parse("2023-01-15"))
                .usuarioId(1)
                .livroId(1)
                .status(true)
                .build();
    }

    @Test
    public void emprestimoServiceCriaEmprestimoRetornaEmprestimo() {

        when(emprestimoRepository.save(Mockito.any(Emprestimo.class))).thenReturn(emprestimo);
        when(usuarioRepository.findById(1)).thenReturn(Optional.ofNullable(usuario));
        when(livroRepository.findById(1)).thenReturn(Optional.ofNullable(livro));

        Emprestimo emprestimoSalvo = emprestimoService.criarEmprestimo(emprestimoDto);

        Assertions.assertThat(emprestimoSalvo).isNotNull();
        Assertions.assertThat(emprestimoSalvo.getDataEmprestimo()).isEqualTo(LocalDate.parse("2023-01-01"));
        Assertions.assertThat(emprestimoSalvo.getDataDevolucao()).isEqualTo(LocalDate.parse("2023-01-15"));
        Assertions.assertThat(emprestimoSalvo.getUsuarioId()).isEqualTo(usuario);
        Assertions.assertThat(emprestimoSalvo.getLivroId()).isEqualTo(livro);
        Assertions.assertThat(emprestimoSalvo.isStatus()).isTrue();
    }

    @Test
    public void emprestimoServiceListaEmprestimos() {
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

        List<Emprestimo> emprestimos = List.of(
                emprestimo,
                Emprestimo.builder()
                        .dataEmprestimo(LocalDate.parse("2023-02-01"))
                        .dataDevolucao(LocalDate.parse("2023-02-15"))
                        .usuarioId(usuario2)
                        .livroId(livro2)
                        .status(false)
                        .build()
        );

        when(emprestimoRepository.findAll()).thenReturn(emprestimos);

        List<Emprestimo> emprestimoSalvo = emprestimoService.listarEmprestimos();

        Assertions.assertThat(emprestimoSalvo).isNotNull();
        Assertions.assertThat(emprestimoSalvo.size()).isEqualTo(2);
        Assertions.assertThat(emprestimoSalvo).containsAll(emprestimos);
    }

    @Test
    public void emprestimoServiceEncontrarPorId() {
        when(emprestimoRepository.findById(1)).thenReturn(Optional.of(emprestimo));

        Optional<Emprestimo> emprestimoSalvo = emprestimoService.encontrarPorId(1);

        Assertions.assertThat(emprestimoSalvo).isPresent();
        Assertions.assertThat(emprestimoSalvo.get().getDataEmprestimo()).isEqualTo(LocalDate.parse("2023-01-01"));
        Assertions.assertThat(emprestimoSalvo.get().getDataDevolucao()).isEqualTo(LocalDate.parse("2023-01-15"));
        Assertions.assertThat(emprestimoSalvo.get().getUsuarioId()).isEqualTo(usuario);
        Assertions.assertThat(emprestimoSalvo.get().getLivroId()).isEqualTo(livro);
        Assertions.assertThat(emprestimoSalvo.get().isStatus()).isTrue();
    }

    @Test
    public void emprestimoAtualizado() {
        EmprestimoDto emprestimoDto = EmprestimoDto.builder()
                .dataEmprestimo(LocalDate.parse("2023-01-01"))
                .dataDevolucao(LocalDate.parse("2023-01-20"))
                .usuarioId(1)
                .livroId(1)
                .status(false)
                .build();

        when(emprestimoRepository.findById(1)).thenReturn(Optional.ofNullable(emprestimo));
        when(emprestimoRepository.save(Mockito.any(Emprestimo.class))).thenReturn(emprestimo);
        when(usuarioRepository.findById(1)).thenReturn(Optional.ofNullable(usuario));
        when(livroRepository.findById(1)).thenReturn(Optional.ofNullable(livro));

        Emprestimo emprestimoAtualizado = emprestimoService.atualizarEmprestimo(1, emprestimoDto);

        Assertions.assertThat(emprestimoAtualizado).isNotNull();
        Assertions.assertThat(emprestimoAtualizado.getDataEmprestimo()).isEqualTo(LocalDate.parse("2023-01-01"));
        Assertions.assertThat(emprestimoAtualizado.getDataDevolucao()).isEqualTo(LocalDate.parse("2023-01-20"));
        Assertions.assertThat(emprestimoAtualizado.getUsuarioId()).isEqualTo(usuario);
        Assertions.assertThat(emprestimoAtualizado.getLivroId()).isEqualTo(livro);
        Assertions.assertThat(emprestimoAtualizado.isStatus()).isFalse();
    }

    @Test
    public void dataEmprestimoIncorreta(){
        EmprestimoDto emprestimoDto = EmprestimoDto.builder()
                .dataEmprestimo(LocalDate.parse("2024-10-21"))
                .dataDevolucao(LocalDate.parse("2024-10-27"))
                .usuarioId(1)
                .livroId(1)
                .status(true)
                .build();

        try {
            emprestimoService.criarEmprestimo(emprestimoDto);
        }catch (Exception e){
            assertEquals("A data do empréstimo não pode ser posterior a hoje.",e.getMessage());
            Assertions.assertThat((Emprestimo) null).isNull();
        }
    }

    @Test
    public void bloqueiaCriarEmprestimoComEmprestimoAtivo() {
        when(emprestimoRepository.existsByLivroIdAndStatus(livro, true)).thenReturn(true);
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(livroRepository.findById(1)).thenReturn(Optional.of(livro));

        try {
            emprestimoService.criarEmprestimo(emprestimoDto);
        } catch (Exception e) {
            assertEquals("O livro não está disponivel para empréstimo",e.getMessage());
            Assertions.assertThat((Emprestimo) null).isNull();
        }
    }

    @Test
    public void emprestimoRecomendaLivro() {
        Livro livro = Livro.builder()
                .id(1)
                .titulo("O Alquimista")
                .autor("Paulo Coelho")
                .isbn("978-0061122415")
                .dataPublicacao(LocalDate.parse("1988-04-15"))
                .categoria("Romance").build();

        Emprestimo emprestimo = Emprestimo.builder()
                .dataEmprestimo(LocalDate.parse("2023-01-01"))
                .dataDevolucao(LocalDate.parse("2023-01-15"))
                .usuarioId(usuario)
                .livroId(livro)
                .status(true)
                .build();

        List<Livro> livros = List.of(
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
                        .categoria("Romance").build()
        );

        when(livroRepository.findById(1)).thenReturn(Optional.of(livro));
        when(emprestimoRepository.findById(1)).thenReturn(Optional.of(emprestimo));
        when(livroRepository.findAllByCategoria(livro.getCategoria())).thenReturn(livros);

        List<Livro> livrosResposta = emprestimoService.recomendarLivro(1);

        Assertions.assertThat(livrosResposta).isNotNull();
        Assertions.assertThat(livrosResposta.size()).isEqualTo(2);
        Assertions.assertThat(livrosResposta).containsAll(livros);
    }
}