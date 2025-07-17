# 📽️ Cooperfilme - Sistema de Análise de Roteiros

Sistema desenvolvido para gerenciar o fluxo de análise, revisão e aprovação de roteiros submetidos por clientes, seguindo uma arquitetura hexagonal com autenticação baseada em JWT.

## 🚀 Tecnologias Utilizadas

- Java 21
- Spring Boot
- Spring Security
- Maven ou Gradle
- Lombok
- JWT (Autenticação)
- Arquitetura Hexagonal (Ports and Adapters)

## 📦 Estrutura de Pacotes

```text
com.cooperfilme
├── adapters
│   ├── in
│   │   └── web
│   └── out
│       └── persistence
├── application
│   ├── port
│   │   ├── in
│   │   └── out
│   └── service
├── domain
│   └── model
└── config
```

## 🛠️ Como Executar

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/cooperfilme.git
cd cooperfilme
```

### 2. Execute a aplicação

**Com Maven:**

```bash
./mvnw spring-boot:run
```

**Com Gradle:**

```bash
./gradlew bootRun
```

### 3. Acesse a aplicação

```arduino
http://localhost:8080
```

## 🔐 Autenticação

A autenticação é feita via JWT.

### 1. Login

**Endpoint:**

```pgsql
POST /auth/login
Content-Type: application/json
```

**Body:**

```json
{
  "email": "analista@cooperfilme.com",
  "senha": "senha123"
}
```

### 2. Enviar o token nas próximas requisições

```makefile
Authorization: Bearer <seu_token_aqui>
```

## 📡 Endpoints

### 🟢 Rotas Públicas

- `POST /auth/login` — Login do usuário e obtenção do token
- `POST /roteiros` — Envio de roteiro
- `GET /roteiros/{id}/status` — Consulta de status do roteiro

### 🔒 Rotas Protegidas (JWT)

Requerem token JWT. Os cargos determinam o acesso às rotas.

- `GET /roteiros/{id}` — Consulta dados completos do roteiro
- `GET /roteiros` — Lista todos os roteiros
- `POST /roteiros/{id}/mudar-status` — Avança o status do roteiro
- `POST /roteiros/{id}/votar` — Aprovadores votam

## 🔄 Fluxo de Status do Roteiro

| Status Atual           | Ação Necessária             | Próximo Status               |
|------------------------|-----------------------------|------------------------------|
| aguardando_analise     | Analista inicia análise     | em_analise                   |
| em_analise             | Analista finaliza análise   | aguardando_revisao ou recusado |
| aguardando_revisao     | Revisor inicia revisão      | em_revisao                   |
| em_revisao             | Revisor finaliza revisão    | aguardando_aprovacao         |
| aguardando_aprovacao   | Aprovadores iniciam votação | em_aprovacao ou recusado     |
| em_aprovacao           | Após votação unânime        | aprovado ou recusado         |
| aprovado / recusado    | Fim do fluxo                | —                            |

❗ Qualquer desvio fora dessas regras causará erro.

## ✅ Exemplo de Envio de Roteiro

**Endpoint:**

```bash
POST /roteiros
Content-Type: application/json
```

**Body:**

```json
{
  "titulo": "Aventura no Sertão",
  "descricao": "Um épico nordestino moderno.",
  "conteudo": "Texto do roteiro aqui...",
  "cliente": {
    "nome": "Maria Oliveira",
    "email": "maria@email.com",
    "telefone": "11999999999"
  }
}
```

## 💡 Dica

Use o Postman ou Insomnia para testar os endpoints com facilidade.
