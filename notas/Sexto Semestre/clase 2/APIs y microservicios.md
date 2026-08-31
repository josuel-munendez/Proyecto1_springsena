
# APIs y Microservicios — Clase 2

![[Pasted image 20260504131925.png]]

Que informaciòn debe viajar en el body de una solicitud http

Depende del **método HTTP**, pero la idea clave es esta:

👉 El **body** (cuerpo) se usa para enviar **datos al servidor** cuando quieres **crear o modificar recursos**.

---

## 🔹 ¿Qué información viaja en el body?

### 1. Datos del recurso (lo más común)

Se envían en formatos como JSON, XML o form-data.

**Ejemplo (JSON):**

```
{  "nombre": "Juan",  "email": "juan@email.com",  "edad": 20}
```

📌 Esto se usa en:

- `POST` → crear datos
- `PUT` / `PATCH` → actualizar datos

---

### 2. Archivos

Cuando subes archivos (imágenes, PDFs, etc.)

**Ejemplo:**

- Fotos de perfil
- Documentos

📌 Se usa:

- `multipart/form-data`

---

### 3. Datos de formularios

Como los de un login o registro.

**Ejemplo:**

```
username=juan&password=1234
```

📌 Formatos comunes:

- `application/x-www-form-urlencoded`
- `multipart/form-data`

---

### 4. Datos estructurados complejos

Para APIs más avanzadas:

```
{  "usuario": {    "nombre": "Ana",    "roles": ["admin", "editor"]  },  "activo": true}

# APIs y Microservicios — Clase 2

## Solicitud HTTP: Conceptos y Ejemplo Visual

![[Pasted image 20260504131925.png]]

Una **solicitud HTTP** es enviada por el cliente (navegador o Postman) al servidor para solicitar un recurso. La estructura básica incluye:

- **Línea de solicitud:** método, recurso, versión (ej: `GET /index.html HTTP/1.1`)
- **Cabeceras (headers):** metadatos de la solicitud (ej: `Content-Type`, `Authorization`)
- **Cuerpo (body):** datos enviados al servidor (solo en métodos como POST, PUT, PATCH)

---

## ¿Qué información viaja en el body?

Depende del **método HTTP**. El body se usa para enviar **datos al servidor** cuando quieres **crear o modificar recursos**.

### Tipos de datos comunes en el body

| Tipo de información         | Ejemplo                                 | Uso típico                |
|----------------------------|-----------------------------------------|---------------------------|
| JSON                       | `{ "nombre": "Juan" }`                | APIs modernas             |
| Texto plano                | `"Hola mundo"`                          | Logs, pruebas             |
| Archivos/binarios          | imagen, PDF                             | Subidas                   |
| Formulario (urlencoded)    | `user=juan&pass=123`                    | Login, formularios        |
| XML                        | `<user>Juan</user>`                      | Sistemas legacy           |
| Booleanos/números          | `true`, `42`                            | Configuración             |
| Listas/arrays              | `[1,2,3]`                               | Datos estructurados       |


**Ejemplo de body en JSON:**

```json
{
  "nombre": "Juan",
  "email": "juan@email.com",
  "edad": 20
}
```

**Métodos que usan body:**
- `POST` → crear datos
- `PUT` / `PATCH` → actualizar datos

**Métodos que NO usan body:**
- `GET` (no debería llevar body)
- `DELETE` (rara vez se usa)

**Header importante:**
```
Content-Type: application/json
```
Indica al servidor cómo interpretar los datos enviados.

---

## ¿Por dónde se puede mandar información en HTTP?

| Canal     | Ejemplo                                 | Uso principal                |
|-----------|-----------------------------------------|------------------------------|
| Body      | `{ "nombre": "Juan" }`                | Datos complejos, crear/actualizar |
| Query     | `/usuarios?nombre=juan&edad=20`         | Filtros, búsquedas           |
| Path      | `/usuarios/123`                         | Identificadores de recursos  |
| Headers   | `Authorization: Bearer token123`        | Autenticación, configuración |
| Cookies   | `Cookie: sessionId=abc123`              | Sesión, estado de usuario    |

**Regla mental:**
- Body → datos
- Query → filtrar/buscar
- Path → identificar
- Headers → configuración
- Cookies → estado

---

## Ejemplo práctico: Calculadora con Spring Boot y Postman

### Código del controlador (Java, Spring Boot)
```java
@RestController
@RequestMapping("/calculadora")
public class ControllerCalculadora {
    // Suma usando parámetros en la URL (request param)
    @GetMapping("/sumar")
    public int sumar(@RequestParam int a, @RequestParam int b) {
        return a + b;
    }

    // Suma usando el cuerpo de la petición (request body)
    @PostMapping("/sumar")
    public int sumarBody(@RequestBody SumaRequest request) {
        return request.getA() + request.getB();
    }

    public static class SumaRequest {
        private int a;
        private int b;
        public int getA() { return a; }
        public void setA(int a) { this.a = a; }
        public int getB() { return b; }
        public void setB(int b) { this.b = b; }
    }
}
```

### Probar con Postman

#### 1. Suma con parámetros en la URL (GET)
- **Método:** GET
- **URL:** `http://localhost:8081/calculadora/sumar?a=5&b=3`
- **Respuesta esperada:** `8`

#### 2. Suma con cuerpo (POST)
- **Método:** POST
- **URL:** `http://localhost:8081/calculadora/sumar`
- **Body:** (raw, JSON)
```json
{
  "a": 7,
  "b": 2
}
```
- **Respuesta esperada:** `9`

---

## Resumen visual de canales de información en HTTP

| Lugar   | Tipo de info    | Cuándo usar               |
| ------- | --------------- | ------------------------- |
| Body    | Datos complejos | Crear/actualizar recursos |
| Query   | Filtros         | Consultas, búsquedas      |
| Path    | Identificadores | Recursos específicos      |
| Headers | Metadatos       | Auth, configuración       |
| Cookies | Sesión          | Estado de usuario         |

---

## Reglas clave para el análisis y desarrollo de APIs

- Usa el **body** para enviar datos complejos o crear/actualizar recursos.
- Usa **query params** para filtros y búsquedas.
- Usa **path params** para identificar recursos únicos.
- Usa **headers** para autenticación y configuración.
- Usa **cookies** para manejar sesiones y estado del usuario.

---

> **Nota:** Estos conceptos y ejemplos son fundamentales para el desarrollo de microservicios y APIs RESTful en el contexto de la competencia SENA: _Desarrollar la solución de software de acuerdo con el diseño y metodologías de desarrollo_.
- Datos completos del usuario
- Información sensible (password)

---

### ✅ Ejemplo (actualizar datos)

```
PATCH /usuarios/123{  "email": "nuevo@email.com"}
```

---

### ✅ Ejemplo (subir archivo)

```
POST /uploadContent-Type: multipart/form-data
```

👉 Envías:

- Imágenes
- PDFs

---

### 📌 Ideal para:

- Formularios
- JSON complejo
- Archivos
- Datos sensibles

---

# 🔹 3. Información por **HEADERS**

📍 Metadatos (no es el “contenido principal”)  
📍 Controlan **cómo** se procesa la petición

---

### ✅ Ejemplo

```
GET /usuariosAuthorization: Bearer abc123Content-Type: application/jsonAccept: application/json
```

👉 Envías:

- 🔐 `Authorization` → token/login
- 📦 `Content-Type` → tipo de body
- 📥 `Accept` → qué respuesta quieres

---

### 📌 Otros ejemplos comunes:

```
User-Agent: ChromeCache-Control: no-cache
```

---

# 🔥 Comparación directa (muy importante)

|Dónde|Ejemplo|Tipo de info|Cuándo usar|
|---|---|---|---|
|URL|`/users?age=20`|Filtros, IDs|Consultar|
|Body|`{ "name": "Juan" }`|Datos completos|Crear/editar|
|Header|`Authorization: Bearer`|Config/Auth|Control|

---

# 🧠 Ejemplo completo combinado (nivel pro)

```
POST /usuarios?ref=googleAuthorization: Bearer abc123Content-Type: application/json{  "nombre": "Juan",  "email": "juan@email.com"}
```

👉 Aquí:

- URL → `ref=google` (tracking/filtro)
- Header → autenticación
- Body → datos del usuario

---

# 🔑 Regla rápida (para examen o práctica)

- 🌐 **URL → qué recurso / filtros**
- 📦 **Body → datos**
- 🏷️ **Headers → reglas / contexto**

---

Si quieres, en el siguiente paso te hago un **mapa mental tipo “backend real”** (qué usar en login, registro, APIs REST, etc.) que es justo lo que te piden en entrevistas o proyectos.

Probar con Postman la suma con request para y con request body