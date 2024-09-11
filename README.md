# ForumEngine

## Endpoints

| Method | Endpoint         | Description       |
|--------|------------------|-------------------|
| POST   | `/auth/register` | User registration |
| POST   | `/auth/login`    | User login        |

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