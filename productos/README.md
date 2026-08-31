# ms-productos

Microservicio **Productos** — Spring Boot + JDBC puro (sin JPA).
Puerto **8082** | Base de datos: **db_productos** | Tabla: `producto`

## Arquitectura por capas

```
src/main/java/com/productos/
├── models/Producto.java             → Entidad (POJO) de la tabla `producto`.
├── persistence/ProductoPersistence.java → JDBC puro (DriverManager, PreparedStatement, ResultSet).
├── businesslogic/BLProducto.java    → Validaciones y reglas de negocio.
└── controler/ControllerProducto.java → API REST (@RestController).

src/main/resources/static/index.html → Frontend que consume la API con fetch.
```

## Cómo correr

1. Ejecutar `schema.sql` en MySQL (crea `db_productos` con datos de prueba).
2. `./mvnw spring-boot:run`
3. Abrir `http://localhost:8082` (frontend) o probar la API.

## Endpoints (para Postman)

| Verbo  | URL                          | Body JSON (POST/PUT) |
| :----- | :--------------------------- | :------------------- |
| GET    | http://localhost:8082/api/productos | — |
| GET    | http://localhost:8082/api/productos/{id} | — |
| POST   | http://localhost:8082/api/productos | `{ "nombre": "Cable HDMI", "descripcion": "2 metros", "precioBase": 15000, "activo": true, "aprobado": false }` |
| PUT    | http://localhost:8082/api/productos | igual al POST pero con `"id": 1` |
| DELETE | http://localhost:8082/api/productos/{id} | — |

Las fechas (`fechaCreacion`, `fechaActualizacion`) las genera MySQL automáticamente.
