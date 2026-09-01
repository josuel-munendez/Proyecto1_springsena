# 📐 Arquitectura, Patrones de Diseño y Buenas Prácticas
### Documentación maestra — Proyecto 1: Spring Boot + JDBC + MySQL

> Guía única para la sustentación: qué hace cada capa, dónde están los
> patrones, qué buenas prácticas se aplicaron y por qué.

---

## 1. Visión general — 3 microservicios independientes

| Microservicio | Carpeta | Tecnología | Puerto | Base de datos | Tabla |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **ms-parqueadero** (Vehículos) | `vehiculos/` | Capas + JPA/Repository | 8080 | H2 (memoria) | vehiculos |
| **ms-usuarios** | `usuarios/` | Spring Boot + JDBC puro | 8081 | `mi_base_datos` | usuario |
| **ms-productos** | `productos/` | Spring Boot + JDBC puro | 8082 | `db_productos` | producto |

> **Nota de consistencia:** `vehiculos` usa **JPA / Spring Data** (`JpaRepository`) y vistas
> **Thymeleaf** (MVC), a diferencia de usuarios/productos (JDBC puro + `fetch`). Detalle en
> `SPRING_REST_DOCUMENTATION.md` (sección 14) y `MICROSERVICIOS.md`.

**¿Por qué son microservicios?** Cada uno es un proyecto Maven independiente con su propio proceso, su propio puerto y su propia base de datos (**patrón Database-per-Service**). Si uno cae, los demás siguen vivos. La evidencia física de la separación: puertos distintos + BD distintas.

```
   [Navegador]                [Postman]
       │ fetch                     │ HTTP
       ▼                           ▼
 ┌──────────────┐  ┌──────────────┐  ┌──────────────┐
 │ ms-usuarios  │  │ ms-productos │  │ ms-          │
 │ :8081        │  │ :8082        │  │ parqueadero  │
 │ mi_base_datos│  │ db_productos │  │ :8080        │
 └──────────────┘  └──────────────┘  └──────────────┘
  Procesos separados · BD separadas · Deploy independiente
```

---

## 2. Programación por capas (arquitectura n-tier)

Cada microservicio divide su código en capas con UNA responsabilidad cada una:

| Capa | Paquete | Responsabilidad | NO hace |
| :--- | :--- | :--- | :--- |
| **Modelo** | `models` / `entity` | Entidad POJO que mapea la tabla: estado + getters/setters | No tiene lógica ni SQL |
| **Negocio** | `businesslogic` | Valida reglas del negocio y orquesta operaciones | No escribe SQL ni conoce HTTP |
| **Persistencia** | `persistence` / `repository` | Todo el SQL y el acceso a datos (DAO) | No decide reglas de negocio |
| **Control** | `controler` | Expone endpoints HTTP REST; traduce HTTP ↔ Java | No valida reglas ni toca datos |
| **Vista** | `static/index.html` | Frontend HTML+JS que consume la API con `fetch` | No conoce Java ni MySQL |

**Regla de oro:** una capa solo habla con la capa vecina.
`Frontend → Controller → BL → Persistence → BD`

Esto produce:
- **Alta cohesión**: cada clase hace una sola cosa bien.
- **Bajo acoplamiento**: cambiar el SQL no rompe el frontend; cambiar el frontend no rompe el negocio.
- **Reutilización**: la misma API sirve al navegador, a Postman o a otro microservicio.

### Equivalencia MVC/MVT (Django) ↔ Spring Boot

| Django (MVT) | Este proyecto |
| :--- | :--- |
| `models.py` (modelo + ORM) | `models/Producto.java` + `persistence/ProductoPersistence.java` |
| `views.py` (vistas/lógica) | `controler/ControllerProducto.java` + `businesslogic/BLProducto.java` |
| Templates HTML | `static/index.html` (HTML estático + JSON como vista de la API) |
| URLs (`urls.py`) | Anotaciones `@RequestMapping` / `@GetMapping`... |
| ORM (`objects.filter()`) | `PreparedStatement` + `ResultSet` (JDBC manual) |

---

## 3. Recorrido de una petición (ejemplo: crear producto)

```
1. Usuario llena el formulario en index.html y presiona "Guardar".
2. JS construye el objeto producto y lo serializa: JSON.stringify().
3. fetch(BASE_URL, {method:"POST", body: json}) → petición HTTP.
4. DispatcherServlet (Front Controller de Spring) lee ruta+verbo y
   elige ControllerProducto.guardar().
5. @RequestBody convierte el JSON → objeto Producto (Jackson usa el
   constructor vacío + setters).
6. El controller delega: bl.guardarProducto(p). No valida nada él.
7. BLProducto.validarProducto(p): null? ¿nombre vacío? ¿precio < 0?
   Si algo falla → return false (nunca llega a la BD).
8. Si es válido → persistence.guardarProducto(p).
9. Se abre conexión (DriverManager), se prepara INSERT con `?`,
   se asignan parámetros y executeUpdate() inserta la fila.
10. try-with-resources cierra Connection/Statement solos.
11. true sube por las capas y Spring lo serializa a JSON: "true".
12. JS recibe true, muestra "Producto creado", limpia formulario,
    llama listar() para refrescar la tabla (GET).
```

---

## 4. Patrones de diseño aplicados

| # | Patrón | ¿Dónde? | Qué hace y cómo funciona aquí |
| :-- | :--- | :--- | :--- |
| 1 | **MVC (Model-View-Controller)** | Toda la arquitectura | Separa datos (Model = entidad+persistencia+BL), interfaz (View = JSON + index.html) y control (Controller = @RestController). Cada cambio de interfaz no afecta los datos y viceversa. |
| 2 | **Arquitectura en capas (n-tier)** | Los 4 paquetes por servicio | Cada responsabilidad vive en una capa con comunicación solo hacia la capa vecina. Facilita mantenimiento, pruebas y trabajo en equipo. |
| 3 | **DAO / Repository** | `UsuarioPersistency`, `ProductoPersistence`, `VehiculoRepository` | Intermediario entre aplicación y BD. usuarios/productos usan DAO JDBC puro (`guardarUsuario`) y ocultan SQL/conexiones; `VehiculoRepository` es un **`JpaRepository`** de Spring Data (JPA) que Spring implementa automáticamente. Cambiar de motor de BD solo toca estas clases. |
| 4 | **Front Controller** | `DispatcherServlet` de Spring MVC (implícito) | TODAS las peticiones entran por un único servlet "recepcionista" que consulta el mapa de rutas (`@RequestMapping`) y despacha al método handler correcto. Nosotros solo escribimos los handlers. |
| 5 | **Inversión de Control (IoC) + Inyección de Dependencias** | Controllers y BLs | Las clases no buscan sus dependencias: las RECIBEN por constructor (`BLProducto(ProductoPersistence p)`). Beneficio: bajo acoplamiento y pruebas inyectando mocks. Spring automatiza esto con su contenedor (@Autowired); aquí se hizo manual para entender el fundamento. |
| 6 | **Singleton (bean de Spring)** | Controllers | Spring crea UNA instancia por controller y la reutiliza en todas las peticiones. Por eso los controllers son sin estado (solo campos `final`): seguros ante concurrencia. |
| 7 | **POJO / JavaBean** | Entidades | Convención atributos privados + constructores + getters/setters estándar. Jackson funciona POR REFLEXIÓN sobre esas convenciones: constructor vacío obligatorio para deserializar JSON. Getters booleanos `isActivo()` → clave `"activo"`. |
| 8 | **Fachada (Facade), ligero** | Clases BL | Para el Controller el BL es una interfaz simple (guardar/listar...) que esconde el flujo validar→SQL. El controller no sabe que existen validaciones ni JDBC. |
| 9 | **Database-per-Service** | Configuración de cada MS | Cada microservicio tiene SU base de datos exclusiva (mi_base_datos / db_productos / H2). Desacopla los esquemas: nadie toca las tablas del otro. |
| 10 | **Repository (Spring Data JPA)** | `VehiculoRepository` | Interfaz que extiende `JpaRepository`: Spring genera automáticamente la implementación (findAll, save, deleteById, findByPlaca). A diferencia del DAO JDBC de usuarios/productos, aquí no hay SQL manual. |

---

## 5. Buenas prácticas aplicadas

| # | Práctica | ¿Dónde? | Qué hace y por qué |
| :-- | :--- | :--- | :--- |
| 1 | **Encapsulamiento** | Todas las entidades | Atributos `private` + acceso por getters/setters: protege el estado interno (principio OOP) y permite validar/controlar el acceso. |
| 2 | **Separation of Concerns / SRP** | Las 4 capas | Cada clase tiene UNA razón para cambiar. Un bug de SQL se arregla en persistencia sin tocar frontend ni controllers. |
| 3 | **Prevención de SQL Injection** | Persistencias | `PreparedStatement` con parámetros `?`: los valores viajan separados del SQL y el driver los trata como DATOS. Concatenar strings permitiría `'; DROP TABLE producto; --`. |
| 4 | **try-with-resources** | Toda la persistencia | Cierra automáticamente Connection/Statement/ResultSet aunque haya excepción. Sin esto, las fugas de conexiones tumban el servidor. |
| 5 | **Validación doble (Defense in Depth)** | Frontend JS + BL | El navegador valida (campos vacíos, precio negativo, correo con @) PERO el backend re-valida: Postman/curl pueden saltarse el HTML. Nunca confiar en el cliente. |
| 6 | **Guard clauses + Fail fast** | Métodos validar*() | Validaciones planas que retornan de inmediato en vez de ifs anidados ("flecha de la muerte"): más legible y cada regla independiente. También evitan NPE verificando nulos ANTES de llamar métodos. |
| 7 | **DRY (Don't Repeat Yourself)** | `mapearFila()` | La conversión ResultSet→objeto existía repetida en listar() y obtener(); ahora vive una sola vez. Agregar columna nueva = cambiar en un único lugar. |
| 8 | **Thin controller (controller delgado)** | Controllers | Sin SQL ni reglas: reciben → delegan → devuelven. La lógica vive en el BL y es reutilizable desde cualquier cliente. |
| 9 | **Semántica REST correcta** | Controllers | GET leer, POST crear, PUT actualizar, DELETE eliminar. Contrato predecible y cacheable. |
| 10 | **Convenciones de nombrado** | Todo el código | camelCase en Java, paquetes coherentes, getters booleanos `isXxx()`. Se corrigieron identificadores con tilde/guion bajo del borrador (`fecha_actualización` → `fechaActualizacion`). |
| 11 | **Documentación Javadoc** | Todos los públicos | Estándar de la industria; genera HTML y explica contrato de cada método (parámetros, retornos, reglas). |
| 12 | **Configuración externalizada** | `application.properties` | Puerto y credenciales documentados fuera del código hardcodeado de lógica (pendiente migrar el DriverManager a @Value). |
| 13 | **Responsabilidad en quien la resuelve mejor** | `actualizarProducto()` | La fecha de actualización la refresca MySQL (`ON UPDATE CURRENT_TIMESTAMP`): Java no duplica esa lógica. |
| 14 | **Retornos seguros** | listar*() | Devuelven lista VACÍA (nunca null) cuando no hay datos o falla la conexión: el frontend puede iterar sin romperse. |

---

## 6. Seguridad aplicada vs pendiente

**Aplicado hoy**
- ✅ Sentencias parametrizadas (anti SQL Injection).
- ✅ Validación en backend además del frontend.
- ✅ Credenciales fuera de la lógica de negocio (en la capa de persistencia).

**Pendiente (fase 2 — ya identificado en TAREAS_PENDIENTES.md)**
- ⏳ `@Value` para inyectar URL/usuario/contraseña desde application.properties.
- ⏳ Logger SLF4J en lugar de `System.out.println` / `printStackTrace`.
- ⏳ BCrypt para contraseñas + DTOs que oculten campos sensibles.
- ⏳ Paginación (`LIMIT ? OFFSET ?`) y códigos de estado HTTP completos (ResponseEntity).

---

## 7. Cómo correr cada microservicio

```bash
# 1) Crear las bases de datos (una sola vez):
#    - ejecutar usuarios/src/main/resources/schema.sql  → mi_base_datos
#    - ejecutar productos/src/main/resources/schema.sql → db_productos

# 2) Arrancar (cada uno en su terminal):
cd usuarios && ./mvnw spring-boot:run   # http://localhost:8081
cd productos && ./mvnw spring-boot:run   # http://localhost:8082
cd vehiculos && ./mvnw spring-boot:run   # http://localhost:8080

# 3) Abrir en el navegador la raíz de cada puerto (index.html)
#    y/o probar la API desde Postman:
#    GET/POST/PUT  http://localhost:8081/api/usuarios[/{id}]
#    GET/POST/PUT  http://localhost:8082/api/productos[/{id}]
```

---

## 8. Glosario rápido

| Término | Significado |
| :--- | :--- |
| **Bean** | Objeto creado y administrado por el contenedor de Spring. |
| **IoC** | Inversión de Control: las dependencias las provee quien usa la clase (o Spring), no la clase misma. |
| **Jackson** | Librería que convierte JSON ↔ objetos Java usando getters/setters. |
| **DispatcherServlet** | Front Controller de Spring: recibe todo el HTTP y despacha. |
| **ResultSet** | Cursor de JDBC que recorre las filas devueltas por un SELECT. |
| **POJO** | Objeto Java plano, sin dependencias de frameworks. |
| **Stub** | Implementación provisional con datos fijos, para poder probar. |
| **fetch / AJAX** | Peticiones HTTP desde JavaScript sin recargar la página. |
