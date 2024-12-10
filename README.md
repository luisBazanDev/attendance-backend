# API Endpoints Documentation

## Instalación

### Tomcat
- Al compilar el archivo WAR, asegúrate de nombrarlo como `ROOT.war` para que la aplicación esté accesible desde el prefijo principal `/`.
- Si pruebas la aplicación localmente con un prefijo diferente (por ejemplo, `/test_war_exploded`), edita la configuración de inicio eliminando este prefijo.

---

## Autenticación

### Login
**Endpoint**: `POST /api/v0/auth`  
**Descripción**: Autentica un usuario y devuelve un token y los datos del usuario.

**Request Body**:
```json
{
  "username": "string",
  "password": "string"
}
```
**Respuestas**:
- `200 OK: Devuelve el token y los datos del usuario.`
- `403 Forbidden: Usuario o contraseña inválidos.`

### Validar Sesión
**Endpoint**: `GET /api/v0/auth/valid`  
**Descripción**: Verifica si un token de sesión es válido.  

**Respuestas**:
- `200 OK: Sesión válida.`
- `403 Forbidden: Token inválido o expirado.`

## Grupos

### Crear Grupo
**Endpoint**: `POST /api/v0/auth/createGroup`  
**Descripción**: Crea un nuevo grupo. Solo accesible para administradores.  

**Request Body**:
```json
{
  "name": "string",
  "description": "string",
  "amount_persons": "int",
  "amount_sessions": "int"
}
```

**Respuestas**:
- `200 OK: Grupo creado con éxito.`
- `403 Forbidden: Token inválido o expirado.`
- `400 Bad Request: Error al crear el grupo.`
- `401 Unauthorized: No tienes permisos de administrador.`

### Agregar Persona a un Grupo
**Endpoint**: `POST /api/v0/auth/addPersonToGroup`  
**Descripción**: Añade una persona a un grupo. Requiere permisos de administrador o gestor.

**Request Body**:
```json
{
  "personId": "string",
  "firstName": "string",
  "lastName": "string",
  "phone": "string",
  "groupId": "int"
}
```

**Respuestas**:
- `200 OK: Persona añadida correctamente.`
- `403 Forbidden: Token inválido o expirado.`
- `400 Bad Request: Error al agregar la persona.`
- `429 Too Many Requests: Límite de personas en el grupo alcanzado.`

## Sesiones

### Crear Sesión
**Endpoint**: `POST /api/v0/auth/createSession`  
**Descripción**: Crea una nueva sesión para un grupo. Solo accesible para administradores.

**Request Body**:
```json
{
  "groupId": "int",
  "sessionNumber": "int",
  "startAt": "datetime",
  "duration": "int",
  "description": "string"
}
```

**Respuestas**:
- `200 OK: Sesión creada con éxito.`
- `403 Forbidden: Token inválido o expirado.`
- `400 Bad Request: Error al crear la sesión.`

## Estadísticas y Consultas

### Obtener Estadísticas del Grupo
**Endpoint**: `GET /api/v0/auth/getGroupStatistics?groupId={groupId}`  
**Descripción**: Devuelve estadísticas sobre un grupo específico.

**Respuestas**:
- `200 OK: Estadísticas obtenidas correctamente.`
- `403 Forbidden: Token inválido o expirado.`
- `400 Bad Request: Error al obtener las estadísticas.`

### Buscar Grupos por Nombre
**Endpoint**: `GET /api/v0/auth/searchGroupByName?name={name}`  
**Descripción**: Busca grupos por nombre.

**Respuestas**:
- `200 OK: Grupos encontrados.`
- `403 Forbidden: Token inválido o expirado.`
- `400 Bad Request: Error al buscar grupos.`

### Ver Sesiones del Día
**Endpoint**: `GET /api/v0/auth/getSessionsToday`  
**Descripción**: Lista todas las sesiones programadas para el día actual.

**Respuestas**:
- `200 OK: Sesiones obtenidas correctamente.`
- `403 Forbidden: Token inválido o expirado.`
- `400 Bad Request: Error al obtener las sesiones.`

## Notas Finales
1.	**Autorización**: Para la mayoría de los endpoints, necesitas incluir el token de autenticación en los encabezados de la solicitud:

```http
Authorization: Bearer YOUR_TOKEN
```

2.	Roles: Algunos endpoints están restringidos a usuarios con roles específicos como “ADMIN” o “MANAGER”.
3.	Errores Comunes:
- Asegúrate de enviar datos válidos para evitar errores de tipo `400 Bad Request`.
- Verifica tu token de autenticación si recibes un error `403 Forbidden`.