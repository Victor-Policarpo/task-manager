# TaskManagerAPI

API REST para gerenciamento de tarefas desenvolvida com Spring Boot, com autenticação JWT, autorização por roles (USER/ADMIN) e isolamento de dados por proprietário.

## Funcionalidades

### Autenticação e Autorização
- Registro de usuários (`POST /auth/register`)
- Login com geração de JWT (`POST /auth/login`)
- Autenticação stateless via Bearer token
- Roles: `USER` e `ADMIN`

### Tasks (proprietário autenticado)
- Criar tarefa (proprietário definido automaticamente pelo JWT)
- Listar tarefas do usuário autenticado
- Buscar tarefa por ID (somente próprias)
- Filtrar tarefas por status (somente próprias)
- Atualizar tarefa (somente próprias)
- Concluir tarefa (somente próprias)
- Excluir tarefa (somente próprias)

### Perfil do Usuário (`/users/me`)
- Consultar próprio perfil (`GET /users/me`)
- Atualizar nome, idade e email (`PATCH /users/me`)
- Normalização de email (trim + lowercase)
- Verificação de duplicidade no email

### Administração (exclusivo ADMIN)
- Listar todos os usuários
- Consultar usuário por ID
- Atualizar usuário por ID
- Excluir usuário por ID

## Tecnologias

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)

![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)

![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)

![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?style=for-the-badge&logo=postgresql&logoColor=white)

![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)


## Arquitetura

```
HTTP Request
      |
JwtAuthenticationFilter   ← valida Bearer token, popula SecurityContext
      |
SecurityFilterChain        ← CSRF off, STATELESS, Authorization por role
      |
+-----+-----+-----+
|           |           |
/auth/*    /tasks/*    /users/*
(public)  (auth'd)    (auth'd / admin)
|           |           |
AuthCtrl   TaskCtrl   UserCtrl
|           |           |
AuthService TaskService UserService
|           |           |
           AuthenticatedUser  ← componente central de identificação
      |
UserRepository / TaskRepository
      |
  PostgreSQL
```

**Ownership:** As Tasks são isoladas por proprietário. O usuário autenticado no JWT é automaticamente o dono de suas Tasks. O cliente nunca informa o proprietário.

## Endpoints

### Autenticação (público)

| Método | Endpoint          | Descrição                        |
|--------|-------------------|----------------------------------|
| POST   | `/auth/register`  | Registrar novo usuário           |
| POST   | `/auth/login`     | Login e obter JWT                |

### Tasks (autenticado — somente próprias)

| Método | Endpoint              | Descrição                          |
|--------|-----------------------|------------------------------------|
| GET    | `/tasks`              | Listar tarefas do usuário          |
| POST   | `/tasks`              | Criar nova tarefa                  |
| GET    | `/tasks/{id}`         | Buscar tarefa por ID               |
| PATCH  | `/tasks/{id}`         | Atualizar title/content            |
| PATCH  | `/tasks/{id}/complete`| Marcar tarefa como concluída       |
| GET    | `/tasks/search?completed=true`| Filtrar por status          |
| DELETE | `/tasks/{id}`         | Excluir tarefa                     |

### Perfil (autenticado)

| Método | Endpoint    | Descrição                          |
|--------|-------------|------------------------------------|
| GET    | `/users/me` | Consultar perfil do usuário        |
| PATCH  | `/users/me` | Atualizar nome, idade ou email     |

### Administração (exclusivo ADMIN)

| Método | Endpoint        | Descrição                          |
|--------|-----------------|------------------------------------|
| GET    | `/users`        | Listar todos os usuários           |
| GET    | `/users/{id}`   | Consultar usuário por ID           |
| PATCH  | `/users/{id}`   | Atualizar usuário por ID           |
| DELETE | `/users/{id}`   | Excluir usuário por ID             |

## Regras de Autorização

| Endpoint               | USER | ADMIN |
|------------------------|------|-------|
| `/auth/**`             | ✅ público | ✅ público |
| `/tasks/**` (próprias) | ✅ | ✅ (só as suas) |
| `GET /users/me`        | ✅ | ✅ |
| `PATCH /users/me`      | ✅ | ✅ |
| `GET /users`           | ❌ 403 | ✅ |
| `GET /users/{id}`      | ❌ 403 | ✅ |
| `PATCH /users/{id}`    | ❌ 403 | ✅ |
| `DELETE /users/{id}`   | ❌ 403 | ✅ |

## Respostas de Erro

| HTTP Status | Quando                                |
|-------------|---------------------------------------|
| 400         | Validação de dados falhou             |
| 401         | JWT ausente, inválido ou expirado     |
| 403         | Usuário autenticado sem role necessária |
| 404         | Recurso não encontrado (ou pertence a outro usuário) |
| 409         | Email duplicado ou violação de integridade |

## Como Rodar o Projeto

###  Prerequisites
- Java 21
- Maven
- PostgreSQL

### 1. Clonar o repositório
```bash
git clone https://github.com/Victor-Policarpo/task-manager.git
cd task-manager
```

### 2. Configurar variáveis de ambiente
Copie o `.env.example` e configure:
```
DB_URL=jdbc:postgresql://localhost:5432/your_database
DB_USER=your_postgres_user
DB_PASSWORD=your_password_here
JWT_SECRET=REPLACE_WITH_A_BASE64_ENCODED_32_BYTE_OR_LONGER_SECRET
JWT_EXPIRATION=3600000
```

### 3. Criar o banco de dados
```sql
CREATE DATABASE your_database;
```

### 4. Executar a aplicação
```bash
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`.

## Testes

```bash
mvn test
```

O projeto possui 51 testes cobrindo:
- **Auth:** registro, login, normalização de email, hash de senha BCrypt
- **JWT:** geração de token, validade, expiração, filtro de autenticação
- **Ownership:** isolamento de Tasks por usuário, criação automática de proprietário, rejeição de `user_id` do cliente
- **Usuários:** perfil (`/users/me`), proteção de `passwordHash`
- **Admin:** endpoints administrativos e autorização por role
- **Segurança:** `401` sem JWT, `403` sem role necessária, `404` para recursos de outros usuários

## Estrutura do Projeto

```
src/main/java/com/victorpolicarpo/task_manager/
├── config/              # SecurityConfig, PasswordEncoderConfig
├── controllers/         # AuthController, TaskController, UserController
├── dto/
│   ├── auth/            # RegisterRequestDto, LoginRequestDto, AuthResponseDto
│   ├── task/            # TaskRequestDto, TaskUpdateDto, TaskResponseDto, TaskMinDto
│   └── user/            # UserRequestDto, UserUpdateDto, UserResponseDto, UserMinDto
├── exception/           # GlobalExceptionHandler, ResourceNotFoundException, ConflictException
├── mapper/              # TaskMapper, UserMapper (MapStruct)
├── model/               # User, Task, Role
├── repository/          # UserRepository, TaskRepository
├── security/            # JwtService, JwtAuthenticationFilter, AuthenticatedUser, CustomUserDetailsService
└── service/             # AuthService, TaskService, UserService
```

## Autor

Victor Policarpo
- GitHub: [Victor-Policarpo](https://github.com/Victor-Policarpo)
- LinkedIn: [VictorPolicarpo](https://www.linkedin.com/in/victor-policarpo-dev/)

## Licença

Este projeto está licenciado sob a licença MIT. Consulte o arquivo [LICENSE](LICENSE).
