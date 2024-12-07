# Installation
## tomcatt
When compiling the war must be called ROOT to have access to the main prefix “/” and to test the application edit at startup time change in deployment for example: '/test_war_exploded' delete '/test_war_exploded' to get the main prefix

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

HTTP 429 Too Many Requests

```json
{
  "message": "You have reached the limit of people in the group"
}
```

### Add Manager to Group

POST /api/v0/auth/addManager

#### Entities

```ts
type Manager = {
    role: string,
    user_id: number;
    name: string;
    code_person: string;
    last_name: string;
    email: string;
    username: string;
}
```

```json
{
  "username": "",
  "password": "",
  "htop_seed": "",
  "code_person": "",
  "name": "",
  "last_name": "",
  "email": "",
  "phone_number": "",
  "group_id": 0
}
```

HTTP 200

```json
{
  Manager
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
  "message": "The manager could not be created correctly"
}
```

HTTP 401 Unauthorized

```json
{
  "message": "You are not an administrator to create manager"
}
```

### Create Session

POST /api/v0/auth/createSession

#### Entities

```ts
type Session = {
    sessionId: number,
    groupId: number;
    sessionNumber: number;
    createdAt: date;
    duration: number;
    description: string;
}
```

```json
{
  "groupId": 0,
  "sessionNumber": 0,
  "startAt": "",
  "duration": 90,
  "description": "",
}
```

HTTP 200

```json
{
  Session
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
  "message": "The session could not be created correctly"
}
```

HTTP 401 Unauthorized

```json
{
  "message": "You are not an administrator to create session"
}
```

### Take Attendance

POST /api/v0/auth/takeAttendace

#### Entities

```ts
type Attendance = {
    message: string
}
```

```json
{
  "personId": "",
  "groupId": 0,
  "session_number": 3
}
```

HTTP 200

```json
{
  Attendance
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
  "message": "The attendance could not be created correctly"
}
```

HTTP 401 Unauthorized

```json
{
  "message": "You are not an manager or admin to create session"
}
```

### Get Group Statistics

GET /api/v0/auth/getGroupStatistics

#### Entities

```ts
type Statistics = {
    TotalPersons: number,
    TotalSessions: number,
    TotalAttendances: number
}
```

### Query Param

```jquery-css
groupId: 0
```

HTTP 200

```json
{
  Statistics
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
  "message": "Results were not obtained correctly"
}
```

HTTP 401 Unauthorized

```json
{
  "message": "You don't have administrator role to use the api"
}
```