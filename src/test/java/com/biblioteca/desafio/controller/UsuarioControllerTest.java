package com.biblioteca.desafio.controller;

import com.biblioteca.desafio.dto.UsuarioDto;
import com.biblioteca.desafio.model.Usuario;
import com.biblioteca.desafio.service.UsuarioService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
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


@WebMvcTest(controllers = UsuarioController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario usuario;

    private UsuarioDto usuarioDto;

    private List<Usuario> usuarios;

    @BeforeEach
    public void init(){
        usuario = Usuario.builder()
                .nome("Jose Almeida")
                .email("jose@gmail.com")
                .dataCadastro(LocalDate.parse("2023-06-05"))
                .telefone("44987612346").build();

        usuarioDto = UsuarioDto.builder()
                .nome("Jose Almeida")
                .email("jose@gmail.com")
                .dataCadastro(LocalDate.parse("2023-06-05"))
                .telefone("44987612346").build();

        usuarios = List.of(
                Usuario.builder()
                        .nome("Jose Almeida")
                        .email("jose@gmail.com")
                        .dataCadastro(LocalDate.parse("2023-06-05"))
                        .telefone("44987612346").build(),
                Usuario.builder()
                        .nome("Carlos Nobrega")
                        .email("carlos@gmail.com")
                        .dataCadastro(LocalDate.parse("2024-08-23"))
                        .telefone("44989073421").build()
        );

    }

    @Test
    public void usuarioControllerCriarUsuarioRetornarUsuario() throws Exception{
        given(usuarioService.criarUsuario(ArgumentMatchers.any(UsuarioDto.class))).willReturn(usuario);

        ResultActions resposta = mockMvc.perform(post("/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioDto)));

        resposta.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome", CoreMatchers.is(usuarioDto.nome())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(usuarioDto.email())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataCadastro", CoreMatchers.is(usuarioDto.dataCadastro().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.telefone", CoreMatchers.is(usuarioDto.telefone())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void listarTodosUsuarios() throws Exception {
        when(usuarioService.listarUsuario()).thenReturn(usuarios);

        ResultActions resposta = mockMvc.perform(get("/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarios)));

        resposta.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()",CoreMatchers.is(usuarios.size())));
    }

    @Test
    public void encontrarUsuarioPorId() throws Exception{
        when(usuarioService.encontrarPorId(1)).thenReturn(Optional.of(usuario));

        ResultActions resposta = mockMvc.perform(get("/usuario/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarios)));

        resposta.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome", CoreMatchers.is(usuarioDto.nome())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(usuarioDto.email())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataCadastro", CoreMatchers.is(usuarioDto.dataCadastro().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.telefone", CoreMatchers.is(usuarioDto.telefone())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void atualizarUsuario() throws Exception {
        given(usuarioService.atualizarUsuario(ArgumentMatchers.any(UsuarioDto.class), ArgumentMatchers.eq(1))).willReturn(usuario);

        ResultActions resposta = mockMvc.perform(MockMvcRequestBuilders.put("/usuario/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioDto)));

        resposta.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome", CoreMatchers.is(usuarioDto.nome())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(usuarioDto.email())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataCadastro", CoreMatchers.is(usuarioDto.dataCadastro().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.telefone", CoreMatchers.is(usuarioDto.telefone())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void apagarUsuario() throws Exception {
        doNothing().when(usuarioService).deletarUsuario(1);

        ResultActions resposta = mockMvc.perform(delete("/usuario/1")
                .contentType(MediaType.APPLICATION_JSON));

        resposta.andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
