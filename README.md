# TaskManagerAPI

API REST para gerenciamento de tarefas desenvolvida com Spring Boot.
## Funcionalidades
- Criar uma tarefa
- Listar todas as tarefas salvas no banco de dados
- Buscar tarefa por ID
- Filtrar tarefas por status (concluída ou não concluída)
- Atualizar o status de conclusão de uma tarefa
- Atualizar uma tarefa existente
- Deletar uma tarefa

## Tecnologias
![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)

![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)

![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?style=for-the-badge&logo=postgresql&logoColor=white)

![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)


## Endpoints
| Método | Endpoint      | Descrição                    |
|------|-----------------|------------------------------|
| GET  | /task           | Listar todas as tarefas      |
| GET  | /task/{id}      | Buscar tarefa por ID         |
| GET  | /task/filter?complete=true    | Listar tarefas com o filtro de status|
| POST |/task/create-task| Criar uma nova tarefa        |
| PATCH| /task/{id}/complete| Atualizar o status de uma tarefa|
| PATCH| /task/{id}/update| Atualizar title/content de uma tarefa|
| DELETE | /task/{id}    | Deletar uma tarefa|



##  Como rodar o projeto

### Pré-requisitos
- Java 21
- Maven
- PostgreSQL
### 1. Clonar o repositório
```bash
git clone https://github.com/Victor-Policarpo/task-manager.git
cd task-manager
```
### 2. Configurar variáveis de ambiente
Configure as variáveis de ambiente conforme o arquivo `.env.example`:
  ```
DB_URL=jdbc:postgresql://localhost:5432/your_database
DB_USER=your_postgres_user
DB_PASSWORD=your_password_here
```
### 3. Criar o banco de dados
No PostgreSQL, crie o banco de dados:

```CREATE DATABASE your_database;```

### 4. Executar a aplicação
Execute o comando:

```mvn spring-boot:run```

A aplicação estará disponível em:

```http://localhost:8080```
##  Autor

Victor Policarpo
- GitHub: [Victor-Policarpo](https://github.com/Victor-Policarpo)
- LinkedIn: [VictorPolicarpo](https://www.linkedin.com/in/victor-policarpo-dev/)

##  Licença

Este projeto está licenciado sob a licença MIT. Consulte o arquivo [LICENSE](LICENSE).