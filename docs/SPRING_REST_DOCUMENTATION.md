# Documentación Spring REST — Proyecto1_springsena

> **Alcance:** Solo se documentan conceptos, anotaciones y patrones que **existen** en el proyecto.
> Los que no aparecen (ej. `@CrossOrigin`, `@Valid`, `@RequestHeader`, `@PatchMapping`, `ResponseEntity`, `@Autowired`) se omiten intencionalmente.

---

## Tabla de Contenidos

1. [Visión General del Proyecto](#1-visión-general-del-proyecto)
2. [Anotaciones de Clase (Class-Level)](#2-anotaciones-de-clase-class-level)
3. [Anotaciones de Método HTTP (Method-Level)](#3-anotaciones-de-método-http-method-level)
4. [Anotaciones de Parámetros (Parameter-Level)](#4-anotaciones-de-parámetros-parameter-level)
5. [Mapeo CRUD ↔ HTTP ↔ Método](#5-mapeo-crud--http--método)
6. [Endpoints Expuestos (Rutas)](#6-endpoints-expuestos-rutas)
7. [Patrones de Diseño](#7-patrones-de-diseño)
8. [Retorno de Datos y Serialización JSON](#8-retorno-de-datos-y-serialización-json)
9. [Inyección de Dependencias en Controladores](#9-inyección-de-dependencias-en-controladores)
10. [Validación](#10-validación)
11. [Arquitectura por Capas](#11-arquitectura-por-capas)
12. [Conceptos HTTP](#12-conceptos-http)
13. [Glosario de Anotaciones](#13-glosario-de-anotaciones)

---

## 1. Visión General del Proyecto

El proyecto contiene **3 microservicios independientes**, cada uno con su propio `pom.xml`, base de datos y puerto. Todos usan **Java 21**, **Spring Boot 4.1.x**, **spring-boot-starter-webmvc** y **JDBC puro** (sin JPA/Hibernate).

| Microservicio | Directorio | Puerto | Controlador | Ruta Base |
|---|---|---|---|---|
| `ms-parqueadero` | `demo/` | 8080 | `ControllerVehiculo` | `/ControllerVehiculo` |
| `ms-usuarios` | `beta/` | 8081 | `UsuarioController` | `/api/usuarios` |
| `ms-productos` | `productos/` | 8082 | `ControllerProducto` | `/api/productos` |

**Dependencia Maven común:** `spring-boot-starter-webmvc` — trae implícitamente:
- Spring MVC ( DispatcherServlet )
- Tomcat embebido
- Jackson (serialización JSON)
- Todas las anotaciones de `org.springframework.web.bind.annotation.*`

---

## 2. Anotaciones de Clase (Class-Level)

### 2.1 `@RestController`

Marca la clase como manejadora de peticiones HTTP REST. Es equivalente a `@Controller` + `@ResponseBody` combinados.

**Qué hace internamente:**
- Spring registra la clase como un **bean singleton** en el contenedor IoC.
- Cada método público con anotación de mapping devuelve su objeto **serializado a JSON** automáticamente (por Jackson, usando los getters del JavaBean).
- El `DispatcherServlet` usa estas anotaciones como mapa para enrutar cada petición al método correcto.

**Uso en el proyecto:**

| Controlador | Archivo | Línea |
|---|---|---|
| `ControllerVehiculo` | `demo/.../controler/ControllerVehiculo.java` | `@RestController("")` |
| `UsuarioController` | `beta/.../controler/UsuarioController.java` | `@RestController` |
| `ControllerProducto` | `productos/.../controler/ControllerProducto.java` | `@RestController` |

> **Nota:** `@Controller` (sin "Rest") **no se usa** en ningún controlador del proyecto.

### 2.2 `@RequestMapping`

Define un **prefijo común** para todas las rutas de la clase. Cada método agrega su ruta específica debajo de este prefijo.

**Uso en el proyecto:**

| Controlador | Valor | Ruta resultante |
|---|---|---|
| `ControllerVehiculo` | `@RequestMapping("/ControllerVehiculo")` | Todas las rutas empiezan con `/ControllerVehiculo/...` |
| `UsuarioController` | `@RequestMapping("/api/usuarios")` | Todas las rutas empiezan con `/api/usuarios/...` |
| `ControllerProducto` | `@RequestMapping("/api/productos")` | Todas las rutas empiezan con `/api/productos/...` |

**Ejemplo:**
```java
@RestController
@RequestMapping("/api/usuarios")   // ← prefijo de clase
public class UsuarioController {
    @GetMapping          // → GET /api/usuarios
    @GetMapping("/{id}") // → GET /api/usuarios/{id}
    @PostMapping         // → POST /api/usuarios
}
```

---

## 3. Anotaciones de Método HTTP (Method-Level)

Cada anotación asocia un **método Java** con un **verbo HTTP** específico. Spring las usa para encontrar el handler correcto cuando llega una petición.

### 3.1 `@GetMapping`

Mapea peticiones **HTTP GET** — se usa para **leer/consultar** datos (operación Read en CRUD).

| Controlador | Ruta | Método | Archivo:Línea |
|---|---|---|---|
| `ControllerVehiculo` | `GET /ControllerVehiculo/consultar?placa=ABC` | `consultarVehiculo(@RequestParam String placa)` | `ControllerVehiculo.java:73` |
| `UsuarioController` | `GET /api/usuarios` | `listar()` | `UsuarioController.java:72` |
| `UsuarioController` | `GET /api/usuarios/{id}` | `obtener(@PathVariable Long id)` | `UsuarioController.java:84` |
| `ControllerProducto` | `GET /api/productos` | `listar()` | `ControllerProducto.java:70` |
| `ControllerProducto` | `GET /api/productos/{id}` | `obtener(@PathVariable Long id)` | `ControllerProducto.java:82` |

### 3.2 `@PostMapping`

Mapea peticiones **HTTP POST** — se usa para **crear** recursos (operación Create en CRUD).

| Controlador | Ruta | Método | Archivo:Línea |
|---|---|---|---|
| `UsuarioController` | `POST /api/usuarios` | `guardar(@RequestBody Usuario u)` | `UsuarioController.java:97` |
| `ControllerProducto` | `POST /api/productos` | `guardar(@RequestBody Producto p)` | `ControllerProducto.java:95` |

### 3.3 `@PutMapping`

Mapea peticiones **HTTP PUT** — se usa para **actualizar** recursos completos (operación Update en CRUD).

| Controlador | Ruta | Método | Archivo:Línea |
|---|---|---|---|
| `UsuarioController` | `PUT /api/usuarios` | `actualizar(@RequestBody Usuario u)` | `UsuarioController.java:109` |
| `ControllerProducto` | `PUT /api/productos` | `actualizar(@RequestBody Producto p)` | `ControllerProducto.java:107` |

### 3.4 `@DeleteMapping`

Mapea peticiones **HTTP DELETE** — se usa para **eliminar** recursos (operación Delete en CRUD).

| Controlador | Ruta | Método | Archivo:Línea |
|---|---|---|---|
| `ControllerVehiculo` | `DELETE /ControllerVehiculo/eliminar?placa=ABC` | `deleteVehiculo(@RequestParam String placa)` | `ControllerVehiculo.java:56` |
| `UsuarioController` | `DELETE /api/usuarios/{id}` | `eliminar(@PathVariable Long id)` | `UsuarioController.java:121` |
| `ControllerProducto` | `DELETE /api/productos/{id}` | `eliminar(@PathVariable Long id)` | `ControllerProducto.java:119` |

### 3.5 Anotaciones NO usadas

| Anotación | Uso en proyecto | Descripción (para referencia) |
|---|---|---|
| `@PatchMapping` | **0 usos** | Actualización parcial de un recurso |
| `@RequestMapping` en métodos | **0 usos** | Solo se usa a nivel de clase |

---

## 4. Anotaciones de Parámetros (Parameter-Level)

### 4.1 `@RequestBody`

Captura el **cuerpo (body) de la petición HTTP** y lo deserializa a un objeto Java usando Jackson.

**Cómo funciona:**
1. El cliente envía un JSON en el body (ej. `{"nombre":"Ana","correo":"ana@mail.com"}`).
2. Spring lee el Content-Type (`application/json`).
3. Jackson usa el **constructor vacío** + **setters** del JavaBean para construir el objeto.
4. El objeto se inyecta como parámetro del método.

**Uso en el proyecto:**

| Controlador | Método | Parámetro | Archivo:Línea |
|---|---|---|---|
| `UsuarioController` | `guardar()` | `@RequestBody Usuario u` | `UsuarioController.java:98` |
| `UsuarioController` | `actualizar()` | `@RequestBody Usuario u` | `UsuarioController.java:110` |
| `ControllerProducto` | `guardar()` | `@RequestBody Producto p` | `ControllerProducto.java:96` |
| `ControllerProducto` | `actualizar()` | `@RequestBody Producto p` | `ControllerProducto.java:108` |

**Ejemplo de petición:**
```http
POST /api/usuarios
Content-Type: application/json

{
    "nombre": "Ana García",
    "correo": "ana@mail.com",
    "telefono": 1234567,
    "saldo": 50000
}
```

### 4.2 `@PathVariable`

Captura un **fragmento de la ruta URL** que está entre llaves `{}` y lo convierte al tipo declarado.

**Cómo funciona:**
1. La ruta del método contiene `{id}` como placeholder.
2. Cuando llega `/api/usuarios/5`, Spring extrae `5`.
3. Spring **convierte automáticamente** el String al tipo del parámetro (Long, int, etc.).
4. Si el valor no es convertible (ej. `/api/usuarios/abc`), responde **400 Bad Request** automáticamente.

**Uso en el proyecto:**

| Controlador | Método | Parámetro | Archivo:Línea |
|---|---|---|---|
| `UsuarioController` | `obtener()` | `@PathVariable Long id` | `UsuarioController.java:85` |
| `UsuarioController` | `eliminar()` | `@PathVariable Long id` | `UsuarioController.java:122` |
| `ControllerProducto` | `obtener()` | `@PathVariable Long id` | `ControllerProducto.java:83` |
| `ControllerProducto` | `eliminar()` | `@PathVariable Long id` | `ControllerProducto.java:120` |

**Ejemplo de petición:**
```http
GET /api/usuarios/5
DELETE /api/productos/12
```

### 4.3 `@RequestParam`

Captura un **parámetro de query string** de la URL (lo que va después de `?`).

**Cómo funciona:**
1. El cliente envía `?placa=ABC123` al final de la URL.
2. Spring extrae el valor y lo asigna al parámetro del método.
3. Si el parámetro no está presente y es opcional, se usa el valor por defecto.

**Uso en el proyecto:**

| Controlador | Método | Parámetro | Archivo:Línea |
|---|---|---|---|
| `ControllerVehiculo` | `deleteVehiculo()` | `@RequestParam String placa` | `ControllerVehiculo.java:57` |
| `ControllerVehiculo` | `consultarVehiculo()` | `@RequestParam String placa` | `ControllerVehiculo.java:74` |

**Ejemplo de petición:**
```http
DELETE /ControllerVehiculo/eliminar?placa=ABC123
GET /ControllerVehiculo/consultar?placa=XYZ789
```

> **Diferencia clave:** `@PathVariable` extrae de la ruta (`/api/usuarios/{id}`), mientras que `@RequestParam` extrae del query string (`?placa=ABC`).

### 4.4 Anotaciones de parámetros NO usadas

| Anotación | Uso | Descripción (referencia) |
|---|---|---|
| `@RequestHeader` | **0 usos** | Captura un header HTTP específico |
| `@RequestPart` | **0 usos** | Captura una parte de multipart request (archivos) |
| `@RequestAttribute` | **0 usos** | Captura un atributo del request |
| `@ModelAttribute` | **0 usos** | Captura datos de formularios HTML |

---

## 5. Mapeo CRUD ↔ HTTP ↔ Método

### Tabla Resumen de los 3 Controladores

```
┌──────────────────┬────────────┬──────────────────────┬────────────────────────┐
│   Operación CRUD │ Verbo HTTP │ Ruta                 │ Método Java            │
├──────────────────┼────────────┼──────────────────────┼────────────────────────┤
│   CREATE         │ POST       │ /api/usuarios        │ guardar(@RequestBody)  │
│   READ (uno)     │ GET        │ /api/usuarios/{id}   │ obtener(@PathVariable) │
│   READ (todos)   │ GET        │ /api/usuarios        │ listar()               │
│   UPDATE         │ PUT        │ /api/usuarios        │ actualizar(@RequestBody)│
│   DELETE         │ DELETE     │ /api/usuarios/{id}   │ eliminar(@PathVariable)│
├──────────────────┼────────────┼──────────────────────┼────────────────────────┤
│   READ (uno)     │ GET        │ /api/productos/{id}  │ obtener(@PathVariable) │
│   READ (todos)   │ GET        │ /api/productos       │ listar()               │
│   CREATE         │ POST       │ /api/productos       │ guardar(@RequestBody)  │
│   UPDATE         │ PUT        │ /api/productos       │ actualizar(@RequestBody)│
│   DELETE         │ DELETE     │ /api/productos/{id}  │ eliminar(@PathVariable)│
├──────────────────┼────────────┼──────────────────────┼────────────────────────┤
│   DELETE         │ DELETE     │ /ControllerVehiculo/eliminar | deleteVehiculo(@RequestParam) │
│   READ (uno)     │ GET        │ /ControllerVehiculo/consultar | consultarVehiculo(@RequestParam) │
└──────────────────┴────────────┴──────────────────────┴────────────────────────┘
```

**Total de endpoints HTTP: 12**

---

## 6. Endpoints Expuestos (Rutas)

### ms-usuarios (beta) — Puerto 8081

| Método | Ruta | Descripción | Ejemplo Body JSON |
|---|---|---|---|
| `GET` | `/api/usuarios` | Lista todos los usuarios | — |
| `GET` | `/api/usuarios/{id}` | Obtiene un usuario por ID | — |
| `POST` | `/api/usuarios` | Crea un usuario nuevo | `{"nombre":"Ana","correo":"a@a.com","telefono":123,"saldo":0}` |
| `PUT` | `/api/usuarios` | Actualiza un usuario existente | `{"id":1,"nombre":"Ana","correo":"a@a.com","telefono":456,"saldo":100}` |
| `DELETE` | `/api/usuarios/{id}` | Elimina un usuario por ID | — |

### ms-productos (productos) — Puerto 8082

| Método | Ruta | Descripción | Ejemplo Body JSON |
|---|---|---|---|
| `GET` | `/api/productos` | Lista todos los productos | — |
| `GET` | `/api/productos/{id}` | Obtiene un producto por ID | — |
| `POST` | `/api/productos` | Crea un producto nuevo | `{"nombre":"Laptop","precioBase":2500000}` |
| `PUT` | `/api/productos` | Actualiza un producto existente | `{"id":1,"nombre":"Laptop","precioBase":2000000}` |
| `DELETE` | `/api/productos/{id}` | Elimina un producto por ID | — |

### ms-parqueadero (demo) — Puerto 8080

| Método | Ruta | Descripción | Parámetros |
|---|---|---|---|
| `DELETE` | `/ControllerVehiculo/eliminar?placa=ABC` | Elimina un vehículo por placa | `?placa=ABC123` (query string) |
| `GET` | `/ControllerVehiculo/consultar?placa=ABC` | Consulta un vehículo por placa | `?placa=ABC123` (query string) |

---

## 7. Patrones de Diseño

### 7.1 Front Controller (DispatcherServlet)

Todas las peticiones HTTP no llegan "directamente" a los controladores. Primero pasan por el **DispatcherServlet** de Spring MVC, que actúa como un único punto de entrada:

```
Cliente → DispatcherServlet (Front Controller)
              ↓ usa anotaciones como mapa
         ControllerVehiculo / UsuarioController / ControllerProducto
              ↓ delega
         Business Logic (BL)
              ↓ delega
         Persistence (JDBC/MySQL)
```

Los controladores son **handlers** de ese Front Controller.

### 7.2 Singleton por Bean

Spring crea **UNA sola instancia** de cada controlador (scope singleton por defecto) y la reutiliza para todas las peticiones. Por eso los controladores deben ser **stateless** (sin variables de estado entre peticiones): solo guardan la referencia `final` a su capa de negocio.

### 7.3 Controller Delgado (Thin Controller)

Los controladores **NO** contienen:
- SQL ni acceso a base de datos
- Reglas de negocio complejas
- Validaciones complicadas

Solo hacen:
1. Recibir la petición HTTP
2. Delegar en la capa de Business Logic
3. Devolver la respuesta

La lógica vive en **un solo lugar** (BL) reutilizable desde cualquier cliente.

### 7.4 Separación de Responsabilidades (SRP)

```
Controller  → Solo traduce HTTP ↔ Java
BL          → Solo aplica reglas de negocio
Persistence → Solo ejecuta SQL
```

Cada capa tiene **una sola razón para cambiar**.

---

## 8. Retorno de Datos y Serialización JSON

### 8.1 Serialización automática con Jackson

Cuando un método de controlador retorna un objeto, **Jackson** (incluido en `spring-boot-starter-webmvc`) lo serializa a JSON automáticamente usando los **getters** del JavaBean:

```java
// En UsuarioController:
public List<Usuario> listar() {
    return bl.listarUsuarios();  // retorna List<Usuario>
}

// Spring/Jackson convierte cada Usuario → JSON:
// [{"id":1,"nombre":"Ana","direccion":"Calle 1","telefono":1234567,"correo":"ana@mail.com","saldo":50000}, ...]
```

### 8.2 Tipos de retorno usados

| Tipo de retorno | Controladores | Ejemplo |
|---|---|---|
| `void` | demo | `deleteVehiculo()` — respuesta vacía |
| `Vehiculo` (objeto directo) | demo | `consultarVehiculo()` — un objeto |
| `List<Usuario>` | beta | `listar()` — array de objetos |
| `Usuario` | beta | `obtener()` — un objeto |
| `boolean` | beta + productos | `guardar()`, `actualizar()`, `eliminar()` — true/false |
| `List<Producto>` | productos | `listar()` — array de objetos |
| `Producto` | productos | `obtener()` — un objeto |

### 8.3 Anotaciones de respuesta NO usadas

| Anotación | Uso | Descripción (referencia) |
|---|---|---|
| `ResponseEntity` | **0 usos** | Permite controlar status code, headers y body manualmente |
| `@ResponseStatus` | **0 usos** | Asocia un código HTTP específico a un método |

**Comportamiento actual:** Cuando algo falla, se retorna `null` (Spring lo serializa como JSON `null`) o `false` (se serializa como `false`). No se personaliza el código HTTP de error.

---

## 9. Inyección de Dependencias en Controladores

### 9.1 Patrón usado: Inyección manual por constructor (sin `@Autowired`)

Los 3 controladores crean sus dependencias de Business Logic **manualmente** con `new` en el constructor. **No se usa `@Autowired` en ningún sitio del proyecto.**

**UsuarioController (beta):**
```java
private final UsuarioBL bl;          // campo final, inmutable

public UsuarioController() {         // constructor sin parámetros
    this.bl = new UsuarioBL();       // creación manual con new
}
```
Archivo: `UsuarioController.java:58-63`

**ControllerProducto (productos):**
```java
private final BLProducto bl;

public ControllerProducto() {
    this.bl = new BLProducto();
}
```
Archivo: `ControllerProducto.java:57-62`

**ControllerVehiculo (demo):**
```java
// Crea una NUEVA instancia del BL en CADA petición (patrón diferente):
public void deleteVehiculo(@RequestParam String placa){
    BLVehiculo bl = new BLVehiculo();   // nueva instancia por petición
    bl.eliminarVehiculo(placa);
}
```
Archivo: `ControllerVehiculo.java:57-60`

> **Observación:** Los constructores son **sin parámetros**, por lo que Spring no realiza inyección de dependencias real. El IoC container registra los beans pero cada capa crea su dependencia manualmente con `new`.

---

## 10. Validación

### 10.1 Validación declarativa NO usada

| Anotación | Uso | Descripción (referencia) |
|---|---|---|
| `@Valid` | **0 usos** | Bean Validation (JSR 380) en parámetros |
| `@Validated` | **0 usos** | Variante de `@Valid` con grupos |

### 10.2 Validación manual (programática)

La validación se implementa de forma **manual** en la capa de Business Logic usando el patrón **Guard Clauses + Fail Fast**:

| Controlador | Clase BL | Método | Línea | Reglas |
|---|---|---|---|---|
| `ControllerVehiculo` | `BLVehiculo` | `validarVehiculo(Vehiculo v)` | BL L94 | Objeto no nulo, placa 6 chars, marca no vacía, modelo 4 chars |
| `UsuarioController` | `UsuarioBL` | `validarUsuario(Usuario u)` | BL L77 | Objeto no nulo, nombre obligatorio, correo obligatorio, saldo >= 0 |
| `ControllerProducto` | `BLProducto` | `validarProducto(Producto p)` | BL L78 | Objeto no nulo, nombre obligatorio, precio >= 0 |

**Controlador `demo` también tiene validación inline:**
```java
// ControllerVehiculo.java:75
if (placa.length() == 6) {    // validación rápida de formato
    BLVehiculo vr = new BLVehiculo();
    return vr.consultarVehiculo(placa);
}
return null;                   // placa inválida → respuesta vacía
```

---

## 11. Arquitectura por Capas

```
┌─────────────────────────────────────────────────────────────┐
│                    CAPA DE CONTROL                          │
│  ControllerVehiculo / UsuarioController / ControllerProducto │
│  ┌───────────────────────────────────────────────────────┐  │
│  │ • Recibe petición HTTP                                │  │
│  │ • Extrae parámetros (@RequestBody, @PathVariable...) │  │
│  │ • Delega en Business Logic                           │  │
│  │ • Retorna objeto serializado a JSON                  │  │
│  └───────────────────────────────────────────────────────┘  │
├─────────────────────────────────────────────────────────────┤
│                CAPA DE BUSINESS LOGIC                       │
│  BLVehiculo / UsuarioBL / BLProducto                        │
│  ┌───────────────────────────────────────────────────────┐  │
│  │ • Valida reglas de negocio                           │  │
│  │ • Coordina operaciones                               │  │
│  │ • Delega en Persistence                              │  │
│  └───────────────────────────────────────────────────────┘  │
├─────────────────────────────────────────────────────────────┤
│                CAPA DE PERSISTENCIA (DAO)                   │
│  VehiculoRepository / UsuarioPersistency / ProductoPersistence │
│  ┌───────────────────────────────────────────────────────┐  │
│  │ • Ejecuta SQL (PreparedStatement)                    │  │
│  │ • Convierte ResultSet → JavaBean (mapearFila)        │  │
│  │ • Gestiona conexiones JDBC                           │  │
│  └───────────────────────────────────────────────────────┘  │
├─────────────────────────────────────────────────────────────┤
│                    BASE DE DATOS                            │
│  MySQL:3306 (mi_base_datos / db_productos)                 │
└─────────────────────────────────────────────────────────────┘
```

**Patrón de flujo en cada petición:**
```
HTTP Request → Controller → BL → Persistence → MySQL → Response JSON
```

---

## 12. Conceptos HTTP

### 12.1 Métodos HTTP implementados

| Método | Semántica REST | Uso en proyecto |
|---|---|---|
| `GET` | Leer/consultar recursos | Listar todos, obtener uno por ID |
| `POST` | Crear un recurso nuevo | Guardar un registro nuevo |
| `PUT` | Actualizar un recurso completo | Actualizar todos los campos |
| `DELETE` | Eliminar un recurso | Eliminar por ID o por placa |

### 12.2 Códigos HTTP (comportamiento actual)

| Código | Cuándo ocurre | Ejemplo |
|---|---|---|
| `200 OK` | Respuesta exitosa (default de Spring) | `GET /api/usuarios` retorna lista |
| `400 Bad Request` | Conversión de tipo falla | `GET /api/usuarios/abc` (Long inválido) |
| `404 Not Found` | Ruta inexistente | Cualquier ruta mal escrita |

> **Nota:** No se configuran códigos HTTP manualmente (no hay `ResponseEntity` ni `@ResponseStatus`). Spring usa el `200` por defecto para todo lo exitoso.

### 12.3 Content-Type

Todos los endpoints trabajan con `application/json`:
- **Entrada:** Jackson lee JSON del body → lo convierte a JavaBean
- **Salida:** Spring convierte JavaBean → JSON en el body de respuesta

---

## 13. Glosario de Anotaciones

### Anotaciones SÍ usadas en el proyecto

| Anotación | Nivel | Paquete | Descripción |
|---|---|---|---|
| `@RestController` | Clase | `o.s.web.bind.annotation` | Marca clase como controlador REST; serializa retornos a JSON |
| `@RequestMapping` | Clase | `o.s.web.bind.annotation` | Define prefijo de rutas para todos los métodos de la clase |
| `@GetMapping` | Método | `o.s.web.bind.annotation` | Mapea peticiones HTTP GET |
| `@PostMapping` | Método | `o.s.web.bind.annotation` | Mapea peticiones HTTP POST |
| `@PutMapping` | Método | `o.s.web.bind.annotation` | Mapea peticiones HTTP PUT |
| `@DeleteMapping` | Método | `o.s.web.bind.annotation` | Mapea peticiones HTTP DELETE |
| `@RequestBody` | Parámetro | `o.s.web.bind.annotation` | Deserializa JSON del body HTTP a un objeto Java |
| `@PathVariable` | Parámetro | `o.s.web.bind.annotation` | Extrae un valor de la ruta URL (`{id}`) |
| `@RequestParam` | Parámetro | `o.s.web.bind.annotation` | Extrae un parámetro de query string (`?key=value`) |

### Anotaciones NO usadas en el proyecto (para referencia)

| Anotación | Nivel | Descripción |
|---|---|---|
| `@Controller` | Clase | Controlador MVC tradicional (retorna vistas, no JSON) |
| `@PatchMapping` | Método | Mapea peticiones HTTP PATCH (actualización parcial) |
| `@RequestHeader` | Parámetro | Captura un header HTTP específico |
| `@RequestPart` | Parámetro | Captura una parte de multipart request (archivos) |
| `@RequestAttribute` | Parámetro | Captura un atributo del request |
| `@ModelAttribute` | Parámetro | Captura datos de formularios HTML |
| `@Valid` | Parámetro | Activa Bean Validation (JSR 380) |
| `@Validated` | Clase/Parámetro | Variante de @Valid con grupos |
| `@CrossOrigin` | Clase/Método | Habilita CORS |
| `@Autowired` | Campo/Constructor | Inyección de dependencias por Spring IoC |
| `@ResponseStatus` | Clase/Método | Asocia un código HTTP específico a un método |
| `@Scope` | Clase | Define el ciclo de vida del bean (singleton, prototype...) |
| `@ComponentScan` | Clase (solo en demo) | Escanea paquetes externos para beans |
| `ResponseEntity` | Tipo de retorno | Control manual de status, headers y body |
| `@ControllerAdvice` | Clase | Manejo global de excepciones |
| `@ExceptionHandler` | Método | Captura excepciones específicas del controlador |

---

*Documentación generada el 2026-08-24 — Basada en el código fuente del proyecto Proyecto1_springsena.*
