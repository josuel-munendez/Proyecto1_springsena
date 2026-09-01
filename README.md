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
propio proceso, puerto y base de datos) que juntos implementan un CRUD por entidad:

| Microservicio | Carpeta     | Tecnología              | Puerto | Base de datos | Vista            |
| :------------ | :---------- | :---------------------- | :----- | :------------ | :--------------- |
| **ms-parqueadero** (Vehículos) | `vehiculos/` | Spring Boot + **JPA** + Thymeleaf | 8080 | H2 (memoria) | Thymeleaf |
| **ms-usuarios**  | `usuarios/`  | Spring Boot + **JDBC puro**      | 8081 | `mi_base_datos` (MySQL) | `fetch` (JSON) |
| **ms-productos** | `productos/` | Spring Boot + **JDBC puro**      | 8082 | `db_productos` (MySQL)  | `fetch` (JSON) |

Cada microservicio expone su **API REST** y su propio frontend, aplicando la
**arquitectura por capas**:

```
Frontend → Controller → BL (Business Logic) → Persistence/Repository → BD
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

| Verbo  | Ruta                  | Body (POST/PUT) |
| :----- | :-------------------- | :-------------- |
| GET    | `/api/usuarios`       | — |
| GET    | `/api/usuarios/{id}`  | — |
| POST   | `/api/usuarios`       | `{ "nombre": "...", "direccion": "...", "telefono": 0, "correo": "...", "saldo": 0 }` |
| PUT    | `/api/usuarios`       | igual al POST con `"id": 1` |
| DELETE | `/api/usuarios/{id}`  | — |

### ms-productos — `http://localhost:8082/api/productos`

| Verbo  | Ruta                  | Body (POST/PUT) |
| :----- | :-------------------- | :-------------- |
| GET    | `/api/productos`      | — |
| GET    | `/api/productos/{id}` | — |
| POST   | `/api/productos`      | `{ "nombre": "Cable HDMI", "descripcion": "2 m", "precioBase": 15000, "activo": true, "aprobado": false }` |
| PUT    | `/api/productos`      | igual al POST con `"id": 1` |
| DELETE | `/api/productos/{id}` | — |

> Las fechas de `productos` (`fechaCreacion`, `fechaActualizacion`) las genera MySQL.

### ms-parqueadero (Vehículos) — web Thymeleaf en `http://localhost:8080/vehiculos`

Operaciones CRUD completas (listar, crear, ver, editar, eliminar) usando **JPA + Thymeleaf**.

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
- **Prevención de SQL Injection** con `PreparedStatement`
- **Validación en frontend Y backend** (Defense in Depth)
- **Programación por capas** y **Documentación Javadoc**

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
| Arquitectura de microservicios (proyectos + puertos propios)  | 🟡 Parcial |
| Encriptación de datos (contraseñas)                           | ⏳ Pendiente |
| Paginación de datos                                           | ⏳ Pendiente |
| Comunicación entre microservicios (REST)                      | ⏳ Pendiente |
| Pruebas con Postman (colección + capturas)                    | 🟡 Parcial |

> Para el detalle completo y el plan de cierre, ver
> [`docs/ROADMAP_Y_PENDIENTES.md`](docs/ROADMAP_Y_PENDIENTES.md).

---

## 9. Autor

- **Jose Manuel Muñoz Fernández** — proyecto académico SENA.
- Repositorio: [github.com/josuel-munendez/Proyecto1_springsena](https://github.com/josuel-munendez/Proyecto1_springsena)
