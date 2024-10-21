package com.biblioteca.desafio.controller;

import com.biblioteca.desafio.dto.LivroDto;
import com.biblioteca.desafio.model.Livro;
import com.biblioteca.desafio.repositories.LivroRepository;
import com.biblioteca.desafio.service.GoogleApiService;
import com.biblioteca.desafio.service.LivroService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = LivroController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class LivroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LivroService livroService;

    @MockBean
    private LivroRepository livroRepository;

    @MockBean
    private GoogleApiService googleApiService;

    @Autowired
    private ObjectMapper objectMapper;

    private Livro livro;

    private LivroDto livroDto;

    private List<Livro> livros;

    @BeforeEach
    public void init() {
        livro = Livro.builder()
                .id(1)
                .titulo("O Alquimista")
                .autor("Paulo Coelho")
                .isbn("978-0061122415")
                .dataPublicacao(LocalDate.parse("1988-04-15"))
                .categoria("Romance").build();

        livroDto = LivroDto.builder()
                .titulo("O Alquimista")
                .autor("Paulo Coelho")
                .isbn("978-0061122415")
                .dataPublicacao(LocalDate.parse("1988-04-15"))
                .categoria("Romance").build();

        livros = List.of(
                Livro.builder()
                        .id(1)
                        .titulo("O Alquimista")
                        .autor("Paulo Coelho")
                        .isbn("978-0061122415")
                        .dataPublicacao(LocalDate.parse("1988-04-15"))
                        .categoria("Romance").build(),
                Livro.builder()
                        .id(2)
                        .titulo("1984")
                        .autor("George Orwell")
                        .isbn("978-0451524935")
                        .dataPublicacao(LocalDate.parse("1949-06-08"))
                        .categoria("Distopia").build()
        );
    }

    @Test
    public void LivroControllerCriarLivroRetornarLivro() throws Exception {
        given(livroService.criarLivro(ArgumentMatchers.any(LivroDto.class))).willReturn(livro);

        ResultActions resposta = mockMvc.perform(post("/livro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(livroDto)));

        resposta.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.titulo", CoreMatchers.is(livroDto.titulo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.autor", CoreMatchers.is(livroDto.autor())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn", CoreMatchers.is(livroDto.isbn())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataPublicacao", CoreMatchers.is(livroDto.dataPublicacao().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoria", CoreMatchers.is(livroDto.categoria())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void ListarTodosLivros() throws Exception {
        when(livroService.listarLivro()).thenReturn(livros);

        ResultActions resposta = mockMvc.perform(get("/livro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(livros)));

        resposta.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", CoreMatchers.is(livros.size())));
    }

    @Test
    public void encontrarLivroPorId() throws Exception {
        when(livroService.encontrarPorId(1)).thenReturn(Optional.of(livro));

        ResultActions resposta = mockMvc.perform(get("/livro/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(livros)));

        resposta.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.titulo", CoreMatchers.is(livroDto.titulo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.autor", CoreMatchers.is(livroDto.autor())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn", CoreMatchers.is(livroDto.isbn())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataPublicacao", CoreMatchers.is(livroDto.dataPublicacao().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoria", CoreMatchers.is(livroDto.categoria())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void atualizarLivro() throws Exception {
        given(livroService.atualizarLivro(ArgumentMatchers.any(LivroDto.class), ArgumentMatchers.eq(1))).willReturn(livro);

        ResultActions resposta = mockMvc.perform(MockMvcRequestBuilders.put("/livro/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(livroDto)));

        resposta.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.titulo", CoreMatchers.is(livroDto.titulo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.autor", CoreMatchers.is(livroDto.autor())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn", CoreMatchers.is(livroDto.isbn())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataPublicacao", CoreMatchers.is(livroDto.dataPublicacao().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoria", CoreMatchers.is(livroDto.categoria())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void apagarLivro() throws Exception {
        doNothing().when(livroService).apagarLivro(1);

        ResultActions resposta = mockMvc.perform(delete("/livro/1")
                .contentType(MediaType.APPLICATION_JSON));

        resposta.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void LivroRetornaBuscarLivroGoogle() throws Exception {
        String titulo = "OPequenoPríncipe";
        String apiResposta = "{ \"items\": [ { \"volumeInfo\": { \"title\": \"O Pequeno Príncipe\" } } ] }";

        when(googleApiService.buscarLivroGoogle(titulo)).thenReturn(apiResposta);

        ResultActions resposta = mockMvc.perform(get("/livro/buscarLivroGoogle/{titulo}", titulo)
                .contentType(MediaType.APPLICATION_JSON));

        resposta.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(apiResposta))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void LivroAdicionaLivroGoogle() throws Exception {
        String titulo = "OPequenoPríncipe";
        String id = "1D9KEAAAQBAJ";
        String apiResposta = "{ \"items\": [ { \"id\": \"1D9KEAAAQBAJ\", \"volumeInfo\": { \"title\": \"O Pequeno Príncipe\", \"authors\": [\"Antoine de Saint-Exupéry\"], \"industryIdentifiers\": [ { \"identifier\": \"1234567890\" } ], \"publishedDate\": \"1943-04-06\", \"categories\": [\"Fiction\"] } } ] }";

        Livro livro = Livro.builder()
                .id(1)
                .titulo("O Pequeno Príncipe")
                .autor("Antoine de Saint-Exupéry")
                .isbn("1234567890")
                .dataPublicacao(LocalDate.parse("1943-04-06"))
                .categoria("Fiction").build();

        when(googleApiService.buscarLivroGoogle(titulo)).thenReturn(apiResposta);
        when(googleApiService.parseGoogleBooksResponse(apiResposta, id)).thenReturn(livro);
        when(livroRepository.save(livro)).thenReturn(livro);

        ResultActions resposta = mockMvc.perform(post("/livro/adicionarLivroGoogle/{titulo}/{id}", titulo, id)
                .contentType(MediaType.APPLICATION_JSON));

        resposta.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.titulo", CoreMatchers.is(livro.getTitulo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.autor", CoreMatchers.is(livro.getAutor())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn", CoreMatchers.is(livro.getIsbn())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataPublicacao", CoreMatchers.is(livro.getDataPublicacao().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoria", CoreMatchers.is(livro.getCategoria())))
                .andDo(MockMvcResultHandlers.print());
    }
}