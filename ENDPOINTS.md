## Entities

### User

```ts
type User = {
    username: string;
    role: string; // ("ADMIN", "MANAGER")
    name: string;
    last_name: string;
    email: string;
    phone_number: string;
    status: string; // ("UNKNOWN", "VERIFIED")
}
```

## Endpoints

### Auth

#### Login

POST /api/v0/auth

```json
{
  "username": "",
  "password": ""
}
```

Response

HTTP 200 OK

```json
{
  "token": "YOUR TOKEN",
  "user": User
}
```

HTTP 403 Forbidden

```json
{
  "message": "Username or password invalid"
}
```

#### Valid session

GET /api/v0/auth/valid

HTTP 200

```json
{
  "user": User
}
```

HTTP 403 Forbidden

```json
{
  "message": "Token expired or don't have token"
}
```