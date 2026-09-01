# ⚙️ Guía de Instalación — Proyecto1_springsena

Guía paso a paso para clonar, preparar y levantar los 3 microservicios desde cero.

---

## 1. Requisitos del sistema

| Herramienta | Versión mínima | Notas |
| :---------- | :------------- | :---- |
| **JDK**      | 21             | `java -version` |
| **Maven**    | 3.8+           | Opcional: los proyectos incluyen el wrapper `./mvnw`. |
| **MySQL**    | 8.x            | Para `usuarios` y `productos`. |
| **Git**      | —              | Para clonar el repositorio. |
| **Postman**  | —              | Opcional, para probar la API. |

---

## 2. Clonar el repositorio

```bash
git clone https://github.com/josuel-munendez/Proyecto1_springsena.git
cd Proyecto1_springsena
```

---

## 3. Crear las bases de datos (MySQL)

Cada microservicio con JDBC tiene su propio script. Se ejecutan **una sola vez**:

```bash
# Usuarios → crea la BD "mi_base_datos" y la tabla "usuario" con datos de prueba
mysql -u root -p < usuarios/src/main/resources/schema.sql

# Productos → crea la BD "db_productos" y la tabla "producto" con datos de prueba
mysql -u root -p < productos/src/main/resources/schema.sql
```

> **Nota:** por defecto las credenciales son `root` / `123456`. Si tu MySQL usa otra
> contraseña, edita:
> - `usuarios/src/main/resources/application.properties`
> - `productos/src/main/resources/application.properties`
>
> Y, hasta que se migre a `@Value` (ver `ROADMAP_Y_PENDIENTES.md`), las constantes
> `URL`/`USER`/`PASSWORD` de las clases de persistencia.

El microservicio `vehiculos` usa **H2 en memoria** y no requiere base de datos externa.

---

## 4. Levantar los microservicios

Cada uno es un proyecto Maven independiente. Abre **una terminal por microservicio**:

```bash
# Terminal 1 — Vehículos (puerto 8080)
cd vehiculos && ./mvnw spring-boot:run

# Terminal 2 — Usuarios (puerto 8081)
cd usuarios && ./mvnw spring-boot:run

# Terminal 3 — Productos (puerto 8082)
cd productos && ./mvnw spring-boot:run
```

> El wrapper `./mvnw` es un script autónomo que descarga Maven la primera vez y no
> requiere Maven instalado. En Windows usa `mvnw.cmd`.

---

## 5. Verificar que todo funciona

| Microservicio | URL | Qué deberías ver |
| :------------ | :-- | :--------------- |
| Vehículos | http://localhost:8080/vehiculos | Lista vacía + botón nuevo vehículo |
| Usuarios  | http://localhost:8081 | CRUD Usuarios + tabla con datos de prueba |
| Productos | http://localhost:8082 | CRUD Productos + catálogo de prueba |

También puedes probar la API directamente (documentado en `TESTING_POSTMAN.md`):

```bash
curl http://localhost:8081/api/usuarios
curl http://localhost:8082/api/productos
```

---

## 6. Solución de problemas comunes

| Síntoma | Posible causa | Solución |
| :------ | :------------ | :------- |
| `Access denied for user 'root'` | Contraseña de MySQL distinta a `123456` | Actualizar `application.properties` y las constantes de la persistencia. |
| `Communications link failure` | MySQL no está corriendo | `sudo systemctl start mysql` (o el equivalente). |
| Puerto ocupado (`8080 already in use`) | Otro proceso en el puerto | Cambiar `server.port` en `application.properties` o cerrar el proceso. |
| `Could not find or load main class` | Wrapper no otorgó permisos | `chmod +x mvnw` (Linux/macOS). |
| La consola H2 no abre | H2 console deshabilitada | Revisar `spring.h2.console.enabled=true` en `vehiculos/application.properties`. |

---

## 7. Detener los servicios

Presiona `Ctrl + C` en cada terminal. Cada microservicio termina su proceso de forma
limpia.
