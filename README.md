# 📌 Sistema de Agendamento, Histórico e Notificação

Este projeto é um ecossistema de microserviços desenvolvido em **Java 21** com **Spring Boot 4**, focado em agendamento de consultas, histórico clínico e notificações, utilizando arquitetura moderna, segurança com JWT e comunicação assíncrona via Kafka.

---

## 🧩 Visão Geral da Arquitetura

O sistema é composto por três microserviços principais:

### 🟢 1. Agendamento Service
**Porta:** `8080`

**Responsável por:**
- Cadastro e autenticação de usuários
- Criação e edição de consultas
- Login com Spring Security + JWT
- Emissão de tokens JWT
- Publicação de eventos de consulta no Kafka

**Banco de dados:** MongoDB (MongoDB Atlas)

---

### 🔵 2. Histórico Service
**Porta:** `8081` (GraphQL)  
**Porta de Debug:** `5006`

**Responsável por:**
- Consumir eventos do Kafka
- Manter o histórico de consultas
- Expor consultas via GraphQL
- Proteger acessos com JWT Resource Server
- Validar permissões por perfil (roles)

**Banco de dados:** PostgreSQL (porta `5433`)

---

### 🟣 3. Notificação Service
**Porta de Debug:** `5005`

**Responsável por:**
- Consumir eventos do Kafka
- Enviar notificações (email via SMTP)
- Persistir dados relacionados a notificações

**Banco de dados:** MySQL (porta `3307`)

---

## 🔐 Segurança

### 🔑 Autenticação
- Baseada em **Spring Security**
- Login realizado via `AuthenticationManager`
- Usuário implementa `UserDetails`
- Estado do usuário (`ativo/inativo`) controlado por `isEnabled()`

### 🎟️ JWT
- Tokens assinados com **RSA** (chave pública/privada)
- Claims principais:
    - `sub`: id do usuário
    - `roles`: autoridades do usuário
    - `iss`: `Agendamento-service`

**Exemplo de geração de token:**
```java
JwtClaimsSet.builder()
    .issuer("Agendamento-service")
    .subject(user.getId().toString())
    .claim("roles", user.getAuthorities())
    .issuedAt(now)
    .expiresAt(now.plusSeconds(300))
    .build();
```

### ✅ Validação automática do token
O `JwtDecoder` valida:
- Assinatura (chave pública)
- Expiração
- Issuer (`Agendamento-service`)

---

## 👥 Regras de Acesso (Histórico)

| Perfil     | Permissões                                    |
|------------|-----------------------------------------------|
| Médico     | Visualizar e editar histórico de consultas    |
| Enfermeiro | Cria consultas e visualiza histórico          |
| Paciente   | Visualizar apenas suas próprias consultas     |

As regras são aplicadas via:
- `@PreAuthorize`
- Claims do JWT
- `Authentication.getPrincipal()`

---

## 📡 Comunicação entre Serviços

### Kafka
**Porta:** `9092` (externa) / `29092` (interna)  
**Kafdrop (UI):** `9000`

- Utilizado para comunicação assíncrona
- Exemplo de evento:
    - `consultation-notification`

---

## 🧪 Testes

### Login
- Testes unitários simulam autenticação
- `AuthenticationManager` mockado
- `TokenService` mockado

### Segurança
- Testes de acesso por role
- JWT real testado via Postman

---

## 🧪 Testando com Postman

1. Faça login no **Agendamento Service** (`http://localhost:8080`)
2. Copie o `accessToken`
3. No request protegido:
```
Authorization: Bearer <token>
```

### GraphQL (Histórico Service)
**Endpoint:**
```
POST http://localhost:8081/graphql
```

## 🐳 Docker

### Portas dos Serviços
| Serviço              | Porta(s)           |
|----------------------|--------------------|
| Agendamento Service  | `8080`             |
| Histórico Service    | `8081`, `5006`     |
| Notificação Service  | `5005`             |
| MySQL                | `3307`             |
| PostgreSQL           | `5433`             |
| Kafka                | `9092`             |
| Kafdrop              | `9000`             |

### Configuração
- Cada serviço roda em container próprio
- Kafka configurado com `advertised.listeners`
- Comunicação interna via nome do container
- Network: `app-network`

---

## 📦 Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 4.0.1**
- **Spring Security**
- **OAuth2 Resource Server**
- **JWT (Nimbus)**
- **GraphQL**
- **Apache Kafka 4.1.1**
- **Docker / Docker Compose**
- **MySQL 8.0**
- **PostgreSQL 16**
- **MongoDB Atlas**
- **JPA / Hibernate**
- **Lombok**
- **JavaMailSender (SMTP)**

---

## 🎯 Status do Projeto

✔️ Arquitetura definida  
✔️ Segurança implementada  
✔️ Comunicação entre serviços funcional  
✔️ Kafka operacional  
✔️ GraphQL com autenticação JWT  
✔️ Sistema de notificações por email


---
## 🚀 Como Executar

1. Clone o repositório
2. Configure as variáveis de ambiente no arquivo `.env`
3. Execute o Docker Compose:
```bash
docker compose up --build
```

4. Acesse os serviços:
    - Agendamento: `http://localhost:8080`
    - Histórico (GraphQL): `http://localhost:8081/graphql`
    - Kafdrop: `http://localhost:9000`
   
---

## 👩‍💻 Autora

**Aira Soares**

Projeto acadêmico e profissional com foco em arquitetura, segurança e boas práticas em sistemas distribuídos.

---
