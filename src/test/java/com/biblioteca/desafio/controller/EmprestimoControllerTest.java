package com.biblioteca.desafio.controller;

import com.biblioteca.desafio.dto.EmprestimoDto;
import com.biblioteca.desafio.model.Emprestimo;
import com.biblioteca.desafio.model.Livro;
import com.biblioteca.desafio.model.Usuario;
import com.biblioteca.desafio.service.EmprestimoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = EmprestimoController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class EmprestimoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmprestimoService emprestimoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Emprestimo emprestimo;

    private EmprestimoDto emprestimoDto;

    private List<Emprestimo> emprestimos;

    private Livro livro;

    private Usuario usuario;

    @BeforeEach
    public void init() {
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
                .id(1)
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

        emprestimos = List.of(
                Emprestimo.builder()
                        .id(1)
                        .dataEmprestimo(LocalDate.parse("2023-01-01"))
                        .dataDevolucao(LocalDate.parse("2023-01-15"))
                        .usuarioId(usuario)
                        .livroId(livro)
                        .status(true)
                        .build(),
                Emprestimo.builder()
                        .id(2)
                        .dataEmprestimo(LocalDate.parse("2023-02-01"))
                        .dataDevolucao(LocalDate.parse("2023-02-15"))
                        .usuarioId(usuario2)
                        .livroId(livro2)
                        .status(true)
                        .build()
        );
    }

    @Test
    public void EmprestimoControllerCriarEmprestimoRetornarEmprestimo() throws Exception {
        given(emprestimoService.criarEmprestimo(ArgumentMatchers.any(EmprestimoDto.class))).willReturn(emprestimo);

        ResultActions resposta = mockMvc.perform(post("/emprestimo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emprestimoDto)));

        resposta.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataEmprestimo", CoreMatchers.is(emprestimoDto.dataEmprestimo().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataDevolucao", CoreMatchers.is(emprestimoDto.dataDevolucao().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(emprestimoDto.status())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void ListarTodosEmprestimos() throws Exception {
        when(emprestimoService.listarEmprestimos()).thenReturn(emprestimos);

        ResultActions resposta = mockMvc.perform(get("/emprestimo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emprestimos)));

        resposta.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", CoreMatchers.is(emprestimos.size())));
    }

    @Test
    public void encontrarEmprestimoPorId() throws Exception {
        when(emprestimoService.encontrarPorId(1)).thenReturn(Optional.of(emprestimo));

        ResultActions resposta = mockMvc.perform(get("/emprestimo/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emprestimos)));

        resposta.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataEmprestimo", CoreMatchers.is(emprestimoDto.dataEmprestimo().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataDevolucao", CoreMatchers.is(emprestimoDto.dataDevolucao().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(emprestimoDto.status())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void atualizarEmprestimo() throws Exception {
        given(emprestimoService.atualizarEmprestimo(ArgumentMatchers.any(Integer.class), ArgumentMatchers.any(EmprestimoDto.class))).willReturn(emprestimo);

        ResultActions resposta = mockMvc.perform(MockMvcRequestBuilders.put("/emprestimo/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emprestimoDto)));

        resposta.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataEmprestimo", CoreMatchers.is(emprestimoDto.dataEmprestimo().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataDevolucao", CoreMatchers.is(emprestimoDto.dataDevolucao().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(emprestimoDto.status())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void recomendarLivro() throws Exception {
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

        given(emprestimoService.recomendarLivro(1)).willReturn(livros);

        ResultActions resposta = mockMvc.perform(get("/emprestimo/recomendarLivro/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(livros)));

        resposta.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", CoreMatchers.is(livros.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].titulo", CoreMatchers.is(livros.get(0).getTitulo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].titulo", CoreMatchers.is(livros.get(1).getTitulo())))
                .andDo(MockMvcResultHandlers.print());
        }
}