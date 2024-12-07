## Entities

### User

```ts
type User = {
    username: string;
    code: string;
    role: string; // ("ADMIN", "MANAGER")
    name: string;
    last_name: string;
    email: string;
    phone_number: string;
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

### Create Group

POST /api/v0/auth/createGroup

#### Entities

```ts
type Group = {
    id: int,
    name: string;
    description: string;
    amount_persons: int;
    amount_sessions: int;
}
```

```json
{
  "name": "",
  "description": "",
  "amount_persons": 0,
  "amount_sessions": 0
}
```

HTTP 200

```json
{
  Group
}
```

HTTP 403 Forbidden

```json
{
  "message": "Token expired or don't have token"
}
```

HTTP 400 Bad Request

```json
{
  "message": "The group could not be created correctly"
}
```

HTTP 401 Unauthorized

```json
{
  "message": "You are not an administrator to create groups"
}
```

### Add Person to Group

POST /api/v0/auth/addPersonToGroup

#### Entities

```ts
type Person = {
    code: int,
    firstName: string;
    lastName: string;
    email: string;
    phone: string;
    state: string
}
```

```json
{
  "personId": "",
  "firstName": "",
  "lastName": "",
  "phone": "",
  "groupId": 0
}
```

HTTP 200

```json
{
  Person
}
```

HTTP 403 Forbidden

```json
{
  "message": "Token expired or don't have token"
}
```

HTTP 400 Bad Request

```json
{
  "message": "The person could not be created correctly"
}
```

HTTP 401 Unauthorized

```json
{
  "message": "You are not an administrator and manager to create person"
}
```

