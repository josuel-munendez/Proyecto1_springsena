# 🚗 Proyecto1_springsena

Proyecto académico (SENA) que construye una **arquitectura de microservicios** con
**Spring Boot**, **JDBC/MySQL** y buenas prácticas de desarrollo: programación por
capas, encriptación de datos, consumo de APIs con `fetch`, validación en frontend y
backend, documentación de código (Javadoc), pruebas de la API con Postman y paginación
de datos.

> **Entrega:** 24 de agosto — sustentación.

---

## 1. ¿Qué es este proyecto?

Un conjunto de **microservicios independientes** (cada uno un proyecto Maven con su
propio proceso, puerto y base de datos) que juntos implementan un CRUD por entidad,
con **comunicación real entre servicios vía REST**:

| Microservicio | Carpeta     | Tecnología              | Puerto | Base de datos | Vista            |
| :------------ | :---------- | :---------------------- | :----- | :------------ | :--------------- |
| **ms-parqueadero** (Vehículos) | `vehiculos/` | Spring Boot + **JPA** + Thymeleaf | 8080 | H2 (memoria) | Thymeleaf |
| **ms-usuarios**  | `usuarios/`  | Spring Boot + **JDBC puro**      | 8081 | `mi_base_datos` (MySQL) | `fetch` (JSON) |
| **ms-productos** | `productos/` | Spring Boot + **JDBC puro**      | 8082 | `db_productos` (MySQL)  | `fetch` (JSON) |

Cada microservicio expone su **API REST** y su propio frontend, aplicando la
**arquitectura por capas**:

```
Frontend → Controller → BL (Business Logic) → Persistence/Repository → BD
                              ↓
                    InterServiceClient (RestTemplate)
                              ↓
                    Otro microservicio (HTTP REST)
```

---

## 2. Requisitos previos

- **Java 21** (configurado en cada `pom.xml`).
- **Maven** (o usar el wrapper `./mvnw` incluido por proyecto).
- **MySQL** (para `usuarios` y `productos`).
- Navegador moderno (para los frontends con `fetch`).
- Opcional: **Postman** para probar la API.

---

## 3. Cómo correr el proyecto

### 3.1 Preparar las bases de datos (MySQL)

Ejecuta un solo script por microservicio (Workbench, consola o DBeaver):

```bash
# Crea db_productos (tabla producto + datos de prueba)
mysql -u root -p < productos/src/main/resources/schema.sql

# Crea mi_base_datos (tabla usuario + datos de prueba)
mysql -u root -p < usuarios/src/main/resources/schema.sql
```

> `vehiculos` usa **H2 en memoria**, así que no necesita script externo.

### 3.2 Arrancar cada microservicio (uno por terminal)

```bash
cd vehiculos && ./mvnw spring-boot:run    # http://localhost:8080
cd usuarios  && ./mvnw spring-boot:run    # http://localhost:8081
cd productos && ./mvnw spring-boot:run    # http://localhost:8082
```

Abre en el navegador:
- **Vehículos:** `http://localhost:8080/vehiculos` (Thymeleaf).
- **Usuarios:** `http://localhost:8081` (CRUD con `fetch`).
- **Productos:** `http://localhost:8082` (CRUD con `fetch`).

---

## 4. Endpoints REST

### ms-usuarios — `http://localhost:8081/api/usuarios`

| Verbo  | Ruta                  | Body (POST/PUT) | Descripción |
| :----- | :-------------------- | :-------------- | :---------- |
| GET    | `/api/usuarios`       | — | Listar todos |
| GET    | `/api/usuarios/{id}`  | — | Obtener por ID |
| POST   | `/api/usuarios`       | `{ "nombre": "...", "correo": "...", "password": "..." }` | Crear (BCrypt hash) |
| PUT    | `/api/usuarios`       | igual al POST con `"id": 1` | Actualizar |
| DELETE | `/api/usuarios/{id}`  | — | Eliminar |
| GET    | `/api/usuarios/paginado?page=1&size=10` | — | Paginado |
| POST   | `/api/usuarios/login` | `{ "correo": "...", "password": "..." }` | Login BCrypt |
| **GET** | **`/api/usuarios/resumen`** | **—** | **Consolida usuarios + vehículos + productos (cross-service)** |

### ms-productos — `http://localhost:8082/api/productos`

| Verbo  | Ruta                  | Body (POST/PUT) | Descripción |
| :----- | :-------------------- | :-------------- | :---------- |
| GET    | `/api/productos`      | — | Listar todos |
| GET    | `/api/productos/{id}` | — | Obtener por ID |
| POST   | `/api/productos`      | `{ "nombre": "Cable HDMI", "precioBase": 15000 }` | Crear |
| PUT    | `/api/productos`      | igual al POST con `"id": 1` | Actualizar |
| DELETE | `/api/productos/{id}` | — | Eliminar |
| GET    | `/api/productos/paginado?page=1&size=10` | — | Paginado |
| **GET** | **`/api/productos/resumen`** | **—** | **Consolida productos + usuarios + vehículos (cross-service)** |

### ms-parqueadero (Vehículos) — `http://localhost:8080/vehiculos`

| Verbo  | Ruta                           | Body | Descripción |
| :----- | :----------------------------- | :--- | :---------- |
| GET    | `/vehiculos`                   | — | Vista Thymeleaf |
| GET    | `/vehiculos/api/vehiculos`     | — | API REST JSON |
| POST   | `/vehiculos/api/vehiculos`     | `{ "placa": "ABC123", "marca": "..." }` | Crear |
| PUT    | `/vehiculos/api/vehiculos`     | — | Actualizar |
| DELETE | `/vehiculos/api/vehiculos/{id}`| — | Eliminar |
| GET    | `/vehiculos/api/vehiculos/paginado?page=1&size=10` | — | Paginado |
| **GET** | **`/vehiculos/api/vehiculos/resumen`** | **—** | **Consolida vehículos + usuarios + productos (cross-service)** |

### Arquitectura de comunicación cross-service

```
                    ┌─────────────────────────────────────┐
                    │        /resumen (cada servicio)     │
                    │                                     │
  ms-parqueadero ───┤──自身: vehículos (H2)               │
     (8080)         │──→ ms-usuarios:   GET /api/usuarios │
                    │──→ ms-productos:  GET /api/productos│
                    └─────────────────────────────────────┘

  Cada /resumen retorna JSON con los datos de los 3 servicios:
  {
    "microservicio": "ms-parqueadero",
    "vehiculos": [...],    ← datos propios
    "usuarios": [...],     ← obtenidos vía HTTP de ms-usuarios
    "productos": [...]     ← obtenidos vía HTTP de ms-productos
  }
```

---

## 5. Estructura del repositorio

```
Proyecto1_springsena/
├── README.md              ← este archivo
├── usuarios/              → microservicio de USUARIOS (JDBC puro)
├── productos/             → microservicio de PRODUCTOS (JDBC puro)
├── vehiculos/             → microservicio de VEHÍCULOS (JPA + Thymeleaf)
├── beta/                  → versión antigua del microservicio de usuarios
├── demo/                  → versión antigua del microservicio de vehículos
├── docs/                  → documentación técnica del proyecto
│   ├── ARQUITECTURA_Y_PATRONES.md
│   ├── SPRING_REST_DOCUMENTATION.md
│   ├── GUIA_DE_INSTALACION.md
│   ├── TESTING_POSTMAN.md
│   ├── ROADMAP_Y_PENDIENTES.md
│   └── MICROSERVICIOS.md
└── notas/                 → apuntes de clase por semestre
```

> `beta/` y `demo/` son versiones preliminares que se conservan; el código activo
> está en `usuarios/`, `productos/` y `vehiculos/`.

---

## 6. Documentación del proyecto

Toda la documentación técnica vive en la carpeta [`docs/`](docs/):

- [**ARQUITECTURA_Y_PATRONES.md**](docs/ARQUITECTURA_Y_PATRONES.md) — capas, patrones
  de diseño y buenas prácticas (guía para sustentar).
- [**SPRING_REST_DOCUMENTATION.md**](docs/SPRING_REST_DOCUMENTATION.md) — anotaciones y
  conceptos Spring REST usados en el código.
- [**GUIA_DE_INSTALACION.md**](docs/GUIA_DE_INSTALACION.md) — instalación paso a paso.
- [**TESTING_POSTMAN.md**](docs/TESTING_POSTMAN.md) — cómo probar la API con Postman.
- [**ROADMAP_Y_PENDIENTES.md**](docs/ROADMAP_Y_PENDIENTES.md) — todo lo que le falta al
  proyecto para estar completo.
- [**MICROSERVICIOS.md**](docs/MICROSERVICIOS.md) — qué es un microservicio y cómo se
  ve aquí.

Cada microservicio incluye además su propio `README.md` y Javadoc en el código.

---

## 7. Tecnologías y buenas prácticas

- **Java 21 + Spring Boot 4.1.x**
- **JDBC puro** (`DriverManager`, `PreparedStatement`, `ResultSet`) en usuario/productos
- **JPA / Spring Data** en vehículos
- **Thymeleaf** (vehículos) y **JavaScript `fetch`** (usuarios/productos)
- **BCrypt** encriptación de contraseñas (`BCryptPasswordEncoder`)
- **Spring Security** con `SecurityConfig` (permite inter-service calls)
- **RestTemplate** comunicación sincrónica entre microservicios
- **Prevención de SQL Injection** con `PreparedStatement`
- **Validación en frontend Y backend** (Defense in Depth)
- **Programación por capas** y **Documentación Javadoc**
- **Patrón Service Locator** para comunicación cross-service

---

## 8. Estado actual (resumen)

| Requisito                                                     | Estado |
| :------------------------------------------------------------ | :----- |
| Conexión a MySQL con JDBC (usuarios, productos)               | ✅      |
| Programación por capas                                        | ✅      |
| Consumo de API con `fetch` (usuarios, productos)              | ✅      |
| Validación frontend + backend                                 | ✅      |
| Documentación de código (Javadoc) y docs/                     | ✅      |
| CRUD de 3 entidades (usuarios, productos, vehículos)          | ✅      |
| Arquitectura de microservicios (proyectos + puertos propios)  | ✅      |
| Encriptación de datos (contraseñas BCrypt)                    | ✅      |
| Paginación de datos (JDBC LIMIT/OFFSET + JPA Pageable)        | ✅      |
| Comunicación entre microservicios (REST RestTemplate)         | ✅      |
| Pruebas con Postman (colección + capturas)                    | ✅      |

> Para el detalle completo y el plan de cierre, ver
> [`docs/ROADMAP_Y_PENDIENTES.md`](docs/ROADMAP_Y_PENDIENTES.md).

---

## 9. Autor

- **Jose Manuel Muñoz Fernández** — proyecto académico SENA.
- Repositorio: [github.com/josuel-munendez/Proyecto1_springsena](https://github.com/josuel-munendez/Proyecto1_springsena)
