# api-contrato

API desenvolvida para gerenciar contratos, eventos relacionados e partes envolvidas. A API utiliza o framework Spring Boot, com foco em escalabilidade, manutenção e boas práticas.
## API para gerenciamento de contratos:
### Contrato:
cadastro, atualização, alteração de status, consultas (por número, por data e por inscrição federal das partes envolvidas), arquivamento e desarquivamento de um ou mais contratos.
### Evento: 
cadastro, atualização, consultas (por número do contrato vinculado, por número e tipo do evento).
### Parte Envolvida:
cadastro (cadastro de parte envolvida no endpoint de cadastro de contrato), atualização.
## Índice

- [Tecnologias](#tecnologias)
- [Instalação](#instalação)
- [Endpoints](#endpoints)
- [Contrato](#contrato)
- [Evento](#evento)
- [Parte Envolvida](#parte-envolvida)
- [Exemplo de Requests](#exemplo-de-requisições)

## Tecnologias

- Java 17: Linguagem de programação principal.
- Spring Boot 3.3.8-SNAPSHOT: Framework para desenvolvimento de aplicações Java, com starters específicos:
- Spring Boot Starter Web: Para criação de APIs REST.
- Spring Boot Starter Data JPA: Para integração com bancos de dados utilizando JPA (Hibernate).
- Spring Boot Starter Validation: Para validação de dados com Java Bean Validation (Hibernate Validator).
- Spring Boot DevTools: Para suporte ao desenvolvimento com recarregamento automático.
- PostgreSQL: Banco de dados relacional.
- Hibernate: Implementação do JPA para mapeamento objeto-relacional.
- Maven: Gerenciador de dependências e build.
### Bibliotecas de Suporte
- Lombok: Geração de código boilerplate como getters, setters, construtores e mais.
- MapStruct: Para mapeamento automático entre objetos (DTOs e entidades).
- Java JWT (com.auth0:java-jwt): Para autenticação baseada em tokens JWT.
- Caelum Stella: Para validações específicas no contexto brasileiro (exemplo: CPF e CNPJ).
- SpringDoc OpenAPI: Geração automática de documentação da API usando Swagger.
### Ferramentas de Teste e Qualidade
- JUnit 4.13.2: Framework para criação de testes unitários.
- Mockito: Framework para criação de mocks nos testes.
- Jacoco: Para cobertura de código nos testes.
- SonarQube: Para análise estática de código e geração de métricas de qualidade.
### Plugins de Maven
- Spring Boot Maven Plugin: Para empacotamento e execução da aplicação.
- Maven Compiler Plugin: Para configuração do Java 17 como linguagem de compilação.
- Jacoco Maven Plugin: Para cobertura de código.
- Sonar Maven Plugin: Para integração com SonarQube.
### Outros
- JetBrains Annotations: Para suporte a anotações específicas, como @NotNull e @Nullable.


## Instalação

1. Clone o repositório:

   ```bash
   git clone git@github.com:kauanmeira/api-contrato.git
   cd api-contrato

## EndPoints
### Contrato
- Criar Contrato: POST /contrato
- Atualizar Contrato: PUT /contrato/atualizar/{numeroContrato}
- Atualizar Status do Contrato: PUT /contrato/atualizar-status/{numeroContrato}?statusContrato={status}
- Arquivar Contratos: PUT /contrato/arquivar?numerosContratos={id1,id2,...}
- Desarquivar Contratos: PUT /contrato/desarquivar?numerosContratos={id1,id2,...}
- Buscar Contrato por Número: GET /contrato/{numeroContrato}
- Buscar Contratos por Data de Criação: GET /contrato/buscar-por-data?dataCriacao={yyyy-MM-dd}
- Buscar Contratos por Inscrição Federal: GET /contrato/buscar-por-inscricao?inscricao={inscricao}
### Evento
- Cadastrar Evento: POST /evento
- Atualizar Evento: PUT /evento/atualizar/{id}
- Buscar Eventos por Número do Contrato: GET /evento/{numeroContrato}
- Buscar Eventos do Contrato por Tipo: GET /evento/por-tipo/{numeroContrato}?tipo={tipo}
### Parte Envolvida
- Atualizar Parte Envolvida: PUT /parte-envolvida/atualizar/{id}

## Exemplo de Requests
#### Contrato
```
POST /contrato
Content-Type: application/json
{
    "dataCriacao": "2024-12-31",
    "descricaoContrato": "o presente CONTRATO DE PRESTAÇÃO DE SERVIÇOS, que reger-se-á mediante as cláusulas e condições adiante estipuladas. CLÁUSULA PRIMEIRA - DO OBJETO.",
    "statusContrato": "ATIVO",
    "partesEnvolvidas": [
        {
            "inscricaoFederal": "46149592880",
            "nomeCompleto": "Valdinei Ferreira Alves",
            "dataNascimento": "2002-04-19",
            "tipoParte": "ADVOGADO",
            "telefone": "17988214036",
            "email": "valdinei.alves@gmail.com"
        },
        {
            "inscricaoFederal": "00.689.700/0001-35",
            "nomeCompleto": "Kauan Meira",
            "dataNascimento": "2002-04-19",
            "tipoParte": "ADVOGADO",
            "telefone": "17988214036",
            "email": "kauan.meira@gmail.com"
        },
        {
            "inscricaoFederal": "18149803858",
            "nomeCompleto": "Valdinei Ferreira Alves",
            "dataNascimento": "1982-03-20",
            "tipoParte": "CLIENTE",
            "telefone": "17988214036",
            "email": "valdinei.alves@gmail.com"
        },
        {
            "inscricaoFederal": "44859491840",
            "nomeCompleto": "Kauan Meira",
            "dataNascimento": "2002-04-19",
            "tipoParte": "FORNECEDOR",
            "telefone": "17988214036",
            "email": "kauan.meira@gmail.com"
        }
    ]
}
```
```
PUT /contrato/atualizar/{numeroContrato}
Content-Type: application/json

{
    "dataCriacao": "2024-12-31",
    "descricaoContrato": "Atualização descrição do contrato teste"
}

```
```
PUT /contrato/atualizar-status/{numeroContrato}?statusContrato={status}
exemplo: localhost:8080/contrato/atualizar-status/5?statusContrato=SUSPENSO
Content-Type: application/json
```
```
PUT /contrato/arquivar?numerosContratos={id1,id2,...}
exemplo: localhost:8080/contrato/arquivar?numerosContratos=6,7
Content-Type: application/json
```
```
PUT /contrato/desarquivar?numerosContratos={id1,id2,...}
exemplo: localhost:8080/contrato/desarquivar?numerosContratos=6,7
Content-Type: application/json
```
```
GET /contrato/{numeroContrato}
exemplo: localhost:8080/contrato/4
Content-Type: application/json
```
```
GET /contrato/{numeroContrato}
exemplo: localhost:8080/contrato/4
Content-Type: application/json
```
```
GET /contrato/buscar-por-data?dataCriacao={yyyy-MM-dd}
exemplo: localhost:8080/contrato/buscar-por-data?dataCriacao=2024-12-20
Content-Type: application/json
```
```
GET /contrato/buscar-por-inscricao?inscricao={inscricao}
exemplo: localhost:8080/contrato/buscar-por-inscricao?inscricao=46149592880
Content-Type: application/json
```
#### Evento:
```
POST /evento
Content-Type: application/json
{
  "tipoEvento": "ASSINATURA",
  "dataRegistro": "2024-12-26",
  "descricaoEvento": "Descrição teste de evento vinculado a um contrato",
  "numeroContrato": 4
}
```
```
PUT /evento/atualizar/{id}
Content-Type: application/json
{
  "tipoEvento": "RESCISAO",
  "dataRegistro": "2024-12-31",
  "descricaoEvento": "Descrição teste de evento vinculado a um contrato"
}
```
```
GET /evento/{numeroContrato}
exemplo: localhost:8080/evento/1
Content-Type: application/json
```
```
GET /evento/por-tipo/{numeroContrato}?tipo={tipo}
exemplo: http://localhost:8080/evento/por-tipo/5?tipo=RESCISAO
Content-Type: application/json
```
#### Parte Envolvida
```
PUT /parte-envolvida/atualizar/{id}
Content-Type: application/json
{
  "inscricaoFederal": "46149592880",
  "nomeCompleto": "Teste Atualização",
  "dataNascimento": "1980-12-31",
  "tipoParte": "CLIENTE",
  "telefone": "17988214036",
  "email": "kauan@gmail.com"
}
```
