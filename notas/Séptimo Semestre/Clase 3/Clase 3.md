# Séptimo Semestre — Clase 3

**Fecha:** 10 de agosto de 2026

---

## 🛠️ Lo que hicimos en clase

Dimos el salto al **Proyecto 1 real con JDBC puro**: pasamos de las entidades sueltas (`Producto`, `Vehiculo`) a montar el **CRUD completo de Usuarios** conectado a MySQL con **`DriverManager`, `Connection`, `PreparedStatement` y `ResultSet`** (sin JPA, sin JdbcTemplate).

Se creó un nuevo paquete base para este microservicio de usuarios:

```
demo/src/main/java/com/example/demo_mysql/
├── models
│   └── Usuario.java                 → Entidad (POJO) de la tabla `usuario`.
├── persistence
│   └── UsuarioPersistence.java      → Capa de acceso a datos (JDBC puro / CRUD).
├── businesslogic
│   └── BLUsuario.java               → Capa de negocio (validaciones).
├── controler
│   └── ControllerUsuario.java       → API REST (`@RestController`).
└── service
    └── UsuarioService.java          → Capa de negocio alternativa (BLL).
```

### 1. `models/Usuario.java` — La entidad

Es un **POJO** (Plain Old Java Object) que mapea la tabla `usuario`:

```java
public class Usuario {
    private Long id;
    private String nombre;
    private String direccion;
    private int telefono;
    private String correo;
    private int saldo;

    public Usuario() { }                                   // vacío (lo piden los frameworks)
    public Usuario(Long id, String nombre, String direccion,
                   int telefono, String correo, int saldo) { ... }

    // getters y setters de cada atributo (encapsulamiento)
}
```

- Atributos **`private`** → **encapsulamiento** (lo visto en Clase 2).
- **Dos constructores**: vacío (lo exige Jackson para deserializar JSON) y completo.
- Sin lógica: solo estado + getters/setters.

### 2. `persistence/UsuarioPersistence.java` — JDBC puro

Aquí está **todo el SQL** y la conexión a MySQL. Métodos del CRUD:

| Método                       | SQL                                   | Verbo HTTP que lo usa |
| :--------------------------- | :------------------------------------ | :-------------------- |
| `guardarUsuario(Usuario)`    | `INSERT INTO usuario ...`             | POST                  |
| `eliminarUsuario(Long id)`   | `DELETE FROM usuario WHERE id = ?`    | DELETE                |
| `actualizarUsuario(Usuario)` | `UPDATE usuario SET ... WHERE id = ?` | PUT                   |
| `listarUsuarios()`           | `SELECT ... FROM usuario`             | GET                   |

Claves técnicas del código:

- **Conexión con `DriverManager.getConnection(URL, USER, PASSWORD)`** — JDBC puro.
- **`PreparedStatement`** con parámetros `?` → previene **SQL Injection**.
- **`try-with-resources`** → cierra automáticamente `Connection`, `PreparedStatement` y `ResultSet` (no hay que llamar `.close()` a mano).
- **`ResultSet`** → se recorre con `resultSet.next()` y se lee cada columna con `getLong("id")`, `getString("nombre")`, etc.
- Las credenciales (`jdbc:mysql://localhost:3306/mi_base_datos`, `root`, `123456`) están **escritas en duro** en el código → ⚠️ pendiente de corregir (ver plan de fin de semana).

### 3. `businesslogic/BLUsuario.java` — Capa de negocio

Es el "cerebro". Valida los datos **antes** de tocar la base de datos:

```java
public boolean validarUsuario(Usuario u) {
    if (u == null) { System.out.println("Error: el usuario es nulo."); return false; }
    if (u.getNombre() == null || u.getNombre().isBlank()) { ... return false; }
    if (u.getCorreo() == null || u.getCorreo().isBlank()) { ... return false; }
    if (u.getSaldo() < 0) { System.out.println("Error: el saldo no puede ser negativo."); return false; }
    return true;
}

public boolean guardarUsuario(Usuario u) {
    if (validarUsuario(u)) {
        return persistence.guardarUsuario(u);   // delega en persistencia SOLO si valida
    }
    return false;
}
```

**Reglas de negocio implementadas:** objeto no nulo, nombre obligatorio, correo obligatorio, saldo no negativo.

> 💡 El `BL` **nunca** habla con el Controller ni con la BD directamente; se comunica **solo** con `UsuarioPersistence`.

### 4. `controler/ControllerUsuario.java` — API REST

Es la puerta de entrada HTTP:

```java
@RestController
@RequestMapping("/api/usuarios")
public class ControllerUsuario {

    private final BLUsuario bl;

    @GetMapping                 → listarUsuarios()          // GET /api/usuarios
    @GetMapping("/{id}")        → obtener(id)               // GET /api/usuarios/1
    @PostMapping                → guardar(@RequestBody Usuario u)  // POST
    @PutMapping                 → actualizar(@RequestBody Usuario u) // PUT
    @DeleteMapping("/{id}")     → eliminar(@PathVariable Long id)    // DELETE
}
```

- **`@RestController`** = `@Controller` + `@ResponseBody` → devuelve **JSON** (lo aprendido en Clase 2).
- **`@RequestBody`** convierte el JSON del cliente a un objeto `Usuario`.
- **`@PathVariable`** captura el id de la URL (`/api/usuarios/5`).
- El Controller **delega** en el `BL` (nunca llama directo a la persistencia).

### 5. `service/UsuarioService.java` — La capa BLL (extra)

Igual que `BLUsuario` pero con el nombre estándar de la industria (**Service**). Expone: `validarUsuario`, `registrarUsuario`, `eliminarUsuario`, `actualizarUsuario`, `listarUsuarios`. Es la **equivalencia 100%** con el `BLVehiculo` del Parqueadero.

### 6. Frontend con `fetch` — `static/index.html`

Se creó un **HTML plano** (sin Thymeleaf) en `demo/src/main/resources/static/index.html` que consume la API con `fetch`:

```javascript
// LISTAR (GET)
async function listar() {
    const res = await fetch("http://localhost:8081/api/usuarios");
    const usuarios = await res.json();
    // ...renderiza una fila por cada usuario en la tabla
}

// GUARDAR / ACTUALIZAR (POST / PUT)
await fetch(BASE_URL, {
    method: esNuevo ? "POST" : "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(usuario)
});

// ELIMINAR (DELETE)
await fetch(BASE_URL + "/" + id, { method: "DELETE" });
```

- La URL base es `http://localhost:8081/api/usuarios` (puerto 8081).
- Muestra mensajes de éxito/error.
- Al cargar la página se llama a `listar()`.

### 7. Base de datos — `schema.sql`

```sql
CREATE DATABASE IF NOT EXISTS mi_base_datos;
USE mi_base_datos;

CREATE TABLE IF NOT EXISTS usuario (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre     VARCHAR(100) NOT NULL,
    direccion  VARCHAR(200),
    telefono   INT,
    correo     VARCHAR(100),
    saldo      INT
);

INSERT INTO usuario (nombre, direccion, telefono, correo, saldo) VALUES
('Ana García',   'Calle 1 #2-3', 3105551234, 'ana@mail.com',   50000),
('Luis Pérez',   'Cra 4 #5-6',   3205559876, 'luis@mail.com',  120000),
('María López',  'Av 7 #8-9',    3005554321, 'maria@mail.com', 75000);
```

---

## 📌 Conceptos aprendidos y usados hoy

| Concepto                   | Explicación breve                                                                                                        |
| :------------------------- | :----------------------------------------------------------------------------------------------------------------------- |
| **JDBC puro**              | API de bajo nivel de Java para hablar con la BD con SQL en texto plano (`Connection`, `PreparedStatement`, `ResultSet`). |
| **`PreparedStatement`**    | Sentencia SQL con parámetros `?`. Evita inyección SQL y es más seguro/legible.                                           |
| **`try-with-resources`**   | Bloque que **cierra solo** los recursos (Connection, Statement, ResultSet). Buenísima práctica.                          |
| **`ResultSet`**            | Objeto que guarda las filas devueltas por un `SELECT`; se recorre con `.next()`.                                         |
| **Programación por capas** | Separar en `models` → `businesslogic` → `persistence` → `controler`. Cada capa tiene UNA responsabilidad.                |
| **`@RequestBody`**         | Convierte el JSON del cuerpo de la petición a un objeto Java.                                                            |
| **`@PathVariable`**        | Captura un valor desde la ruta de la URL (ej. el id en `/api/usuarios/5`).                                               |
| **`fetch`**                | API de JavaScript para hacer peticiones HTTP (GET, POST, PUT, DELETE) desde el frontend.                                 |
| **JSON**                   | Formato en el que se intercambian los datos entre frontend y backend (es la "Vista" en una API REST).                    |
| **CRUD**                   | Create (POST), Read (GET), Update (PUT), Delete (DELETE).                                                                |

### ¿Cómo encaja en MVC?
- **Model (M):** `models`, `persistence` y `businesslogic` (todo lo que gestiona los datos y su lógica).
- **Controller (C):** `controler` (recibe la petición HTTP).
- **View (V):** el **JSON** que devuelve el controller (y el `index.html` que lo consume).

---

## 🔗 Conexión con Clases 1 y 2

- **Clase 1:** creamos el proyecto `demo` (Spring Initializr), las entidades `Producto` (RED) y `Vehiculo` (Parqueadero), configuramos `application.properties` (puerto 8081, MySQL 3304) y aprendimos los **getters de booleanos** (estándar JavaBeans: `is...()` para primitivos, `get...()` para objetos).
- **Clase 2:** refactorizamos entidades (atributos `private`, constructores), creamos **`BLVehiculo`** (validaciones) y **`ControllerVehiculo`** (vacío), y aprendimos **programación por capas**, **encapsulamiento**, `@RestController` vs `@Controller`, `@RequestBody`, `@RequestParam`, etc.
- **Clase 3 (hoy):** aplicamos todo eso a un proyecto real con **persistencia real** (JDBC puro + MySQL + `schema.sql`) y le pusimos **frontend con `fetch`**. La diferencia clave con las clases anteriores es que ahora los datos **sí se guardan y se leen de la base de datos**.

**Flujo completo que quedó armado hoy:**
```
index.html (fetch)
   ↓ GET/POST/PUT/DELETE http://localhost:8081/api/usuarios
ControllerUsuario   → recibe la petición HTTP
   ↓
BLUsuario           → valida las reglas de negocio
   ↓
UsuarioPersistence  → JDBC puro (PreparedStatement)
   ↓
MySQL (mi_base_datos)
```

---

## 📚 Tarea / pendientes
- Ver el plan completo del fin de semana en **`TAREAS_PENDIENTES.md`** (encriptación, paginación, `@Value`, Logger, DTO, microservicios, etc.).

---

## 🖼️ Material de clase

![[WhatsApp Image 2026-08-10 at 6.17.24 PM.jpeg]]

*Imagen: tipos de peticiones web y su estructura: http (GET, POST, PUT, DELETE, UPDATE, QUERY), servidor (codigos de estado, mysql), url (parametros).*
