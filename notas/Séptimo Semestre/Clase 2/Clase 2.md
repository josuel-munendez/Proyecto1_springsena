# Séptimo Semestre — Clase 2

**Fecha:** 3 de agosto de 2026

---

## 🛠️ Lo que hicimos en clase

Continuamos el **Proyecto 1** (Spring Boot + JDBC + MySQL). Hoy trabajamos en **refactorización de entidades** y empezamos la **programación por capas** (`entity` → `businesslogic` → `controler`).

### 1. Refactorización de la entidad `Producto` (proyecto RED)

Aplicamos en código lo aprendido en la Clase 1 sobre getters de booleanos (estándar JavaBeans).

**Antes (Clase 1):**
```java
protected boolean is_activo;
protected boolean is_aprovado;

public boolean getIs_activo()   { return is_activo; }
public boolean getIs_aprovado() { return is_aprovado; }
```

**Después (Clase 2):**
```java
protected boolean activo;
protected boolean aprovado;

public boolean is_activo()   { return activo; }
public boolean is_aprovado() { return aprovado; }
```

- Renombramos `is_activo` → `activo` y `is_aprovado` → `aprovado` (se elimina el prefijo `is` del nombre de la variable).
- Los getters pasaron de `getIs_...()` a `is_...()`, cumpliendo el estándar JavaBeans y evitando conflictos con Jackson.
- Se agregaron **constructores**: uno vacío y uno con todos los parámetros.

### 2. Refactorización de la entidad `Vehiculo` (proyecto Parqueadero)

**Antes:** atributos `protected`. **Después:** atributos `private` (encapsulamiento) + constructores.

```java
private long id;
private String placa;
private String marca;
private String modelo;
private String propetario;

public Vehiculo() { }

public Vehiculo(long id, String propetario, String modelo, String marca, String placa) {
    this.id = id;
    this.propetario = propetario;
    this.modelo = modelo;
    this.marca = marca;
    this.placa = placa;
}
```

### 3. Capa de negocio: `BLVehiculo` (business logic)

Nueva clase en el paquete `Parqueadero.businesslogic` con las validaciones de datos del vehículo:

```java
package Parqueadero.businesslogic;

import Parqueadero.entity.Vehiculo;

public class BLVehiculo {

    public boolean validarVehiculo(long id, String placa, String modelo) {
        return false;   // ⏳ pendiente de implementar
    }

    public boolean validarVehiculo(Vehiculo v) {
        if (v != null) {
            if (v.getPlaca().length() == 6) {
                if (!v.getMarca().isBlank()) {
                    if (v.getModelo().length() == 4) {
                        return true;
                    } else {
                        System.out.println("Modelo invalido");
                    }
                } else {
                    System.out.println("Datos vacios");
                }
            } else {
                System.out.println("Placa incompleta");
            }
        }
        return false;
    }
}
```

**Reglas de validación implementadas:**

| Regla | Condición |
|---|---|
| Objeto no nulo | `v != null` |
| Placa | longitud de exactamente **6** caracteres |
| Marca | **no vacía** (`!marca.isBlank()`) |
| Modelo (año) | longitud de **4** caracteres |

> ⚠️ **Observación:** los mensajes impresos en cada `else` están desordenados respecto a la condición que validan (la marca vacía imprime "Placa incompleta" y la placa incompleta imprime "Datos vacios"). Conviene corregirlos para que el log sea claro.
>
> Nota: `validarVehiculo(long id, String placa, String modelo)` es una **sobrecarga** que aún está pendiente de implementar.

### 4. Capa de controlador: `ControllerVehiculo`

Nueva clase en `Parqueadero.controler`, por ahora **vacía** (se completará con los endpoints REST en la próxima clase):

```java
package Parqueadero.controler;

public class ControllerVehiculo {

}
```

### 5. Configuración de la base de datos

En `application.properties` cambiamos el nombre de la base de datos a la definitiva del proyecto:

```properties
spring.datasource.url=jdbc:mysql://localhost:3304/springsena
app.datasource.url=jdbc:mysql://localhost:3304/springsena
```

- **Antes:** `test` / `test2`.
- **Ahora:** `springsena` (una sola base de datos para el proyecto).

---

## 📌 Conceptos clave de hoy

- **Programación por capas:** separar la aplicación en capas con responsabilidades distintas:
  1. **Entity (entidad):** representa los datos / la tabla (`Vehiculo`, `Producto`).
  2. **Business Logic (lógica de negocio):** reglas de validación y procesamiento (`BLVehiculo`).
  3. **Controller (controlador):** recibe las peticiones HTTP y delega en la lógica (`ControllerVehiculo`).
- **Encapsulamiento:** atributos `private` con acceso controlado por getters/setters.
- **Constructores:** uno vacío (lo requieren los frameworks) y uno con todos los parámetros para crear objetos completos.
- **Sobrecarga de métodos:** dos métodos con el mismo nombre pero distinta firma (ej. `validarVehiculo`).

---

## ⏭️ Pendientes / próxima clase
- Implementar `validarVehiculo(long id, String placa, String modelo)`.
- Completar `ControllerVehiculo` con los endpoints REST.
- Conectar a MySQL vía JDBC (CRUD).


### "¿Que es encapsulamiento y como se aplica con POO en Java, SpringBoot, Python, Django, PHP y Laravel?"

El **encapsulamiento** es uno de los cuatro pilares de la Programación Orientada a Objetos (POO). Consiste en **ocultar los detalles internos** (estado y complejidad) de un objeto y **proveer una interfaz pública** (métodos) para interactuar con él.

Su objetivo principal es proteger la integridad de los datos, evitando que sean modificados directamente desde fuera de la clase, y permitir que la lógica de negocio (validaciones, cálculos) esté centralizada dentro del propio objeto.

**Regla de oro del encapsulamiento:**
**Atributos → `private` / `protected`.**  
**Acceso → `getters` (lectura) y `setters` (escritura con reglas).**

Veamos cómo se aplica esta teoría en cada ecosistema que mencionas:

---

### 1. Java (POO Pura) y Spring Boot

**Java** es el lenguaje más estricto con el encapsulamiento. Usa modificadores de acceso explícitos (`private`, `protected`, `public`).

- **En Java estándar:** Se declaran atributos `private` y se crean métodos `public get()` y `set()`.
- **En Spring Boot:** Se respeta este dogma, pero se potencia con **Inyección de Dependencias** (encapsulando las dependencias internas) y **DTOs** (Data Transfer Objects).

**Ejemplo en Java / Spring Boot:**

```java
@Service
public class UsuarioService {
    // Encapsulamiento de la dependencia (private + final)
    private final UsuarioRepository repository;

    // Inyección por constructor (la forma más segura y recomendada en Spring)
    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    // Lógica encapsulada: el exterior solo llama a este método,
    // sin saber cómo se guarda o valida.
    public Usuario crearUsuario(String email) {
        // Regla de negocio encapsulada aquí
        if (email == null || !email.contains("@")) {
            throw new RuntimeException("Email inválido");
        }
        return repository.save(new Usuario(email));
    }
}

// Entidad / DTO
public class Usuario {
    private Long id;
    private String email; // Atributo privado

    public String getEmail() { return email; } // Getter
    public void setEmail(String email) { 
        // Setter con validación encapsulada
        if (email != null && email.contains("@")) {
            this.email = email; 
        }
    }
}
```
> *Spring Boot también usa `@ConfigurationProperties` con getters/setters para mapear variables de entorno, manteniendo el encapsulamiento de la configuración.*

---

### 2. Python (POO) y Django

**Python** no tiene modificadores de acceso como `private` o `public` por diseño (filosofía *"somos todos adultos responsables"*). En su lugar, usa **convenciones de nombres**:

- `_atributo` (guion bajo): Indica "protegido" (no se debe tocar fuera).
- `__atributo` (doble guion bajo): *Name mangling*, lo "esconde" cambiando su nombre internamente para evitar colisiones, simulando `private`.

Para aplicar encapsulamiento "real" se usan **propiedades** (`@property`).

**En Django:** Se aplica encapsulando la lógica de negocio dentro de los **métodos del modelo** o **managers**, y usando `@property` para campos calculados.

**Ejemplo en Python / Django:**

```python
class Usuario(models.Model):
    # En Django, los campos son atributos de clase, 
    # pero la instancia los maneja internamente.
    email = models.EmailField()
    _puntos = models.IntegerField(default=0)  # Convención interna

    # PROPERTY: Getter con lógica
    @property
    def puntos(self):
        # Puedes añadir lógica aquí sin romper la interfaz externa
        return self._puntos

    # Setter con validación encapsulada
    @puntos.setter
    def puntos(self, valor):
        if valor < 0:
            raise ValueError("Los puntos no pueden ser negativos")
        self._puntos = valor

    # Método público que encapsula lógica compleja
    def obtener_estado(self):
        # El exterior llama a esto, no sabe cómo se calcula
        return "VIP" if self._puntos > 1000 else "Regular"
```
> *Nota: Django usa `Class-Based Views` (CBV) donde los métodos `get()` y `post()` encapsulan la lógica de cada verbo HTTP.*

---

### 3. PHP (POO) y Laravel

**PHP** usa modificadores de acceso idénticos a Java (`public`, `private`, `protected`). 

**En Laravel (Eloquent):** Hay un caso particular. Eloquent usa el **mágico `__get()` y `__set()`** para acceder a los atributos de la base de datos directamente (ej: `$user->name`). Esto *rompe* teóricamente el encapsulamiento clásico de atributos, pero Laravel lo solapa permitiéndote definir **Mutadores y Accesores** para encapsular la *lógica de transformación* de esos datos.

**Ejemplo en PHP / Laravel:**

```php
class Usuario extends Model
{
    // Atributos protegidos (mass assignment)
    protected $fillable = ['email', 'password'];

    // ENCAPSULAMIENTO de dependencias (Inyección en Controlador)
    public function __construct(
        private UserService $service // PHP 8+ property promotion
    ) {}

    // ACCESOR (getter): Encapsula formato de salida
    public function getNombreCompletoAttribute(): string
    {
        return "{$this->nombre} {$this->apellido}";
    }

    // MUTADOR (setter): Encapsula lógica de guardado
    public function setEmailAttribute($value)
    {
        // El exterior asigna $user->email = "ALGO";
        // pero internamente lo limpiamos y guardamos en minúscula
        $this->attributes['email'] = strtolower(trim($value));
    }

    // Encapsulamiento de reglas de negocio en el modelo
    public function esAdministrador(): bool
    {
        return $this->rol_id === 1; // El exterior no sabe cómo se guarda el rol
    }
}
```
> *En Laravel, los **Service Providers** y **Repositories** son grandes ejemplos de encapsulamiento, ya que esconden la complejidad de las consultas a la base de datos detrás de interfaces y métodos legibles.*

---

### Tabla Comparativa Rápida

| Tecnología | Modificador de atributos | Método Getter/Setter | Particularidad del entorno |
| :--- | :--- | :--- | :--- |
| **Java / Spring** | `private` (estricto) | `getNombre()` / `setNombre()` | Inyección por constructor. Uso de `@Data` (Lombok) genera getters automáticos manteniendo `private`. |
| **Python / Django** | Convención `_` o `__` | `@property` y `@x.setter` | Django usa `Meta` y `Managers` para encapsular consultas SQL complejas. |
| **PHP / Laravel** | `protected` (Eloquent) o `private` | `getXAttribute()` / `setXAttribute()` | Eloquent usa magia `__call` y `__get`, pero los Accessors/Mutators encapsulan la lógica de transformación. |

---

### Conclusión: ¿Cómo aplicar bien el encapsulamiento en estos frameworks?

Independientemente del lenguaje, aplica estas 3 reglas:

1. **Nunca expongas el estado interno directo** (evita `objeto.atributo = valor` si el atributo es un detalle de infraestructura).
2. **Usa el patrón "Tell, don't ask"**: En lugar de preguntarle al objeto por sus datos para tomar una decisión fuera, **pídele al objeto que haga algo** (ej: `usuario.cambiarEmail(nuevo)` en vez de `usuario.setEmail(nuevo)` + validación externa).
3. **Aprovecha el framework para encapsular dependencias**: En Spring, Laravel y Django, usa la inyección de dependencias en el constructor para que la clase no tenga que instanciar sus propias herramientas (repositorios, servicios), manteniendo el acoplamiento bajo y la prueba unitaria fácil.

En resumen: **Encapsular no es solo poner `private`, es esconder el "cómo" para exponer el "qué"**, y todos estos frameworks te dan herramientas para hacerlo, aunque con distintos grados de rigor sintáctico.


### "¿Que es RestController y Controller y diferencias?"

Antes de empezar, una aclaración clave: **`@RestController` y `@Controller` son anotaciones exclusivas del ecosistema Spring (Java / Spring Boot)**. 

En **Laravel** y **Django** también existen los "Controladores", pero no tienen esta distinción de anotaciones; allí un controlador puede devolver tanto HTML (vistas) como JSON (APIs) según el método que uses. En Spring, en cambio, esta diferenciación es explícita y marca un comportamiento radicalmente distinto.

Vamos a definirlos y compararlos:

---

### 1. ¿Qué es `@Controller`?

Es la anotación base del **Spring MVC** (Modelo-Vista-Controlador). Su propósito tradicional es **manejar peticiones web y devolver una Vista (HTML)**.

- Cuando un método en un `@Controller` retorna un `String`, Spring lo interpreta como el **nombre de una vista** (ej: `"home"` → `home.html` o `home.jsp`).
- Para enviar datos a esa vista, se usa el objeto `Model` o `ModelAndView`.
- **Por defecto**, los métodos **NO** están anotados con `@ResponseBody`, lo que significa que el valor de retorno NO se escribe directamente en el cuerpo de la respuesta HTTP.

### 2. ¿Qué es `@RestController`?

Es una anotación **especializada** introducida en Spring 4.0. Es una **composición** que combina `@Controller` + `@ResponseBody`.

- Su propósito exclusivo es crear **APIs RESTful** que devuelven datos estructurados (generalmente **JSON** o **XML**).
- **Todos los métodos** de esta clase tienen implícitamente `@ResponseBody`. Por lo tanto, el objeto que retornes (POJO, Lista, Mapa) será automáticamente serializado a JSON mediante el `HttpMessageConverter` de Spring.
- No existe la resolución de vistas; si retornas un `String`, este se enviará literalmente como texto plano en el cuerpo del HTTP, no como el nombre de un archivo `.html`.

---

### Diferencias Clave (Comparativa Directa)

| Característica | `@Controller` | `@RestController` |
| :--- | :--- | :--- |
| **Propósito principal** | Servir páginas web (HTML) | Construir APIs REST (JSON / XML) |
| **Anotación interna** | Solo `@Component` | `@Controller` + `@ResponseBody` |
| **Valor de retorno por defecto** | **Nombre de vista** (ej: "index"). Spring busca un archivo `.jsp` o `.html` con ese nombre. | **El objeto en sí** (se convierte automáticamente a JSON y se envía en el cuerpo). |
| **Uso de `@ResponseBody`** | **Obligatorio** en cada método si quieres devolver JSON en lugar de vista. | **No es necesario**, ya viene incluido a nivel de clase. |
| **Uso de `Model` / `ModelAndView`** | Sí, se usa frecuentemente para pasar datos a la plantilla. | No se usa (no hay vista a la que renderizar). |
| **Escenario típico** | Aplicación web con frontend monólito (Thymeleaf, JSP, Freemarker). | Microservicios, SPA (React/Angular/Vue) que consumen JSON, o apps móviles. |

---

### Ejemplo práctico en Spring Boot para que lo veas claro

**Caso 1: `@Controller` devolviendo una vista (HTML)**
```java
@Controller
public class WebController {
    
    @GetMapping("/saludo")
    public String saludar(Model model) {
        model.addAttribute("mensaje", "Hola desde el servidor");
        return "vistaSaludo"; // Busca el archivo /templates/vistaSaludo.html
    }
}
```

**Caso 2: `@Controller` forzado a devolver JSON (usando `@ResponseBody` manual)**
```java
@Controller
public class ApiMixtaController {
    
    @GetMapping("/usuario")
    @ResponseBody // <--- Sin esto, Spring buscaría una vista llamada "usuario"
    public Usuario obtenerUsuario() {
        return new Usuario("Carlos", 30); // Devuelve JSON
    }
}
```

**Caso 3: `@RestController` (la forma limpia para APIs)**
```java
@RestController // Equivale a @Controller + @ResponseBody global
@RequestMapping("/api")
public class UsuarioRestController {
    
    @GetMapping("/usuario")
    public Usuario obtenerUsuario() {
        // No necesito @ResponseBody aquí. Spring convierte esto a JSON automáticamente.
        return new Usuario("Carlos", 30); 
    }

    @GetMapping("/texto")
    public String obtenerTexto() {
        // Esto NO busca una vista. Devuelve literalmente: "Hola mundo" en texto plano.
        return "Hola mundo";
    }
}
```

---

### ¿Qué pasa con Laravel y Django? (Para cerrar el círculo)

Como te mencioné al inicio, en **Laravel (PHP)** y **Django (Python)** no existe esta separación sintáctica:

- En **Laravel**, un controlador retorna una vista con `view('home')` o retorna JSON con `response()->json($data)`. El desarrollador decide dentro del método qué devolver.
- En **Django**, usas `render()` para HTML o `JsonResponse()` para JSON. 

Es decir, **en esos frameworks eres tú quien decide el formato en cada método**, mientras que en Spring, `@RestController` es una declaración de intenciones a nivel de clase que te ahorra escribir `@ResponseBody` en cada método y deshabilita la resolución de vistas por completo.

---

### Recomendación de uso en Spring Boot

- Usa **`@Controller`** si tu backend va a generar directamente el HTML que ve el navegador (aplicación tradicional o con Thymeleaf).
- Usa **`@RestController`** si tu backend solo sirve datos y tu frontend está separado (React, Angular, Vue, Postman, o cualquier cliente externo).

**Tip profesional:** Si algún día necesitas que un `@RestController` devuelva una vista excepcionalmente (por ejemplo, para un error 404 personalizado en HTML), puedes inyectar `HttpServletResponse` o usar `ModelAndView` dentro de él, pero eso rompe la convención y no es recomendable. Para proyectos híbridos, es mejor separar los controladores en paquetes distintos (`web` y `api`).


### "¿Definicion y diferencias RequestPart, RequestBody, RequestAttribute, RequestHeader, RequestParam?"

Estas cinco anotaciones son **herramientas de Spring Boot** (y Spring MVC en general) para **extraer datos de la petición HTTP** y vincularlos a los parámetros de tus métodos en los controladores. 

La gran diferencia entre ellas radica en **¿de dónde viene ese dato?** (origen) y **¿qué formato tiene?**.

Vamos a definirlas una a una y luego compararlas con ejemplos prácticos.

---

### 1. Definiciones detalladas

- **`@RequestParam`** (Parámetro de consulta / Formulario)
  - **Origen:** **Query String** (ej: `?nombre=Juan&edad=30`) o **datos de formulario** (`application/x-www-form-urlencoded`).
  - **Uso:** Obtener valores clave-valor simples (String, int, boolean, etc.).
  - **Obligatoriedad:** Por defecto es obligatorio (`required=true`), pero puedes hacerlo opcional.
  - **Ejemplo en URL:** `GET /usuarios?page=2&size=10`

- **`@PathVariable`** (Nota: no la pediste, pero la mencionaré de paso, aunque no viene en tu lista)
- **`@RequestBody`** (Cuerpo de la solicitud - JSON/XML)
  - **Origen:** **Cuerpo (Body) de la petición HTTP**.
  - **Uso:** Cuando el cliente envía un objeto complejo en formato **JSON** (o XML). Spring usa un `HttpMessageConverter` (ej: Jackson) para deserializar automáticamente ese JSON a un objeto Java (POJO).
  - **Restricción:** **SOLO se puede usar UNA VEZ por método**, porque el flujo de entrada (`InputStream`) del cuerpo se lee una sola vez.
  - **Ejemplo:** `POST /api/usuarios` con body `{"nombre":"Ana", "email":"ana@mail.com"}`.

- **`@RequestPart`** (Parte de multipart - archivo + JSON)
  - **Origen:** Una parte específica dentro de una petición de tipo **`multipart/form-data`**.
  - **Uso:** Ideal para peticiones que envían **archivos (MultipartFile)** junto con datos JSON en la misma solicitud. Puedes extraer un archivo o una parte específica que contenga JSON y convertirla a un objeto.
  - **Diferencias con `@RequestParam`:** `@RequestParam` también sirve para multipart, pero solo para campos de texto simples. `@RequestPart` sirve para obtener la parte binaria (el archivo) O para convertir una parte JSON a un DTO usando `@RequestPart("datos") MiDto dto`.

- **`@RequestHeader`** (Cabeceras HTTP)
  - **Origen:** Las **cabeceras (Headers)** de la petición HTTP (ej: `Content-Type`, `Authorization`, `User-Agent`).
  - **Uso:** Obtener metadatos del cliente o de la autenticación.
  - **Ejemplo:** `@RequestHeader("Authorization") String token`.

- **`@RequestAttribute`** (Atributo interno del servidor)
  - **Origen:** El **ámbito (scope) de la petición** a nivel de servidor (no viene del cliente directamente). Se establece internamente mediante **Filters**, **Interceptors**, o **forwarding** (redirección interna del servidor) usando `request.setAttribute("clave", valor)`.
  - **Uso:** Compartir datos entre diferentes capas del backend durante el mismo ciclo de vida de una petición, sin pasarlos por la URL o el Body.
  - **Ejemplo:** Un Interceptor que valida un token y guarda el `userId` en el request, y tu Controlador lo recupera con `@RequestAttribute("userId") Long id`.

---

### 2. Tabla Comparativa Rápida

| Anotación | Ubicación en la HTTP | Formato típico | ¿Viene del Cliente? | ¿Se usa para archivos? |
| :--- | :--- | :--- | :--- | :--- |
| **@RequestParam** | URL (Query String) o Form Data | Clave-Valor (String) | ✅ Sí | ❌ No (solo texto) |
| **@RequestBody** | Cuerpo (Body) | JSON / XML (Objeto complejo) | ✅ Sí | ❌ No |
| **@RequestPart** | Cuerpo (multipart) | Binario (archivo) o JSON | ✅ Sí | ✅ **Sí** (MultipartFile) |
| **@RequestHeader** | Cabeceras (Headers) | Clave-Valor (String) | ✅ Sí | ❌ No |
| **@RequestAttribute** | Interno (Request Scope) | Cualquier objeto Java (atributo) | ❌ **No** (lo pone el servidor) | ❌ No |

---

### 3. Ejemplo práctico en un solo Controlador (Spring Boot)

Imagina esta petición `POST /api/productos?categoria=electronica`:
- **Header:** `Authorization: Bearer ABC123`
- **Body (multipart):** Parte llamada `imagen` (archivo .png) + Parte llamada `datos` (JSON con `{"nombre":"TV", "precio":999}`).
- *(Además, supongamos que un Filter anterior ejecutó `request.setAttribute("ipCliente", "192.168.1.1")`)*.

```java
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> crearProducto(
            
            // 1. De la URL: ?categoria=electronica
            @RequestParam("categoria") String categoria,
            
            // 2. De la cabecera HTTP
            @RequestHeader("Authorization") String token,
            
            // 3. De una parte específica del multipart (el JSON)
            @RequestPart("datos") ProductoDto productoDto,
            
            // 4. De otra parte del multipart (el archivo)
            @RequestPart("imagen") MultipartFile imagen,
            
            // 5. Del ámbito interno del servidor (lo puso un Interceptor)
            @RequestAttribute("ipCliente") String ipCliente
    ) {
        
        System.out.println("Categoría desde URL: " + categoria);
        System.out.println("Token desde Header: " + token);
        System.out.println("Nombre del producto desde JSON: " + productoDto.getNombre());
        System.out.println("Nombre del archivo: " + imagen.getOriginalFilename());
        System.out.println("IP capturada internamente: " + ipCliente);
        
        return ResponseEntity.ok().build();
    }
}
```

---

### 4. Preguntas Frecuentes y Claves para no confundirlas

- **¿`@RequestParam` vs `@RequestPart`?**
  - Usa **`@RequestParam`** para campos de texto en formularios (ej: `<input type="text" name="nombre">`).
  - Usa **`@RequestPart`** para **subir archivos** (`MultipartFile`) O cuando necesitas enviar un **JSON complejo** dentro de un `multipart/form-data` (algo que `@RequestParam` no puede hacer porque solo mapea String simples).

- **¿`@RequestBody` vs `@RequestPart`?**
  - **`@RequestBody`** solo funciona si el `Content-Type` es `application/json` (o XML). No funciona con `multipart/form-data`.
  - **`@RequestPart`** solo funciona si el `Content-Type` es `multipart/form-data`. Es la única forma de recibir un JSON *dentro* de un multipart.

- **¿`@RequestAttribute` vs `@RequestParam`?**
  - Si ves un `?` en la URL, es **Param**.
  - Si ves que un `Filter` o `Interceptor` hizo `request.setAttribute()`, es **Attribute**.
  - **Nunca uses `@RequestAttribute` para datos que manda el frontend**, porque no los encontrará y dará error.

- **¿Puedo usar `@RequestBody` y `@RequestParam` juntos?**
  - Sí, pero el `@RequestParam` debe venir en la URL (Query String). No puedes tener dos `@RequestBody` ni poner un `@RequestParam` en el Body si ya usaste `@RequestBody` (porque el Body ya se consumió).



### "¿Que son, que hacen y como se comunican: businesslogic, controler, entity y repository, y como se esta aplicando MVC y POO en esta arquitectura. Y como seria en Django y Laravel. Y si sí se esta aplicando Arquitectura de microservicios y Programación por capas?"

Analizando la estructura que me compartes (`Parqueadero` → `businesslogic`, `controller`, `entity`, `repository`), podemos afirmar con total claridad que estás ante una **Arquitectura Monolítica Multicapa (Layered Architecture)** muy bien definida, y **no** ante una de microservicios.

Aquí te explico en detalle qué es cada carpeta, cómo se comunican, y cómo se aplican MVC, POO, y su equivalente en Django y Laravel.

---

### 1. ¿Qué son, qué hacen y cómo se comunican?

El flujo de datos en esta arquitectura es **unidireccional y descendente**. La petición entra por el controlador y baja hasta la base de datos, y la respuesta sube de vuelta.

- **`entity/Vehiculo` (La Capa de Dominio / Modelo)**
  - **¿Qué es?** Un POJO (Plain Old Java Object) mapeado a una tabla de base de datos con JPA (`@Entity`). 
  - **¿Qué hace?** Define la estructura de los datos (atributos: `placa`, `marca`, `horaEntrada`) y sus relaciones. No contiene lógica de negocio, solo estado y getters/setters.
  - **Comunicación:** Es el "contrato de datos". Viaja desde el Repository hacia el BL, y del BL hacia el Controller (como respuesta JSON).

- **`repository/VehiculoRepository` (La Capa de Acceso a Datos - DAL)**
  - **¿Qué es?** Una interfaz que extiende `JpaRepository` o `CrudRepository` de Spring Data.
  - **¿Qué hace?** Encapsula toda la lógica de interacción con la base de datos (CRUD, consultas personalizadas). **No tiene lógica de negocio**, solo métodos como `findByPlaca()` o `save()`.
  - **Comunicación:** Es inyectada (vía `@Autowired`) en la capa superior (Business Logic). El Repository **NUNCA** llama al Business Logic; solo sube datos hacia él.

- **`businesslogic/BLVehiculo` (La Capa de Servicio / Lógica de Negocio - BLL)**
  - **¿Qué es?** Un `@Service` de Spring. Es el núcleo de la aplicación.
  - **¿Qué hace?** Aquí van las **reglas de negocio**. Ejemplo: *"Un vehículo no puede entrar si ya hay uno con la misma placa dentro"*, *"Calcular el costo del parqueadero según las horas"*. Orquesta las operaciones del `Repository` y aplica las validaciones.
  - **Comunicación:** Es inyectada en el Controlador. Recibe órdenes del Controller, llama al Repository, procesa los datos, y devuelve el resultado (o lanza excepciones de negocio) hacia el Controller.

- **`controller/ControllerVehiculo` (La Capa de Presentación / API)**
  - **¿Qué es?** Un `@RestController` de Spring (como vimos en la pregunta anterior).
  - **¿Qué hace?** Expone los **endpoints HTTP** (POST /vehiculo/entrada, GET /vehiculo/salida). Recibe JSON del cliente, convierte parámetros, **delega** la ejecución al `BLVehiculo`, y envuelve la respuesta en un HTTP 200/400/500.
  - **Comunicación:** Es el punto de entrada. Llama al `BLVehiculo` (nunca llama directamente al `Repository`). Recibe el resultado y lo devuelve al cliente.

**Flujo completo:**
`Cliente (JSON)` → **Controller** (recibe petición) → **BLVehiculo** (valida reglas) → **Repository** (guarda/consulta) → **Entity** (mapea datos) → **BD** → (vuelve hacia arriba con los datos).

---

### 2. ¿Cómo se está aplicando MVC y POO aquí?

- **MVC (Modelo-Vista-Controlador) en REST:** 
  Aunque MVC nació para interfaces de usuario (HTML), en APIs REST se adapta perfectamente:
  - **Controller** = `ControllerVehiculo` (el Controlador).
  - **Model** = `Entity` + `BLVehiculo` + `Repository` (todo lo que gestiona el estado y la lógica de los datos).
  - **View** = El **JSON** que se devuelve al cliente (el frontend React/Angular o Postman hace las veces de "Vista" renderizando esos datos).
  
- **POO (Programación Orientada a Objetos):** Se aplica en varios pilares:
  - **Encapsulamiento:** Los atributos de `Entity` son `private` y se accede mediante getters/setters. El `BLVehiculo` encapsula toda la lógica compleja, exponiendo solo métodos públicos sencillos (`registrarEntrada()`).
  - **Abstracción:** El `Controller` no sabe que estás usando MySQL o PostgreSQL; solo conoce el método del `BLVehiculo`. El `BL` no sabe si el `Repository` usa JPA o JDBC nativo.
  - **Inyección de Dependencias (DI) / Inversión de Control (IoC):** Spring inyecta el `Repository` dentro del `BL`, y el `BL` dentro del `Controller` (generalmente por constructor). Esto desacopla las capas y facilita los tests unitarios.

---

### 3. ¿Cómo sería esta misma arquitectura en Django y Laravel?

Esta estructura es tan universal que existe en todos los frameworks, solo que con nombres de carpetas distintos:

| **Capa** | **Spring Boot (Java)** | **Django (Python)** | **Laravel (PHP)** |
| :--- | :--- | :--- | :--- |
| **Entidad (Modelo)** | `entity/Vehiculo` | `models.py` -> `class Vehiculo(models.Model)` | `app/Models/Vehiculo.php` (Eloquent) |
| **Acceso a Datos** | `repository/VehiculoRepository` | No tiene por defecto. Se usa el **Manager** (`Vehiculo.objects.get()`). Opcionalmente, se crea un `repositories.py` manual. | `app/Repositories/VehiculoRepository.php` (patrón implementado manualmente) o se usa directamente Eloquent. |
| **Lógica de Negocio** | `businesslogic/BLVehiculo` | `services.py` -> `class VehiculoService:` | `app/Services/VehiculoService.php` |
| **Controlador (API)** | `controller/ControllerVehiculo` | `views.py` (para Django tradicional) o `viewsets.py` (con Django REST Framework). | `app/Http/Controllers/VehiculoController.php` |

**Nota importante:** En Django y Laravel, es MUY común que los programadores novatos metan toda la lógica de negocio directamente dentro del Controlador o del Modelo. **La estructura que tienes en Spring (separando BL) es la recomendada (Patrón Service-Repository)**, y en Laravel/Django se aplica exactamente igual si quieres un código limpio y mantenible: separas el Controller (HTTP), el Service (reglas de negocio) y el Repository (consultas a BD).

---

### 4. ¿Es Arquitectura de Microservicios o Programación por Capas?

- **¿Arquitectura de Microservicios?** **NO.** 
  - Un microservicio es un despliegue independiente con su propia base de datos y su propio dominio acotado. Aquí tienes un solo `src/main/java/Parqueadero`. Todo el código (controladores, lógica, repositorios) se empaqueta en un único archivo `.jar` y se despliega en un solo servidor. Es un monolito. 
  - *Ojo:* Podría ser que este monolito sea *uno de los microservicios* de un ecosistema más grande (ej: "Servicio de Parqueadero"), pero la arquitectura INTERNA de este módulo sigue siendo monolítica por capas.

- **¿Programación por Capas (Layered Architecture)?** **SÍ, rotundamente.**
  - Cumple a la perfección con el estándar de **3 capas** (o 4 si contamos la entidad):
    - **Capa de Presentación:** `controller`.
    - **Capa de Negocio (Lógica):** `businesslogic`.
    - **Capa de Persistencia (Acceso a Datos):** `repository` + `entity`.
  - Ventajas de esta capas: **Separación de responsabilidades** (SRP), fácil mantenimiento, y puedes cambiar completamente la base de datos (ej: de MySQL a MongoDB) tocando solo la capa `repository` sin afectar al `BL` ni al `Controller`.

---

### Conclusión y recomendación final

Tu estructura es **impecable desde el punto de vista de POO y patrones de diseño**. Sigue exactamente el patrón **Service-Repository**, que es el estándar de la industria en Spring Boot.

**Consejo profesional para tu proyecto:**
- Asegúrate de que `BLVehiculo` **NO** tenga anotaciones de JPA (`@Entity`) ni de HTTP (`@GetMapping`). Debe ser 100% negocio puro.
- Para cumplir aún más con POO, deberías crear una **Interfaz** para `BLVehiculo` (ej: `IVehiculoService`) y que `BLVehiculo` la implemente. Esto facilita aún más los tests y el desacoplamiento.
- Si en el futuro necesitas escalar a microservicios, esta estructura por capas te facilitará la vida, porque podrás mover fácilmente el `controller` y `businesslogic` a un nuevo proyecto sin arrastrar código de otros dominios. ¡Buen trabajo!

### "¿Definición, diferencia y relación entre PWA, JPA, JDBC, ORM, CRM y CMS?"

Este es un grupo de siglas muy heterogéneo. Para que lo entiendas perfectamente, voy a dividirlas en **tres grupos lógicos**: 

1. **Backend y Persistencia de Datos** (JDBC, ORM, JPA) → son herramientas técnicas para programar.
2. **Frontend y Experiencia de Usuario** (PWA) → es un tipo de arquitectura de aplicación web.
3. **Sistemas de Software Empresarial** (CRM, CMS) → son tipos de aplicaciones completas con un propósito de negocio.

Aquí tienes sus definiciones, diferencias y cómo se relacionan entre sí (incluso en el contexto de tu proyecto Spring Boot).

---

### Grupo 1: La capa de Persistencia (JDBC, ORM, JPA)

Estos tres conceptos viven dentro de tus carpetas `repository` y `entity`. Son los encargados de hablar con la base de datos.

- **JDBC (Java Database Connectivity)**
  - **Definición:** Es la **API estándar de bajo nivel** que provee Java para conectarse a cualquier base de datos relacional (MySQL, PostgreSQL, Oracle). Son las clases `Connection`, `Statement`, `ResultSet`.
  - **¿Qué hace?** Escribe y ejecuta **sentencias SQL en texto plano** (`"SELECT * FROM vehiculo WHERE placa = 'ABC'"`).
  - **Problema:** Es muy verboso y manual. Tienes que abrir conexiones, manejar excepciones, mapear fila por fila a objetos Java y cerrar recursos. Es propenso a errores.

- **ORM (Object-Relational Mapping)**
  - **Definición:** Es un **patrón de diseño / técnica de programación**. Consiste en **mapear** (traducir) automáticamente una tabla de una base de datos relacional a una clase de un lenguaje orientado a objetos (y las filas a objetos).
  - **¿Qué hace?** Te permite hacer `vehiculo.getPlaca()` en lugar de escribir `SELECT placa FROM...`. Le dice al sistema: *"La tabla 'vehiculos' se llama clase 'Vehiculo', y la columna 'placa' es el atributo 'placa'"*.

- **JPA (Java Persistence API)**
  - **Definición:** Es una **especificación (interfaz)** exclusiva de Java que define **cómo debe funcionar un ORM** dentro del ecosistema Java. No es un código ejecutable, es un "contrato" o "manual de instrucciones".
  - **¿Qué hace?** Proporciona las anotaciones (`@Entity`, `@Id`, `@Column`) y las interfaces (`EntityManager`, `JPARepository`) que tú usas en tu código.
  - **Relación con los anteriores:** 
    - **JDBC** es el **motor** (la carretera por donde viajan los datos).
    - **JPA** es el **volante y los pedales** (la interfaz que usa el programador).
    - **ORM** es la **técnica de conducción** (el concepto de manejar el coche).
    - **Hibernate** (el más famoso) es la **implementación concreta** de JPA. Cuando usas JPA, por debajo Spring Boot usa Hibernate, y Hibernate **usa JDBC** para ejecutar las consultas SQL finales. 

---

### Grupo 2: La Arquitectura Frontend (PWA)

- **PWA (Progressive Web App)**
  - **Definición:** Es un tipo de aplicación web que utiliza tecnologías modernas (Service Workers, Web App Manifest) para ofrecer una experiencia similar a una **aplicación nativa móvil** desde el navegador.
  - **¿Qué hace?** Permite que tu web se pueda instalar en el escritorio del celular, funcione **sin conexión a internet** (offline), reciba notificaciones push y cargue rápidamente.
  - **Relación con los demás:** Una PWA es el **cliente (Frontend)**. Tu backend en Spring Boot (con sus JDBC/JPA) le sirve los datos en JSON a través del `@RestController`, y el PWA los consume y los muestra. Puedes tener un CRM (sistema de gestión de clientes) que esté construido como una PWA.

---

### Grupo 3: Los Sistemas de Negocio (CRM y CMS)

Estos no son tecnologías, sino **tipos de software** que resuelven problemas específicos de una empresa.

- **CRM (Customer Relationship Management)**
  - **Definición:** Software diseñado para **gestionar la relación con los clientes**. Su objetivo es aumentar las ventas y la fidelización.
  - **¿Qué hace?** Almacena datos de clientes, historial de compras, interacciones con soporte, oportunidades de venta (funnels), y automatiza correos de marketing. Ejemplos: **Salesforce**, **HubSpot**, **Zoho CRM**.
  - **Diferencia con CMS:** El CRM se enfoca en **personas (clientes/ventas)**, mientras que el CMS se enfoca en **contenido (textos/imágenes)**.

- **CMS (Content Management System)**
  - **Definición:** Software diseñado para **crear, gestionar y modificar contenido digital** sin necesidad de saber programación.
  - **¿Qué hace?** Permite a editores subir artículos, fotos, vídeos, y organizarlos en páginas web. Ejemplos: **WordPress**, **Drupal**, **Joomla**.
  - **Diferencia con CRM:** El CMS se enfoca en **publicar información al público**, no en gestionar ventas internas.

---

### Tabla Comparativa Rápida

| Sigla | Área | ¿Es código o concepto? | Propósito principal |
| :--- | :--- | :--- | :--- |
| **JDBC** | Backend (Java) | API / Código | Conectar Java con BD ejecutando SQL puro. |
| **ORM** | Backend (General) | Patrón de diseño | Mapear objetos (clases) a tablas relacionales. |
| **JPA** | Backend (Java) | Especificación (Interfaz) | Estandarizar el ORM en Java (ej: Hibernate). |
| **PWA** | Frontend / Arquitectura | Tecnología web | Hacer que una web se comporte como app nativa (offline, instalable). |
| **CRM** | Negocio / Empresarial | Sistema de Software | Gestionar clientes, ventas e interacciones comerciales. |
| **CMS** | Negocio / Empresarial | Sistema de Software | Gestionar y publicar contenido digital (blogs, noticias, páginas). |

---

### ¿Cómo se relacionan TODOS en un proyecto real?

Imagina que te piden construir un **CRM** para un concesionario de autos (incluyendo un módulo de parqueadero, como tu proyecto).

1. El **CRM** (sistema final) necesita guardar la información de los clientes y los vehículos. 
2. Para construir ese CRM, decides usar **Spring Boot** (Java). En tu backend, defines tus **Entidades** y usas **JPA** (con Hibernate) para mapearlas. 
3. Cuando Hibernate guarda un objeto en la BD, por debajo utiliza el driver **JDBC** para ejecutar el `INSERT INTO`.
4. Para que los vendedores usen el CRM desde sus celulares sin estar siempre conectados a internet, decides que el frontend sea una **PWA** (se descarga los datos del vehículo y los muestra offline).
5. Además, el concesionario quiere tener un blog para publicar noticias de autos. En lugar de programarlo desde cero, usan un **CMS** (WordPress) montado en un subdominio (blog.concesionario.com).

**En resumen:** *JDBC, ORM y JPA* son los ladrillos del motor de base de datos. *PWA* es el tipo de fachada que le pones a tu edificio. *CRM y CMS* son los tipos de edificios completos que puedes construir usando esos ladrillos.



### "¿Tipos de sistemas de gestion (software) y los más importantes?"

Existen muchos tipos de software de gestión, cada uno enfocado a optimizar un área específica de un negocio. Para tener una visión clara, lo mejor es clasificarlos.

### 🗺️ Mapa de los Sistemas de Gestión

Podemos dividir los sistemas de gestión en tres grandes grupos según su enfoque.

*   **Sistemas Integrales (El "Cerebro")**: Gestionan la empresa de forma global.
*   **Sistemas Funcionales (Los "Órganos")**: Se centran en un área o departamento concreto.
*   **Sistemas Analíticos (La "Memoria")**: Se especializan en el análisis de datos.

---

### 📊 Los Sistemas de Gestión Más Importantes

Aquí tienes los sistemas más relevantes hoy en día, organizados por su función principal:

| Grupo | Sistema | Siglas | Función Principal |
| :--- | :--- | :--- | :--- |
| **Integral** | Planificación de Recursos Empresariales | **ERP** | Integrar y centralizar todos los procesos de negocio en una única plataforma. |
| **Funcional** | Gestión de Relaciones con Clientes | **CRM** | Gestionar las interacciones con clientes y prospectos para aumentar las ventas. |
| **Funcional** | Gestión de Recursos Humanos | **HRM** | Administrar todo lo relacionado con los empleados: nóminas, contratación, desempeño. |
| **Funcional** | Gestión de la Cadena de Suministro | **SCM** | Optimizar el flujo de materiales y productos, desde el proveedor hasta el cliente. |
| **Funcional** | Gestión de Almacenes | **SGA / WMS** | Controlar y automatizar los procesos internos de un almacén. |
| **Funcional** | Gestión del Ciclo de Vida del Producto | **PLM** | Gestionar toda la información de un producto desde su idea inicial hasta su retirada. |
| **Analítico** | Inteligencia de Negocios | **BI** | Analizar grandes volúmenes de datos para apoyar la toma de decisiones estratégicas. |
| **Analítico** | Gestión de Procesos de Negocio | **BPM** | Modelar, automatizar y optimizar los flujos de trabajo de la empresa. |

---

### ¿Cómo se relacionan entre sí?

Es importante entender que estos sistemas no funcionan de forma aislada, sino que se complementan:

*   **El ERP como eje central**: Un sistema ERP suele ser el núcleo. Sus módulos pueden cubrir finanzas, compras, inventario, etc., y a menudo se integra con sistemas más especializados.
*   **Integración con sistemas funcionales**: Por ejemplo, un **ERP** se puede integrar con un **CRM** para que los datos de ventas fluyan automáticamente a la contabilidad, o con un **SGA** para mantener el inventario sincronizado en tiempo real.
*   **El valor de los sistemas analíticos**: Mientras los sistemas operativos (ERP, CRM) generan datos, un sistema de **BI** los analiza para identificar tendencias y oportunidades de mejora. Un **BPM** ayuda a que todos estos procesos funcionen de manera más fluida y eficiente.

---

### 💎 En resumen

Para tu portafolio, construir un **CRM** como has hecho es un excelente punto de partida porque es uno de los sistemas funcionales más críticos y demandados. Los sistemas **ERP** son los más complejos e integrales, y los de **BI** son los que aportan el valor estratégico más alto.

**Un consejo final**: No se trata de elegir uno, sino de entender que las empresas modernas necesitan un ecosistema de estos sistemas trabajando juntos. Un ERP como eje central, integrado con un CRM para las ventas y un BI para la analítica, es la combinación ganadora para la mayoría de las organizaciones.

¿Hay algún sistema en particular de esta lista sobre el que te gustaría que profundice?


### "¿Y un CMS?"

Un **CMS** (Content Management System o Sistema de Gestión de Contenidos) es un tipo de software específico diseñado para facilitar la **creación, administración y modificación de contenido digital** (texto, imágenes, videos) en sitios web o aplicaciones, sin requerir conocimientos avanzados de programación.

A diferencia de los ERP o CRM que gestionan procesos de negocio o clientes, el CMS se centra exclusivamente en la **gestión de la información publicada**. Sus características clave incluyen:

- **Separación de contenido y diseño:** Permite cambiar la apariencia del sitio sin alterar el contenido.
    
- **Gestión multiusuario:** Define roles y permisos (administradores, editores, autores).
    
- **Extensibilidad:** Uso de plugins o módulos para añadir funcionalidades (SEO, comercio electrónico, formularios).
    
- **Autoadministración:** Interfaz intuitiva para que usuarios no técnicos publiquen contenido fácilmente.
    

Los tipos más comunes son:

1. **CMS Propietarios:** Software de pago con licencia cerrada y soporte dedicado (ej. Adobe Experience Manager, Sitecore).
    
2. **CMS de Código Abierto (Open Source):** Gratuitos, altamente personalizables y con grandes comunidades (ej. WordPress, Joomla, Drupal).
    
3. **CMS SaaS (Software as a Service):** Soluciones en la nube todo-en-uno donde se paga una suscripción mensual (ej. Wix, Squarespace, Shopify para e-commerce).
    
4. **Headless CMS:** Gestionan solo el contenido backend y lo entregan vía API a cualquier frontend (apps móviles, webs, relojes inteligentes), ofreciendo mayor flexibilidad técnica (ej. Contentful, Strapi).





### "¿Que hacen los DeleteMapping y GetMapping?"

`@GetMapping` y `@DeleteMapping` son **anotaciones especializadas de Spring Boot** (Spring MVC) que se colocan sobre los métodos de tus `@RestController`. 

Su función es **mapear (vincular) un método Java a un tipo específico de petición HTTP** y a una ruta (URL). Son versiones más cortas y legibles de escribir `@RequestMapping(method = RequestMethod.GET)` y `@RequestMapping(method = RequestMethod.DELETE)`.

Dicho de forma simple: **le dicen a Spring qué método debe ejecutarse cuando el cliente (Postman, React, Angular) haga una petición GET o DELETE a esa URL.**

Aquí te detallo qué hace cada una, cómo se usan en tu arquitectura (`ControllerVehiculo` → `BLVehiculo`) y sus diferencias clave.

---

### 1. `@GetMapping` (Obtener / Consultar datos)

- **Verbo HTTP:** `GET`.
- **Propósito:** **Recuperar información** del servidor. **NUNCA debe modificar el estado** de la base de datos (es una operación "segura" y "read-only").
- **¿Dónde van los parámetros?** Normalmente en la URL (Query String con `@RequestParam` o en la ruta con `@PathVariable`).
- **Código de respuesta típico:** `200 OK` (si encuentra los datos) o `404 Not Found` (si no existen).

**Ejemplo en tu `ControllerVehiculo`:**
```java
@RestController
@RequestMapping("/api/vehiculos")
public class ControllerVehiculo {

    private final BLVehiculo blVehiculo;

    // 1. GET para listar TODOS los vehículos
    @GetMapping // Mapea GET /api/vehiculos
    public List<Vehiculo> listarTodos() {
        return blVehiculo.obtenerTodosLosVehiculos();
    }

    // 2. GET para buscar UNO por su ID (o placa)
    @GetMapping("/{placa}") // Mapea GET /api/vehiculos/ABC123
    public Vehiculo buscarPorPlaca(@PathVariable String placa) {
        return blVehiculo.buscarPorPlaca(placa);
    }

    // 3. GET con filtros (Query Params)
    @GetMapping("/buscar") // Mapea GET /api/vehiculos/buscar?marca=Toyota
    public List<Vehiculo> filtrarPorMarca(@RequestParam String marca) {
        return blVehiculo.filtrarPorMarca(marca);
    }
}
```

---

### 2. `@DeleteMapping` (Eliminar datos)

- **Verbo HTTP:** `DELETE`.
- **Propósito:** **Eliminar un recurso existente** en el servidor (base de datos).
- **¿Dónde van los parámetros?** Por convención RESTful, el identificador del recurso a eliminar va en la **ruta (Path Variable)**. Raramente usa Query String, y **casi nunca usa `@RequestBody`** (aunque técnicamente podría, no es una buena práctica).
- **Código de respuesta típico:** `204 No Content` (eliminado exitosamente y no devuelvo nada en el cuerpo) o `200 OK` (eliminado y devuelvo un mensaje de confirmación). `404 Not Found` si no existe.

**Ejemplo en tu `ControllerVehiculo`:**
```java
@RestController
@RequestMapping("/api/vehiculos")
public class ControllerVehiculo {

    private final BLVehiculo blVehiculo;

    // DELETE para eliminar por placa
    @DeleteMapping("/{placa}") // Mapea DELETE /api/vehiculos/ABC123
    public ResponseEntity<Void> eliminarVehiculo(@PathVariable String placa) {
        blVehiculo.eliminarVehiculo(placa);
        // 204 NO CONTENT es la respuesta más estándar para DELETE exitoso
        return ResponseEntity.noContent().build();
    }

    // Alternativa: DELETE devolviendo un mensaje de confirmación
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPorId(@PathVariable Long id) {
        blVehiculo.eliminarPorId(id);
        return ResponseEntity.ok("Vehículo con ID " + id + " eliminado correctamente.");
    }
}
```

---

### Diferencias Clave entre `@GetMapping` y `@DeleteMapping`

| Característica | `@GetMapping` | `@DeleteMapping` |
| :--- | :--- | :--- |
| **Verbo HTTP** | GET | DELETE |
| **Acción en BD** | **Lee** (SELECT). No modifica nada. | **Elimina** (DELETE). Modifica el estado. |
| **Cuerpo (Body)** | No debe tener cuerpo. | Normalmente no tiene cuerpo (o se ignora). |
| **Parámetros típicos** | `@PathVariable` o `@RequestParam`. | `@PathVariable` (para indicar qué eliminar). |
| **Idempotencia** | Sí (llamarlo 1 o 100 veces da el mismo resultado y no causa efectos secundarios). | Sí (eliminar el mismo recurso dos veces: la primera lo borra, la segunda lanza 404, pero el estado final es "no existe"). |
| **Seguridad / Cache** | Puede ser cacheado por el navegador. | Nunca se cachea. |
| **Código de éxito** | `200 OK` (devuelve datos) | `204 No Content` (sin datos) o `200 OK` (con mensaje) |

---

### Conexión con tu Arquitectura (Capas)

Fíjate en los ejemplos de código: 

1. El **`@GetMapping`** y **`@DeleteMapping`** solo están en la capa de `controller`. Su trabajo es traducir la petición HTTP a una llamada a un método Java.
2. Inmediatamente, el Controller **delega** la responsabilidad al `BLVehiculo` (Lógica de Negocio). 
3. El `BLVehiculo` se encargará de validar reglas (ej: *"No se puede eliminar un vehículo si tiene una deuda pendiente"*) y luego llamará al `Repository` para que ejecute el `DELETE` en SQL (usando JPA/JDBC).

**Regla de oro en REST con Spring:**
- Usa **`@GetMapping`** para todo lo que sea *consultar*.
- Usa **`@DeleteMapping`** para todo lo que sea *borrar*.
- Nunca uses `@GetMapping` para borrar (aunque puedas hacer un `DELETE` por URL, es una mala práctica que rompe el estándar REST y los navegadores pueden cachear la petición sin querer).

**Nota extra:** Si necesitas actualizar datos, usa `@PutMapping` (reemplazar todo) o `@PatchMapping` (actualizar parcialmente). Si necesitas crear, usa `@PostMapping`. En tu proyecto de parqueadero, el `POST` y `PUT` serán necesarios para registrar la entrada y salida de los vehículos.