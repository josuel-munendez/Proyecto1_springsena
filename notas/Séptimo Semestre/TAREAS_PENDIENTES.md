# ✅ Plan de Trabajo — Fin de Semana (Proyecto 1: Spring Boot + JDBC + MySQL)

**Fecha de entrega:** 24 de agosto — sustentación.

> Este es el checklist de TODO lo que falta por hacer, ordenado por prioridad.
> Cada sección explica **qué**, **por qué** y **dónde** va el cambio.

---

## 0. Estado actual (resumen de lo que YA funciona)

✅ CRUD de Usuarios con JDBC puro (`UsuarioPersistence`).
✅ Capa de negocio (`BLUsuario` / `UsuarioService`).
✅ API REST (`ControllerUsuario` → `/api/usuarios`).
✅ Entidad `Usuario` (`models`).
✅ Frontend con `fetch` (`static/index.html`).
✅ Script de BD (`schema.sql` → tabla `usuario`).

**Falta:** las mejoras de la guía del profesor 👇

---

## 1. 🔴 CRÍTICO — 3 correcciones obligatorias en `UsuarioPersistence`

> Se roban tu BD y tu nota si no lo corriges. Hacer **primero**.

### 1.1 Credenciales hardcodeadas → `@Value`
- **Problema:** `URL`, `USER`, `PASSWORD` escritas en duro en el código. Si subes esto a GitHub, se exponen.
- **Solución:** inyectarlas desde `application.properties` con `@Value`.

```java
@Value("${spring.datasource.url}")     private String url;
@Value("${spring.datasource.username}") private String user;
@Value("${spring.datasource.password}") private String password;
```
- **Dónde:** `persistence/UsuarioPersistence.java`.

### 1.2 `e.printStackTrace()` → `Logger`
- **Problema:** en producción ensucia logs y no sirve para monitoreo.
- **Solución:** usar SLF4J/Logback (ya viene con Spring Boot).

```java
private static final Logger log = LoggerFactory.getLogger(UsuarioPersistence.class);
// en el catch:
log.error("Error al guardar el usuario", e);
```

### 1.3 Paginación en `listarUsuarios()`
- **Problema:** con 1M de filas, la consulta `SELECT * FROM usuario` mata el servidor.
- **Solución:** método con `LIMIT ? OFFSET ?`.

```sql
SELECT id, nombre, direccion, telefono, correo, saldo FROM usuario LIMIT ? OFFSET ?
```
- **Dónde:** `UsuarioPersistence` (nuevo método `listarUsuariosPaginados(int limite, int offset)`) + endpoint con `page` y `size` en el Controller.

---

## 2. 🔴 CRÍTICO — Programación por capas completa

> Ya tienes la base. Reforzar que **no se mezclen capas**.

| Paquete                     | Clase                          | Estado      | Responsabilidad                   |
| :-------------------------- | :----------------------------- | :---------- | :-------------------------------- |
| `models`                    | `Usuario.java`                 | ✅           | Entidad (POJO)                    |
| `persistence`               | `UsuarioPersistence.java`      | ✅           | Acceso a datos (JDBC puro)        |
| `businesslogic` / `service` | `BLUsuario` / `UsuarioService` | ✅           | Reglas de negocio                 |
| `controler`                 | `ControllerUsuario.java`       | ✅           | Endpoints REST                    |
| `dto`                       | `UsuarioDTO.java`              | ⏳ **Crear** | Oculta `password` en el JSON      |
| `config`                    | `DatabaseConfig.java`          | ⏳ **Crear** | Centraliza la conexión (opcional) |

**Reglas a respetar:**
- El `Controller` **NO** debe tener SQL.
- El `Persistence` **NO** debe tener lógica de encriptación.
- El `Controller` llama **solo** al `BL`/`Service` (nunca directo a la persistencia).

---

## 3. 🟡 Encriptación de contraseñas (BCrypt)

- **Problema:** guardar contraseñas en texto plano = grave fallo de seguridad.
- **Solución:** BCrypt (Spring Security lo trae). Agregar dependencia en `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-crypto</artifactId>
</dependency>
```

- **Dónde va la lógica:** en el `Service`/`BL` (NUNCA en el Controller).

```java
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public boolean registrarUsuario(Usuario u) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    u.setPassword(encoder.encode(u.getPassword())); // guarda el hash, no el texto
    return persistence.guardarUsuario(u);
}
```
- **Además:** agregar el campo `password` a la entidad `Usuario` y a la tabla `usuario` (en `schema.sql`).

---

## 4. 🟡 DTO para no filtrar la contraseña

- **Problema:** si el `Service` guarda el hash de la contraseña y devuelves el `Usuario` completo, expones el hash en el JSON.
- **Solución:** crear `UsuarioDTO` **sin** el campo `password` y mapear los resultados.

```java
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String direccion;
    private int telefono;
    private String correo;
    private int saldo;
    // getters y setters (sin password)
}
```
- **Dónde:** paquete `dto`. Mapear `Usuario` → `UsuarioDTO` en el Service o Controller.

---

## 5. 🟡 Validación en Backend Y Frontend

### Backend (ya empezado en `BLUsuario`)
- [ ] Email debe contener `@`.
- [ ] Contraseña de más de 6 caracteres.
- [ ] Lanzar excepción en lugar de solo `System.out.println` + `return false`.

### Frontend (`index.html`)
- [ ] Validar antes del `fetch`: `if (nombre.length < 3) alert("Mínimo 3 caracteres")`.
- [ ] Validar email y contraseña en JavaScript.

---

## 6. 🟡 Frontend con Fetch (mejorar `index.html`)

- [ ] Añadir campo **contraseña** al formulario.
- [ ] Consumir los endpoints **con paginación**: `GET /api/usuarios?page=1&size=5`.
- [ ] Mostrar controles de paginación (anterior / siguiente).
- [ ] No mostrar el campo `password` en la tabla (usar DTO).

---

## 7. 🟡 Paginación de datos (end-to-end)

**Flujo completo:**
1. `Controller`: `@GetMapping` recibe `page` y `size`.
   ```java
   @GetMapping
   public List<UsuarioDTO> listar(@RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "5") int size) {
       return bl.listarPaginado(page, size);
   }
   ```
2. `Service`: calcula `offset = (page - 1) * size`.
3. `Persistence`: usa `LIMIT ? OFFSET ?`.

---

## 8. 🟢 Arquitectura de Microservicios (entender + sustentar)

> ⚠️ **Importante:** tu proyecto **actual es monolítico por capas** (todo corre en un solo proceso / un `.jar`). NO es un microservicio por sí solo.

**Para sustentar "microservicios" necesitas:**
- Dos proyectos Spring Boot **independientes** (mismo repo o distintos):
  - **`ms-usuarios`** → JDBC puro → puerto **8081** → BD `mi_base_datos`.
  - **`ms-parqueadero`** → JPA (Repository) → puerto **8080** → BD propia.
- Cada uno con **su propia base de datos** y **su propia arquitectura por capas**.
- Comunicación entre ellos vía **REST** con `RestTemplate`:

```java
// En ms-parqueadero (consumidor)
@Service
public class ClienteUsuarioService {
    private final RestTemplate restTemplate;

    public int consultarSaldo(Long idUsuario) {
        String url = "http://localhost:8081/api/usuarios/" + idUsuario + "/saldo";
        ResponseEntity<Integer> r = restTemplate.getForEntity(url, Integer.class);
        return r.getStatusCode().is2xxSuccessful() ? r.getBody() : 0;
    }
}
```
- Registrar el bean de `RestTemplate`:
  ```java
  @Bean
  public RestTemplate restTemplate() { return new RestTemplate(); }
  ```
- En cada `application.properties`: `server.port` distinto y BD distinta. **Eso es la evidencia física de microservicios.**

> Para el trimestre, con dos proyectos en puertos distintos y comunicación REST es suficiente (no hace falta Gateway).

---

## 9. 🟢 Documentación con Javadoc

- [ ] Comentarios `/** ... */` en cada método público de todas las clases.
- [ ] Explicar qué hace cada capa en el encabezado de cada clase.

---

## 10. 🟢 Pruebas con Postman

- [ ] Crear una colección con los 4 verbos HTTP: GET, POST, PUT, DELETE.
- [ ] Body JSON de ejemplo para POST/PUT.
- [ ] Tomar capturas de pantalla para la sustentación.

---

## 11. 🟢 Resumen: requisito → solución

| Requisito del profesor | Cómo resolverlo |
| :--- | :--- |
| JDBC puro | ✅ Ya: `DriverManager`, `Connection`, `PreparedStatement`, `ResultSet` |
| Encriptación | BCrypt en la capa Service/BL (sección 3) |
| Microservicios | Dos proyectos, puertos distintos, REST + `RestTemplate` (sección 8) |
| Consumo con fetch | ✅ Ya + mejorar con paginación y password (sección 6) |
| Validación front/back | Backend en Service + frontend en JS (sección 5) |
| Paginación | `LIMIT ? OFFSET ?` + parámetros `page`/`size` (sección 7) |
| Documentación | Javadoc (sección 9) |
| Pruebas Postman | Colección + capturas (sección 10) |

---

## 📋 Orden sugerido de trabajo (fin de semana)

1. Corregir las **3 cosas críticas** de `UsuarioPersistence` (sección 1).
2. Crear `UsuarioDTO` y el campo `password` (secciones 3 y 4).
3. Implementar **BCrypt** en el Service (sección 3).
4. Implementar **paginación** end-to-end (sección 7).
5. **Validaciones** backend + frontend (sección 5).
6. Mejorar **index.html** (sección 6).
7. Montar el **segundo microservicio** (Parqueadero) y la comunicación REST (sección 8).
8. **Javadoc** + **Postman** + capturas (secciones 9 y 10).
9. Repasar y **sustentar**.
