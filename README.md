# ForumEngine

> [!NOTE]
> This application is not `business oriented` and my focus is mostly on technical part, I just want to implement a sample with using monolithic architecture pattern.

> [!WARNING]  
> This project is in progress.
 
**ONLINE DEMO: [http://ec2-13-53-102-21.eu-north-1.compute.amazonaws.com:8080/swagger-ui/index.html](http://ec2-13-53-102-21.eu-north-1.compute.amazonaws.com:8080/swagger-ui/index.html)**

* [About](#-about)
* * [Monolithic architecture pattern](#-monolithic-architecture-pattern)
* [Technology stack](#-technology-stack)
* * [Backend](#-backend)
* * [Database](#-database)
* * [Testing](#-testing)
* * [Containerization](#-containerization)
* * [CI/CD](#-cicd)
* * [Amazon Web Services](#-amazon-web-services)
* [API documentation](#-api-documentation)
* * [Swagger UI](#-swagger-ui)
* * [Postman](#-postman)
* [Starting up](#-starting-up)
* [Endpoints](#-endpoints)
* * [Security](#-security)

## 📌 About
A CRUD application where people can sign up, log in, create, update and delete posts. Additionally, users can reply to posts in the comment section and use other features. It’s a great platform for building a community where people can interact and share knowledge.
### 🔗 Monolithic architecture pattern
![image](https://github.com/user-attachments/assets/28a2af73-d795-4659-93f7-14ab5a477321)

## 🔨 Technology stack
### 💻 Backend
* Java 17
* Spring Boot 3.3.2 
* Spring Data JPA
* Spring Security
* Lombok

### 🐬 Database
* MySQL 8.0
* Liquibase

### 🧪 Testing

![code_coverage](https://github.com/user-attachments/assets/7789aeb7-46c4-4831-9f3b-6cea2bc56998)

Unit & integration tests
* JUnit 5
* Mockito
* SpringBootTest
* Testcontainers

### 📦 Containerization
* Docker
* Two containers are configured in the `docker-compose.yml` file:
  - **mysql_db**: MySQL 8.0 database
  - **backend**: Java 17 (Spring Boot) application

### 🔁 CI/CD
This repository uses **GitHub Actions** for automatic building and testing on every `push` and `pull request` to the `master` branch.

Additionally, a complete CI/CD pipeline has been configured, which includes:
- Building the application's Docker image.
- Pushing the generated image to Docker Hub.
- Deploying the application on an AWS EC2 instance.
- Using AWS RDS as the application's database.

### 🌐 Amazon Web Services
- **AWS EC2**: The application is deployed on an AWS EC2 (Elastic Compute Cloud) instance.
- **AWS RDS**: The application's database is hosted on AWS RDS (Relational Database Service).
```
http://ec2-13-53-102-21.eu-north-1.compute.amazonaws.com:8080
```
or
```
http://api.forumengine.jchojdak.com:8080
```

## 📄 API documentation
### 📍 Swagger UI

The application generates interactive API documentation Swagger UI (SpringDoc OpenAPI).
```
http://localhost:8080/swagger-ui/index.html
```

### 📍 Postman

Collection to import:
```
https://github.com/jchojdak/forumengine/blob/master/postman_collection/forumengine.json
```

## 🚀 Starting up
To launch the application, follow the steps:
1. Clone project
```
git clone https://github.com/jchojdak/forumengine.git
```
2. Open cloned directory
```
cd forumengine
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

## 📜 Endpoints

| #  | Method | Endpoint                               | Description                               | Authorization                                      |
|----|--------|----------------------------------------|-------------------------------------------|----------------------------------------------------|
| 1  | POST   | `/auth/register`                       | User registration                         | No                                                 |
| 2  | POST   | `/auth/login`                          | User login                                | No                                                 |
| 3  | PATCH  | `/auth/password`                       | Change the password of the logged in user | Yes (authenticated user)                           |
| 4  | GET    | `/categories`                          | Get all categories                        | No                                                 |
| 5  | POST   | `/categories`                          | Add a new category                        | Yes (admin)                                        |
| 6  | GET    | `/categories/{id}`                     | Get category by ID                        | No                                                 |
| 7  | DELETE | `/categories/{id}`                     | Delete category by ID                     | Yes (admin)                                        |
| 8  | POST   | `/posts`                               | Add a new post                            | Yes (authenticated user)                           |
| 9  | GET    | `/posts`                               | Get all posts                             | No                                                 |
| 10 | GET    | `/posts/{id}`                          | Get post by ID                            | No                                                 |
| 11 | DELETE | `/posts/{id}`                          | Delete post by ID                         | Yes (authenticated author or admin)                |
| 12 | PATCH  | `/posts/{id}`                          | Update post by ID                         | Yes (authenticated author)                         |
| 13 | POST   | `/posts/{postId}/comments`             | Add a new comment to the post             | Yes (authenticated user)                           |
| 14 | GET    | `/posts/{postId}/comments`             | Get all comments                          | No                                                 |
| 15 | DELETE | `/posts/{postId}/comments/{commentId}` | Delete comment by ID                      | Yes (authenticated author of the comment or admin) |
| 16 | GET    | `/posts/{postId}/comments/{commentId}` | Get comment by ID                         | No                                                 |
| 17 | PATCH  | `/posts/{postId}/comments/{commentId}` | Update comment by ID                      | Yes (authenticated author of the comment)          |
| 18 | PATCH  | `/users`                               | Update logged in user details             | Yes (authenticated user)                           |
| 19 | GET    | `/users/{userId}`                      | Get user by ID                            | No                                                 |
| 20 | DELETE | `/users/{userId}`                      | Delete user by ID                         | Yes (admin)                                        |
| 21 | POST   | `/users/{userId}/roles/{roleId}`       | Assign role to the user                   | Yes (admin)                                        |
| 22 | DELETE | `/users/{userId}/roles/{roleId}`       | Remove role from the user                 | Yes (admin)                                        |

### 🔑 Security
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
- `categoryId` (Long, optional): Get by category ID. Not required.

*Example:* `/posts?page=3&size=20&sort=DESC&categoryId=2`

### 10. Get post by ID

**Endpoint:** `/posts/{id}`

**Method:** `GET`

### 11. Delete post by ID

**Endpoint:** `/posts/{id}`

**Method:** `DELETE`

### 12. Update post by ID

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

### 17. Update comment by ID

**Endpoint:** `/posts/{postId}/comments/{commentId}`

**Method:** `PATCH`

**Request body:**

```json
{
  "content": "Example new content"
}
```

### 18. Update logged in user details

**Endpoint:** `/users`

**Method:** `PATCH`

**Request body:**

```json
{
  "firstName": "John",
  "lastName": "Smith",
  "mobile": "+48123123123",
  "email": "john.smith@gmail.com"
}
```

### 19. Get user by ID

**Endpoint:** `/users/{userId}`

**Method:** `GET`

### 20. Delete user by ID

**Endpoint:** `/users/{userId}`

**Method:** `DELETE`

### 21. Assign role to the user

**Endpoint:** `/users/{userId}/roles/{roleId}`

**Method:** `POST`

### 22. Remove role from the user

**Endpoint:** `/users/{userId}/roles/{roleId}`

**Method:** `DELETE`
