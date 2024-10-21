# Desafio API

## Descrição
Esta é uma API desenvolvida em Java 17 com Spring Boot para gerenciar uma biblioteca. A API permite o cadastro de livros, usuários e empréstimos, além de fornecer recomendações de livros com base na categoria. O sistema utiliza um banco de dados relacional PostgreSQL e um banco de dados H2 para testes. Os testes são realizados utilizando JUnit e Mockito.

## Tecnologias
- **Java 17**
- **Spring Boot**
- **Maven**
- **PostgreSQL**
- **H2 Database (para testes)**
- **JUnit**
- **Mockito**

## Pré-requisitos
- Java 17.0.12 instalado.
- Maven instalado.
- PostgreSQL instalado e em execução com usuário e senha criado.

## IDE
- Intellij IDEA 2024.2.3

## Configuração do Banco de Dados
É necessário criar um banco de dados chamado 'biblioteca' no PostgreSQL.

### Configuração do `application.properties`
Configure o arquivo `src/main/resources/application.properties` com as seguintes informações:

```properties
spring.application.name=desafio
spring.datasource.url=jdbc:postgresql://localhost:5432/biblioteca
spring.datasource.username=seu_nome_de_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
google_books_api_key=sua_chave_da_api_google_books
```

# Endpoints

A API possui os seguintes endpoints:

Usuários

    POST /usuario: Criar um novo usuário.
    GET /usuario: Listar todos os usuários.
    GET /usuario/{id}: Encontrar um usuário pelo ID.
    PUT /usuario/{id}: Atualizar um usuário pelo ID.
    DELETE /usuario/{id}: Apagar um usuário pelo ID.

Livros

    POST /livro: Criar um novo livro.
    GET /livro: Listar todos os livros.
    GET /livro/{id}: Encontrar um livro pelo ID.
    PUT /livro/{id}: Atualizar um livro pelo ID.
    DELETE /livro/{id}: Apagar um livro pelo ID.
    GET /livro/buscarLivroGoogle/{titulo}: Busca um livro pelo título fornecido.
    POST /livro/adicionarLivroGoogle/{titulo}/{id}: Adiciona um livro ao sistema com base no título e ID do livro na API
    da Google fornecidos.

Empréstimos

    POST /emprestimo: Criar um novo empréstimo.
    GET /emprestimo: Listar todos os empréstimos.
    GET /emprestimo/{id}: Encontrar um empréstimo pelo ID.
    PUT /emprestimo/{id}: Atualizar um empréstimo pelo ID.
    GET /emprestimo/recomendarLivro/{id}: Recomendar livros baseados nos livros já emprestados pelo usuário.

https://app.swaggerhub.com/apis/LUUCASGARCIADOSSANTO/desafio-api/1.0.0

