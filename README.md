# ForumEngine

## Endpoints

| Method | Endpoint           | Description         |
|--------|--------------------|---------------------|
| POST   | `/auth/register`   | User registration   |
| POST   | `/auth/login`      | User login          |
| GET    | `/categories`      | Get all categories  |
| POST   | `/categories`      | Add a new category  |
| GET    | `/categories/{id}` | Get category by ID  |
| DELETE | `/categories/{id}` | Delete the category |
| POST   | `/posts`           | Add a new post      |

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

### 3. Get all categories

**Endpoint:** `/categories`

**Method:** `GET`

### 4. Add a new category

**Endpoint:** `/categories`

**Method:** `POST`

**Request body:**

```json
{
  "name": "exampleNameOfCategory",
  "description": "exampleDescription"
}
```

### 5. Get category by ID

**Endpoint:** `/categories/{id}`

**Method:** `GET`

### 6. Delete the category

**Endpoint:** `/categories/{id}`

**Method:** `DELETE`

### 7. Add a new post

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