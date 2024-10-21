package com.biblioteca.desafio.service;

import com.biblioteca.desafio.model.Livro;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

@Service
public class GoogleApiService {

    @Value("${google_books_api_key}")
    private String apiKey;

    public String buscarLivroGoogle(String titulo) throws IOException, InterruptedException {
        String enderecoApi = "https://www.googleapis.com/books/v1/volumes?q=" + titulo + "&key=" + apiKey;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(enderecoApi)).build();
        HttpResponse<String> resposta = client.send(request, HttpResponse.BodyHandlers.ofString());
        return resposta.body();
    }

    public Livro parseGoogleBooksResponse(String respostaBody, String id) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(respostaBody);
        JsonNode livros = root.path("items");
        JsonNode livroInfo = null;

        if (!livros.isEmpty()) {
            for (JsonNode livro : livros) {
                if (livro.path("id").asText().equals(id)) {
                    livroInfo = livro.path("volumeInfo");
                    break;
                }
            }
            if (livroInfo == null) {
                throw new IllegalArgumentException("Não foi encontrado nenhum livro com esse ID.");
            }

            Livro livro = new Livro();
            livro.setTitulo(livroInfo.path("title").asText());
            livro.setAutor(livroInfo.path("authors").get(0).asText());
            livro.setIsbn(livroInfo.path("industryIdentifiers").get(0).path("identifier").asText());
            livro.setDataPublicacao(LocalDate.parse(livroInfo.path("publishedDate").asText()));
            livro.setCategoria(livroInfo.path("categories").get(0).asText());

            return livro;
        } else {
            throw new IllegalArgumentException("Não foi encontrado nenhum livro.");
        }

    }
}