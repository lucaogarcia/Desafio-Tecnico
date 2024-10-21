package com.biblioteca.desafio.service;

import com.biblioteca.desafio.dto.UsuarioDto;
import com.biblioteca.desafio.model.Usuario;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

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
    public void usuarioServiceCriaUsuarioRetornaUsuario() {
        when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

        Usuario usuarioSalvo = usuarioService.criarUsuario(usuarioDto);

        Assertions.assertThat(usuarioSalvo).isNotNull();
        Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("Jose Almeida");
        Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("jose@gmail.com");
        Assertions.assertThat(usuarioSalvo.getDataCadastro()).isEqualTo(LocalDate.parse("2023-06-05"));
        Assertions.assertThat(usuarioSalvo.getTelefone()).isEqualTo("44987612346");
    }

    @Test
    public void UsuarioServiceListaUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> usuarioSalvo = usuarioService.listarUsuario();

        Assertions.assertThat(usuarioSalvo).isNotNull();
        Assertions.assertThat(usuarioSalvo.size()).isEqualTo(2);
        Assertions.assertThat(usuarioSalvo).containsAll(usuarios);
    }

    @Test
    public void usuarioServiceEncontrarPorId() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        Optional<Usuario> usuarioSalvo = usuarioService.encontrarPorId(1);

        Assertions.assertThat(usuarioSalvo).isPresent();
        Assertions.assertThat(usuarioSalvo.get().getNome()).isEqualTo("Jose Almeida");
        Assertions.assertThat(usuarioSalvo.get().getEmail()).isEqualTo("jose@gmail.com");
        Assertions.assertThat(usuarioSalvo.get().getDataCadastro()).isEqualTo(LocalDate.parse("2023-06-05"));
        Assertions.assertThat(usuarioSalvo.get().getTelefone()).isEqualTo("44987612346");
    }

    @Test
    public void UsuarioAtualizado(){
        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nome("Jose Almeida dos Santos")
                .email("jose@gmail.com")
                .dataCadastro(LocalDate.parse("2023-06-05"))
                .telefone("44999845678").build();

        when(usuarioRepository.findById(1)).thenReturn(Optional.ofNullable(usuario));
        when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

        Usuario usuarioAtualizado = usuarioService.atualizarUsuario(usuarioDto,1);

        Assertions.assertThat(usuarioAtualizado).isNotNull();
        Assertions.assertThat(usuarioAtualizado.getNome()).isEqualTo("Jose Almeida dos Santos");
        Assertions.assertThat(usuarioAtualizado.getEmail()).isEqualTo("jose@gmail.com");
        Assertions.assertThat(usuarioAtualizado.getDataCadastro()).isEqualTo(LocalDate.parse("2023-06-05"));
        Assertions.assertThat(usuarioAtualizado.getTelefone()).isEqualTo("44999845678");
    }

    @Test
    public void DeletarUsuario(){
        assertAll(() -> usuarioService.deletarUsuario(1));
    }

    @Test
    public void DataCadastroIncorreta(){
        usuarioDto = UsuarioDto.builder()
                .nome("Jose Almeida")
                .email("jose@gmail.com")
                .dataCadastro(LocalDate.parse("2024-10-30"))
                .telefone("44987612346").build();
        try {
            usuarioService.criarUsuario(usuarioDto);
            fail();
        }catch (Exception e){
            assertEquals("A data de cadastro não pode ser posterior a hoje.",e.getMessage());
            Assertions.assertThat((Usuario) null).isNull();
        }
    }
}