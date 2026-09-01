# 🧪 Pruebas de la API con Postman

Guía para probar los microservicios REST con **Postman** (GET, POST, PUT, DELETE).
Esta documentación cubre los endpoints **JSON** de `usuarios` y `productos`
(`vehiculos` usa Thymeleaf, no JSON REST por ahora).

---

## 1. Prerrequisitos

1. MySQL corriendo y bases creadas (ver `GUIA_DE_INSTALACION.md`).
2. Los microservicios levantados (`./mvnw spring-boot:run`).
3. Postman instalado (o cualquier cliente HTTP: curl, Insomnia).

---

## 2. URLs base

| Microservicio | URL base |
| :------------ | :------- |
| Usuarios  | `http://localhost:8081/api/usuarios` |
| Productos | `http://localhost:8082/api/productos` |

---

## 3. Colección de ejemplos

### 3.1 Usuarios

| Verbo  | URL | Body (application/json) |
| :----- | :-- | :---------------------- |
| **GET** | `http://localhost:8081/api/usuarios` | — |
| **GET** | `http://localhost:8081/api/usuarios/1` | — |
| **POST** | `http://localhost:8081/api/usuarios` | ```{"nombre":"Carlos Ruiz","direccion":"Cra 1 #2-3","telefono":3105550000,"correo":"carlos@mail.com","saldo":25000}``` |
| **PUT** | `http://localhost:8081/api/usuarios` | ```{"id":1,"nombre":"Ana García","direccion":"Calle 1 #2-3","telefono":3105551234,"correo":"ana@mail.com","saldo":60000}``` |
| **DELETE** | `http://localhost:8081/api/usuarios/1` | — |

**Respuestas esperadas:**
- `GET` → arreglo JSON `[{ "id":1, "nombre":"Ana García", ... }]`.
- `POST` / `PUT` / `DELETE` → `true` (éxito) o `false` (validación fallida o id inexistente).

### 3.2 Productos

| Verbo  | URL | Body (application/json) |
| :----- | :-- | :---------------------- |
| **GET** | `http://localhost:8082/api/productos` | — |
| **GET** | `http://localhost:8082/api/productos/1` | — |
| **POST** | `http://localhost:8082/api/productos` | ```{"nombre":"Cable HDMI","descripcion":"2 metros","precioBase":15000,"activo":true,"aprobado":false}``` |
| **PUT** | `http://localhost:8082/api/productos` | ```{"id":1,"nombre":"Teclado mecánico","descripcion":"RGB switches rojos","precioBase":260000,"activo":true,"aprobado":true}``` |
| **DELETE** | `http://localhost:8082/api/productos/1` | — |

---

## 4. Buenas prácticas al probar

1. **Verbos HTTP correctos:** GET lee, POST crea, PUT actualiza, DELETE elimina.
2. **Headers:** en POST/PUT usa `Content-Type: application/json`.
3. **POST debe llevar `id`**: no (o `null`); el id lo asigna la BD (AUTO_INCREMENT).
4. **PUT debe llevar `id`**: sí, con el valor del registro a actualizar.
5. **Validación del backend:** probar enviar datos inválidos (nombre vacío, precio
   negativo, correo sin `@`) y verificar que responde `false`. Esto demuestra la
   validación en backend (Defense in Depth).

> ⚠️ **Pendiente:** crear y versionar la **colección Postman** (archivo `.json`) y las
> **capturas de pantalla** para la sustentación. Ver `ROADMAP_Y_PENDIENTES.md` (sección 4).

---

## 5. Alternativa con curl (sin Postman)

```bash
# Listar
curl http://localhost:8081/api/usuarios

# Listar por id
curl http://localhost:8081/api/usuarios/1

# Crear
curl -X POST http://localhost:8081/api/usuarios \
     -H "Content-Type: application/json" \
     -d '{"nombre":"Ana","direccion":"Calle 1","telefono":310555,"correo":"a@mail.com","saldo":100}'

# Actualizar
curl -X PUT http://localhost:8081/api/usuarios \
     -H "Content-Type: application/json" \
     -d '{"id":1,"nombre":"Ana","direccion":"Calle 1","telefono":310555,"correo":"a@mail.com","saldo":200}'

# Eliminar
curl -X DELETE http://localhost:8081/api/usuarios/1
```
