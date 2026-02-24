# TaskManagerAPI

RESTful API for task and user management built with Spring Boot, providing a simple and efficient way to create, organize, and track tasks associated with users.

## Features

###  Users

* Create new users
* Retrieve all users stored in the database
* Fetch a specific user by ID
* Update user information
* Delete users and their related tasks

###  Tasks

* Create new tasks linked to users
* Retrieve all tasks stored in the database
* Fetch a specific task by ID
* Filter tasks by completion status (completed or pending)
* Mark tasks as completed or update their completion status
* Update existing task details
* Delete tasks



## Technologies
![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)

![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)

![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?style=for-the-badge&logo=postgresql&logoColor=white)

![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)


## Users endpoints

| Method | Endpoint | Description |
|-------|----------|------------|
| GET | /users | List all users |
| GET | /users/{id} | Get user by ID |
| POST | /users | Create user |
| PATCH | /users/{id} | Update user |
| DELETE | /users/{id} | Delete user and related tasks |

## Tasks endpoints

| Method | Endpoint | Description |
|-------|----------|------------|
| GET | /tasks | List all tasks |
| GET | /tasks/{id} | Get task by ID |
| GET | /tasks/search | Filter tasks by completion status |
| POST | /tasks | Create task |
| PATCH | /tasks/{id} | Update task |
| PATCH | /tasks/{id}/complete | Mark task as completed |
| DELETE | /tasks/{id} | Delete task |


## How use endpoints with body parameters
###  Create user 

- **POST /users**

```json
{
  "name": "NameOfUser",
  "age": 20,
  "email": "yourEmail@gmail.com",
  "password": "@andNumbers123"
}
```

⚠️ password is stored without hash (temporary until Spring Security implementation)

###  Create Tasks
- **POST /tasks**

```json
{
  "title": "Example title",
  "content": "Example description",
  "userId": 1
}
```

### Update Users 

- **PATCH /users/{id}**
```json
{
  "name": "NameOfUser",     // optional
  "age": 20,                  // optional
  "email": "yourEmail@gmail.com" // optional
}
```

### Update Tasks

- **PATCH /tasks/{id}**
```json
{
  "title": "Example title",       // optional
  "content": "Example description" // optional
}
```
Only provided fields are updated

##  How to run the project

###  Prerequisites
- Java 21
- Maven
- PostgreSQL
### 1. Clone the repository
```bash
git clone https://github.com/Victor-Policarpo/task-manager.git
cd task-manager
```
### 2. Configure environment variables
Configure the environment variables according to the file `.env.example`:
  ```
DB_URL=jdbc:postgresql://localhost:5432/your_database
DB_USER=your_postgres_user
DB_PASSWORD=your_password_here
```
### 3. Create the database
PostgreSQL, create database:

```CREATE DATABASE your_database;```

### 4. Run the application
Execute commands:

```mvn spring-boot:run```

The application will be available at:

```http://localhost:8080```
##  Author

Victor Policarpo
- GitHub: [Victor-Policarpo](https://github.com/Victor-Policarpo)
- LinkedIn: [VictorPolicarpo](https://www.linkedin.com/in/victor-policarpo-dev/)

##  Licença

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.
