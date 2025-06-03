# Eliza 2.0 - Psicóloga Virtual

## Descrição

Eliza é uma API RESTful desenvolvida em Java com Spring Boot, concebida como um MVP para servir como psicóloga virtual empática e acessível, especialmente voltada ao suporte psicológico de pessoas em situação de vulnerabilidade após desastres, catástrofes naturais ou grandes crises sociais.

A solução integra a inteligência artificial do Google Gemini, permitindo conversas humanizadas, acolhedoras e responsivas, capazes de entender o contexto emocional do usuário e oferecer orientações ou encaminhamentos de forma segura e ética. A IA foi treinada para compreender demandas sensíveis e proporcionar suporte inicial até que o indivíduo possa receber acompanhamento especializado.

O projeto utiliza autenticação JWT para garantir a segurança e privacidade dos dados dos usuários e persiste as interações em banco de dados PostgreSQL, viabilizando histórico de conversas e acompanhamento contínuo.

Como MVP, Eliza foi projetada para ser facilmente expandida no futuro, permitindo integrações com outros serviços de assistência social, plataformas de telemedicina, equipes de resposta a emergências e órgãos de saúde pública. A API pode ser incorporada a aplicativos móveis, portais de atendimento emergencial e sistemas de monitoramento pós-crise, promovendo cuidado psicológico imediato e humanizado em larga escala.
## Stacks Utilizadas

- **Java 21**
- **Spring Boot 3.5**
    - Spring Web
    - Spring Data JPA
    - Spring Security
    - Spring Validation
- **PostgreSQL**
- **JWT (JSON Web Token)**
- **Google Gemini API**
- **Lombok**
- **Maven**
- **Swagger/OpenAPI** (documentação automática)

## Como rodar

1. Configure o banco PostgreSQL e ajuste o arquivo `src/main/resources/application.properties` conforme necessário.
2. Execute o projeto com:
   ```
   ./mvnw spring-boot:run
   ```
3. Acesse a documentação Swagger em:  
   [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Endpoints Principais

### 1. Cadastro de Usuário

**POST** `/api/auth/register`

**Request:**
```json
{
  "nome": "Maria Silva",
  "email": "maria@email.com",
  "senha": "minhasenha123"
}
```

**Response (201):**
```json
"Usuário cadastrado com sucesso!"
```

**Response (403):**
```json
"E-mail já cadastrado ou inválido."
```

---

### 2. Login

**POST** `/api/auth/login`

**Request:**
```json
{
  "email": "maria@email.com",
  "senha": "minhasenha123"
}
```

**Response (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**Response (403):**
```json
"Email ou senha inválidos."
```

---

### 3. Enviar Mensagem ao Bot

**POST** `/api/chat`

**Headers:**
```
Authorization: Bearer {token}
```

**Request:**
```json
{
  "texto": "Estou me sentindo ansiosa ultimamente."
}
```

**Response (200):**
```json
{
  "resposta": "Olá, entendo como a ansiedade pode ser difícil. Quer conversar mais sobre o que está sentindo?"
}
```

---

### 4. Buscar Histórico de Mensagens

**GET** `/api/chat/historico?page=0&size=10`

**Headers:**
```
Authorization: Bearer {token}
```

**Response (200):**
```json
{
  "content": [
    {
      "textoUsuario": "Estou me sentindo ansiosa ultimamente.",
      "respostaBot": "Olá, entendo como a ansiedade pode ser difícil. Quer conversar mais sobre o que está sentindo?",
      "dataHora": "2024-06-10T15:30:00"
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "size": 10,
  "number": 0
}
```

---

### 5. Deletar Mensagem

**DELETE** `/api/chat/mensagem/{id}/deletar`

**Headers:**
```
Authorization: Bearer {token}
```

**Response (200):**
```json
"Deletado com sucesso"
```

**Response (403):**
```json
"Sem permissão para deletar essa mensagem"
```

---

### 6. Atualizar Mensagem

**PUT** `/api/chat/mensagem/{id}/update`

**Headers:**
```
Authorization: Bearer {token}
```

**Request:**
```json
{
  "texto": "Atualizando minha mensagem anterior."
}
```

**Response (200):**
```json
"Mensagem alterada para: Atualizando minha mensagem anterior."
```

---

## Observações

- Todas as rotas (exceto `/api/auth/**` e `/swagger-ui/**`) exigem autenticação via JWT.
- O projeto utiliza validação de dados e tratamento de erros.
- A integração com a IA Gemini pode ser customizada conforme necessidade.

---

## Licença

Este projeto é apenas para fins acadêmicos.

---