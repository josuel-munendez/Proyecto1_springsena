# ms-usuarios

Microservicio **Usuarios** — Spring Boot + **JDBC puro** (sin JPA).
Puerto **8081** | Base de datos: **mi_base_datos** (MySQL) | Tabla: `usuario`

## Arquitectura por capas

```
src/main/java/com/usuarios/
├── models/Usuario.java            → Entidad (POJO) de la tabla `usuario`
├── persistence/UsuarioPersistency.java → JDBC puro (DriverManager, PreparedStatement, ResultSet)
├── businesslogic/UsuarioBL.java   → Validaciones y reglas de negocio
└── controller/UsuarioController.java → API REST (@RestController)

src/main/resources/static/index.html → Frontend que consume la API con fetch
```

## Cómo correr

1. Ejecutar `schema.sql` en MySQL (crea `mi_base_datos` con datos de prueba).
2. `./mvnw spring-boot:run`
3. Abrir `http://localhost:8081` (frontend) o probar la API.

## Endpoints (para Postman)

| Verbo  | URL                          | Body JSON (POST/PUT) |
| :----- | :--------------------------- | :------------------- |
| GET    | http://localhost:8081/api/usuarios | — |
| GET    | http://localhost:8081/api/usuarios/{id} | — |
| POST   | http://localhost:8081/api/usuarios | `{ "nombre": "Ana", "direccion": "Calle 1", "telefono": 310555, "correo": "ana@mail.com", "saldo": 500 }` |
| PUT    | http://localhost:8081/api/usuarios | igual al POST pero con `"id": 1` |
| DELETE | http://localhost:8081/api/usuarios/{id} | — |

## Reglas de negocio (BL)

- Nombre obligatorio.
- Correo obligatorio.
- Saldo no negativo.
