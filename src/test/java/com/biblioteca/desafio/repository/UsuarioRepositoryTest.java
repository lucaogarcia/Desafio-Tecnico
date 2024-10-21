package com.biblioteca.desafio.repository;


import com.biblioteca.desafio.model.Usuario;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void salvarUsuarioRepositoryRetornarUsuario(){

        Usuario usuario = Usuario.builder()
                .nome("Jose Almeida")
                .email("jose@gmail.com")
                .dataCadastro(LocalDate.parse("2023-06-05"))
                .telefone("44987612346").build();

        Usuario usuario1 = usuarioRepository.save(usuario);

        Assertions.assertThat(usuario1).isNotNull();
        Assertions.assertThat(usuario1.getId()).isGreaterThan(0);
    }

    @Test
    public void salvarUsuarioRepositoryRetornarListaUsuarios(){
        Usuario usuario1 = Usuario.builder()
                .nome("Jose Almeida")
                .email("jose@gmail.com")
                .dataCadastro(LocalDate.parse("2023-06-05"))
                .telefone("44987612346").build();

        Usuario usuario2 = Usuario.builder()
                .nome("Carlos Nobrega")
                .email("nobrega23@gmail.com")
                .dataCadastro(LocalDate.parse("2024-08-23"))
                .telefone("44989073421").build();

        usuarioRepository.save(usuario1);
        usuarioRepository.save(usuario2);

        List<Usuario> listaUsuario = usuarioRepository.findAll();

        Assertions.assertThat(listaUsuario).isNotNull();
        Assertions.assertThat(listaUsuario.size()).isEqualTo(2);
    }

    @Test
    public void encontreUsuarioRepositoryPorId(){
        Usuario usuario = Usuario.builder()
                .nome("Jose Almeida")
                .email("jose@gmail.com")
                .dataCadastro(LocalDate.parse("2023-06-05"))
                .telefone("44987612346").build();

        usuarioRepository.save(usuario);

        Optional<Usuario> usuario1 = usuarioRepository.findById(usuario.getId());

        Assertions.assertThat(usuario1).isNotNull();
    }

    @Test
    public void emailIncorretoUsuarioENulo(){

        Usuario usuario = Usuario.builder()
                .nome("Jose Almeida")
                .email("jose@.2gmail.com")
                .dataCadastro(LocalDate.parse("2023-06-05"))
                .telefone("44987612346").build();
        try {
            usuarioRepository.save(usuario);
            fail();
        }catch (Exception e){
            assertEquals("Validation failed for classes [com.biblioteca.desafio.model.Usuario] during persist time for groups [jakarta.validation.groups.Default, ]\n" +
                    "List of constraint violations:[\n" +
                    "\tConstraintViolationImpl{interpolatedMessage='Email inválido', propertyPath=email, rootBeanClass=class com.biblioteca.desafio.model.Usuario, messageTemplate='Email inválido'}\n" +
                    "]",e.getMessage());
            Assertions.assertThat((Usuario) null).isNull();
        }
    }

    @Test
    public void usuarioAtualiza(){
        Usuario usuario = Usuario.builder()
                .nome("Jose Almeida")
                .email("jose@gmail.com")
                .dataCadastro(LocalDate.parse("2023-06-05"))
                .telefone("44987612346").build();

        usuarioRepository.save(usuario);

        Optional<Usuario> usuarioSalvo = usuarioRepository.findById(usuario.getId());

        Usuario usuarioAtualizado = usuarioSalvo.get();
        usuarioAtualizado.setNome("Jose Almeida dos Santos");
        usuarioAtualizado.setEmail("jose@gmail.com");
        usuarioAtualizado.setDataCadastro(LocalDate.parse("2023-06-05"));
        usuarioAtualizado.setTelefone("44999845678");

        usuarioRepository.save(usuarioAtualizado);

        Assertions.assertThat(usuarioAtualizado.getNome()).isNotNull();
        Assertions.assertThat(usuarioAtualizado.getTelefone()).isNotNull();
    }

    @Test
    public void DeletarUsuario(){
        Usuario usuario = Usuario.builder()
                .nome("Jose Almeida")
                .email("jose@gmail.com")
                .dataCadastro(LocalDate.parse("2023-06-05"))
                .telefone("44987612346").build();

        usuarioRepository.save(usuario);

        usuarioRepository.deleteById(usuario.getId());
        Optional<Usuario> usuarioRetorno = usuarioRepository.findById(usuario.getId());

        Assertions.assertThat(usuarioRetorno).isEmpty();
    }

}
