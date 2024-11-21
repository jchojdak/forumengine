# ForumEngine
* [About](#about)
* [Technology stack](#technology-stack)
* * [Backend](#backend)
* * [Database](#database)
* * [Testing](#testing)
* * [Containerization](#containerization)
* * [CI/CD](#cicd)
* [API documentation](#api-documentation)
* * [Swagger UI](#swagger-ui)
* * [Postman](#postman)
* [Starting up](#starting-up)
* [Endpoints](#endpoints)
* * [Security](#security)

## About
This REST API application built using Spring Boot and Java allows you to manage an online forum.

## Technology stack
### Backend
* Java 17
* Spring Boot 3.3.2 
* Spring Data JPA
* Spring Security
* Lombok

### Database
* MySQL 8.0
* Liquibase

### Testing
Unit & integration tests
* JUnit 5
* Mockito
* SpringBootTest
* Testcontainers

### Containerization
* Docker
* Two containers are configured in the `docker-compose.yml` file:
  - **mysql_db**: MySQL 8.0 database
  - **backend**: Java 17 (Spring Boot) application

### CI/CD
This repository uses **GitHub Actions** for automatic building and testing on every `push` and `pull request` to the `master` branch.

## API documentation
### Swagger UI

The application generates interactive API documentation Swagger UI (SpringDoc OpenAPI).
```
http://localhost:8080/swagger-ui/index.html
```

### Postman

Collection to import:
```
https://github.com/jchojdak/forumengine/blob/master/postman_collection/forumengine.json
```

## Starting up
To launch the application, follow the steps:
1. Clone project
```
git clone https://github.com/jchojdak/ForumEngine.git
```
2. Open cloned directory
```
cd ForumEngine
```
3. Start the application using docker-compose
```
docker-compose up -d
```
4. Application address
```
http://localhost:8080
```
5. Swagger
```
http://localhost:8080/swagger-ui/index.html
```

6. Default admin account
```
username: admin
password: admin
```

## Endpoints

| #  | Method | Endpoint                               | Description                               | Authorization             |
|----|--------|----------------------------------------|-------------------------------------------|---------------------------|
| 1  | POST   | `/auth/register`                       | User registration                         | No                        |
| 2  | POST   | `/auth/login`                          | User login                                | No                        |
| 3  | PATCH  | `/auth/password`                       | Change the password of the logged in user | Yes (authenticated user)  |
| 4  | GET    | `/categories`                          | Get all categories                        | No                        |
| 5  | POST   | `/categories`                          | Add a new category                        | Yes (admin)               |
| 6  | GET    | `/categories/{id}`                     | Get category by ID                        | No                        |
| 7  | DELETE | `/categories/{id}`                     | Delete category by ID                     | Yes (admin)               |
| 8  | POST   | `/posts`                               | Add a new post                            | Yes (authenticated user)  |
| 9  | GET    | `/posts`                               | Get all posts                             | No                        |
| 10 | GET    | `/posts/{id}`                          | Get post by ID                            | No                        |
| 11 | DELETE | `/posts/{id}`                          | Delete post by ID                         | Yes (authenticated user)  |
| 12 | PATCH  | `/posts/{id}`                          | Update post by ID                         | Yes (authenticated user)  |
| 13 | POST   | `/posts/{postId}/comments`             | Add a new comment to the post             | Yes (authenticated user)  |
| 14 | GET    | `/posts/{postId}/comments`             | Get all comments                          | No                        |
| 15 | DELETE | `/posts/{postId}/comments/{commentId}` | Delete comment by ID                      | Yes (authenticated user)  |
| 16 | GET    | `/posts/{postId}/comments/{commentId}` | Get comment by ID                         | No                        |
| 17 | GET    | `/users/{userId}`                      | Get user by ID                            | No                        |
| 18 | DELETE | `/users/{userId}`                      | Delete user by ID                         | Yes (admin)               |

### Security
To access some resources you need to use `header` authorization.

```
Authorization: Bearer <token>
```

The token can be generated using POST `/login` endpoint.

### 1. User registration

**Endpoint:** `/auth/register`

**Method:** `POST`

**Request body:**

```json
{
  "username": "example",
  "password": "ExamplePassword",
  "email": "example.example@gmail.com"
}
```

### 2. User login

**Endpoint:** `/auth/login`

**Method:** `POST`

**Request body:**

```json
{
  "username": "example",
  "password": "ExamplePassword"
}
```

### 3. Change the password of the logged in user

**Endpoint:** `/auth/password`

**Method:** `PATCH`

**Request body:**

```json
{
  "oldPassword": "oldPassword123",
  "newPassword": "newPassword123"
}
```

### 4. Get all categories

**Endpoint:** `/categories`

**Method:** `GET`

### 5. Add a new category

**Endpoint:** `/categories`

**Method:** `POST`

**Request body:**

```json
{
  "name": "exampleNameOfCategory",
  "description": "exampleDescription"
}
```

### 6. Get category by ID

**Endpoint:** `/categories/{id}`

**Method:** `GET`

### 7. Delete category by ID

**Endpoint:** `/categories/{id}`

**Method:** `DELETE`

### 8. Add a new post

**Endpoint:** `/posts`

**Method:** `POST`

**Request body:**

```json
{
  "categoryId": 1,
  "title": "Example post title",
  "content": "Example post content"
}
```

### 9. Get all posts

**Endpoint:** `/posts`

**Method:** `GET`

**Request parameters:**
- `page` (Integer, optional): The page number to retrieve. Default value is 0.
- `size` (Integer, optional): The number of posts per page. Default value is 10.
- `sort` (Sort.Direction, optional): The sorting direction. Default value is ASC.

*Example:* `/posts?page=3&size=20&sort=DESC`

### 10. Get post by ID

**Endpoint:** `/posts/{id}`

**Method:** `GET`

### 11. Delete post by ID

**Endpoint:** `/posts/{id}`

**Method:** `DELETE`

### 12. Updated post by ID

**Endpoint:** `/posts/{id}`

**Method:** `PATCH`

**Request body:**

```json
{
  "title": "Example new title",
  "content": "Example new content"
}
```

### 13. Add a new comment to the post

**Endpoint:** `/posts/{postId}/comments`

**Method:** `POST`

**Request body:**

```json
{
  "content": "Example comment content"
}
```

### 14. Get all comments

**Endpoint:** `/posts/{postId}/comments`

**Method:** `GET`

**Request parameters:**
- `page` (Integer, optional): The page number to retrieve. Default value is 0.
- `size` (Integer, optional): The number of posts per page. Default value is 10.
- `sort` (Sort.Direction, optional): The sorting direction. Default value is ASC.

*Example:* `/posts/{postId}/comments?page=3&size=20&sort=DESC`

### 15. Delete comment by ID

**Endpoint:** `/posts/{postId}/comments/{commentId}`

**Method:** `DELETE`

### 16. Get comment by ID

**Endpoint:** `/posts/{postId}/comments/{commentId}`

**Method:** `GET`

### 17. Get user by ID

**Endpoint:** `/users/{userId}`

**Method:** `GET`

### 18. Delete user by ID

**Endpoint:** `/users/{userId}`

**Method:** `DELETE`