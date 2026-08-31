`@PathVariable` es la pieza que permite crear URLs dinámicas. Mientras que `@GetMapping` define la ruta fija, `@PathVariable` extrae valores específicos de esa URL para usarlos en tu código.

---

## **¿Qué es @PathVariable?**

Es una anotación de Spring que **extrae una variable directamente de la ruta de la URL**. Se usa cuando la URL contiene un dato que cambia, como el ID de un usuario o el nombre de un producto.

### **Ejemplo en Java**

Si quieres buscar un usuario por su ID, la URL sería algo como `localhost:8080/usuarios/5`.

Java

```
@GetMapping("/usuarios/{id}")
public Usuario buscarPorId(@PathVariable("id") Long id) {
    // Spring toma el "5" de la URL y lo mete en la variable 'id'
    return usuarioService.encontrar(id);
}
```

- **En la URL:** Se define entre llaves `{id}`.
    
- **En el método:** Se usa `@PathVariable` para mapear ese valor al parámetro de la función.
    

---

## **Relación con otros conceptos**

### **1. Con Peticiones HTTP**

En el protocolo HTTP, la información puede viajar en el cuerpo (Body) o en la **ruta (Path)**.

- `@PathVariable` se usa para identificar un recurso específico.
    
- Es la forma "limpia" de enviar datos en un `GET`, `PUT` o `DELETE`.
    

### **2. Con APIs REST**

Las APIs REST siguen estándares de legibilidad. En lugar de usar rutas confusas como `?id=5`, se usan rutas jerárquicas:

- **Mal (Query Param):** `/api/ver-usuario?id=5`
    
- **Bien (PathVariable):** `/api/usuarios/5`
    
    Esto hace que la API sea predecible y fácil de entender para otros desarrolladores.
    

### **3. Con Microservicios**

En una arquitectura de microservicios, los servicios se comunican entre sí constantemente.

- **Comunicación Interna:** Si el "Servicio de Pedidos" necesita datos del "Servicio de Clientes", hará una petición a una URL como `http://cliente-service/api/clientes/{clienteId}`.
    
- **Escalabilidad:** Permite que las rutas sean consistentes aunque los servicios estén en diferentes servidores.
    

---

## **Resumen comparativo**

|**Concepto**|**Función**|**Ejemplo de URL**|
|---|---|---|
|**Ruta Fija**|Acción general|`/usuarios`|
|**@PathVariable**|Identificador único|`/usuarios/25`|
|**@RequestParam**|Filtros o búsquedas|`/usuarios?nombre=ana`|

**En tu proyecto:** Si miras la estructura de carpetas que subiste al principio, cuando empieces a crear controladores en `com.sena.microservices`, usarás `@PathVariable` cada vez que necesites consultar, actualizar o borrar un registro específico de tu base de datos MySQL.




A diferencia de `@PathVariable` (que viene en la URL) o `@RequestBody` (que viene en el cuerpo del mensaje), el **`@RequestAttribute`** se usa para acceder a datos que **ya están dentro del servidor**.

Es información que un componente de tu aplicación (como un Filtro o un Interceptor) guardó en la petición antes de que llegara al Controlador.

---

## **¿Qué es @RequestAttribute?**

Es una anotación que permite recuperar objetos que han sido añadidos al objeto `HttpServletRequest` durante el ciclo de vida de la petición. No viene del cliente (navegador), sino que se genera internamente.

### **Ejemplo en Java**

Imagina que tienes un filtro de seguridad que valida un token y extrae el ID del usuario. Ese filtro guarda el ID para que el controlador lo use después:

Java

```
// 1. En un Filtro o Interceptor (Interno)
request.setAttribute("usuarioId", 12345);

// 2. En tu Controlador
@GetMapping("/perfil")
public String verPerfil(@RequestAttribute("usuarioId") Long id) {
    // Spring recupera el '12345' que el filtro guardó previamente
    return "Perfil del usuario: " + id;
}
```

---

## **Relación con otros conceptos**

### **1. Con Peticiones HTTP**

El objeto "Petición" (`Request`) es como una maleta. El cliente mete cosas en la maleta (como la URL o el JSON), pero el servidor también puede meter cosas extra mientras la maleta viaja hacia el controlador. `@RequestAttribute` sirve para sacar esas "cosas extra" que el servidor metió.

### **2. Con APIs y Microservicios**

Se usa muchísimo para la **Seguridad y Trazabilidad**:

- **Autenticación:** Un microservicio de seguridad valida un token y pasa los datos del usuario ya procesados al controlador mediante un atributo.
    
- **Logs:** Guardar un "ID de rastreo" (Trace ID) para saber qué camino siguió una petición a través de varios microservicios.
    

---

## **Comparativa Total: ¿De dónde vienen los datos?**

|**Anotación**|**Origen del dato**|**Ejemplo de uso**|
|---|---|---|
|**@PathVariable**|**En la URL**|`/usuarios/{id}`|
|**@RequestParam**|**Después del `?`**|`/usuarios?orden=desc`|
|**@RequestBody**|**Cuerpo (JSON)**|Un objeto completo de Usuario.|
|**@RequestAttribute**|**Dentro del servidor**|Datos de sesión, usuario autenticado o ID de rastreo.|

---

### **Resumen para tu proyecto**

Si estás trabajando en el proyecto de la imagen (`com.sena.microservices`), es probable que:

1. Uses `@PathVariable` para buscar registros por ID.
    
2. Uses `@RequestBody` para crear nuevos registros.
    
3. Uses `@RequestAttribute` más adelante, cuando implementes **seguridad** (Spring Security) para saber qué usuario está logueado sin tener que pedirle su ID en cada URL.
    



Aquí tienes el resumen de estos dos conceptos, que aunque suenan parecido, cumplen funciones totalmente distintas en Spring Boot:

---

## **1. @RequestMapping**

Es la anotación "madre" para manejar rutas. Define la dirección URL y el método HTTP.

- **¿Para qué sirve?** Sirve para mapear peticiones web a métodos de controlador. Se puede usar a nivel de **clase** (para dar una ruta base a todo el controlador) o a nivel de **método**.
    
- **Relación con @GetMapping:** `@GetMapping` es simplemente un atajo de `@RequestMapping(method = RequestMethod.GET)`.
    

**Ejemplo en Java:**

Java

```
@RestController
@RequestMapping("/api/productos") // Ruta base para todos los métodos aquí
public class ProductoController {

    @RequestMapping(value = "/lista", method = RequestMethod.GET)
    public List<Producto> listar() {
        return service.findAll();
    }
}
```

- **URL resultante:** `localhost:8080/api/productos/lista`
    

---

## **2. @RequestScope**

Este concepto no tiene que ver con rutas, sino con el **ciclo de vida** (Scope) de un objeto (Bean) de Spring.

- **¿Para qué sirve?** Indica que Spring debe crear una **instancia nueva** de un objeto por cada petición HTTP y destruirla cuando la petición termine.
    
- **En Microservicios/APIs:** Es vital para la seguridad. Si guardas datos del usuario actual en un objeto con `@RequestScope`, te aseguras de que esos datos no se filtren a otros usuarios que estén navegando al mismo tiempo.
    

**Ejemplo en Java:**

Java

```
@Component
@RequestScope // El objeto nace y muere con la petición del cliente
public class CarritoCompra {
    // Datos específicos de esta sesión/petición
}
```

---

## **Resumen de diferencias y relación**

|**Concepto**|**Tipo**|**Función Principal**|
|---|---|---|
|**@RequestMapping**|**Ruta (Routing)**|Define **A DÓNDE** llega la petición (la dirección).|
|**@RequestScope**|**Ciclo de vida (Bean)**|Define **CUÁNTO DURA** un objeto en memoria (solo durante esa petición).|

### **Cómo se conectan en tu proyecto:**

1. El cliente hace una petición a una URL definida por **`@RequestMapping`**.
    
2. Spring recibe la petición y, si el controlador necesita un objeto marcado con **`@RequestScope`**, crea uno nuevo exclusivamente para ese cliente.
    
3. El flujo de datos (que vimos antes) procesa la información.
    
4. Se envía la respuesta y el objeto de **`@RequestScope`** se elimina de la memoria.
    

**Analogía:**

- **@RequestMapping:** Es la dirección de una oficina (ej. Calle 10 #5).
    
- **@RequestScope:** Es un pase de visitante de un solo uso. Te lo dan al entrar a la oficina y lo devuelves (se destruye) al salir.




Para recuperar (o recibir) un JSON en una aplicación de Spring Boot, la herramienta clave es la anotación **`@RequestBody`**.

Aquí te explico cómo funciona, cómo se usa y qué pasa "detrás de escena".

---

## **1. La anotación @RequestBody**

Esta anotación le dice a Spring: _"Mira el cuerpo (Body) de la petición HTTP, agarra el JSON que viene ahí y conviértelo automáticamente en un objeto Java"_.

### **Ejemplo en Java**

Imagina que el cliente envía este JSON:

JSON

```
{
  "nombre": "Laptop",
  "precio": 1200.50
}
```

Para recibirlo, creas un **DTO** (Data Transfer Object) o una clase que coincida con esos campos y lo usas en tu controlador:

Java

```
@PostMapping("/productos")
public String crearProducto(@RequestBody Producto nuevoProducto) {
    // Spring ya convirtió el JSON en el objeto 'nuevoProducto'
    System.out.println("Nombre: " + nuevoProducto.getNombre());
    return "Producto guardado con éxito";
}
```

---

## **2. Requisitos para que funcione**

Para que Spring pueda "traducir" el JSON a Java correctamente, se deben cumplir tres condiciones:

1. **Clase Coincidente:** Tu clase Java debe tener atributos con los mismos nombres que las llaves del JSON.
    
2. **Getters y Setters:** La clase debe tener métodos `get` y `set` (o usar la librería **Lombok** con `@Data`).
    
3. **Encabezado HTTP:** El cliente (Postman, Frontend) debe enviar la petición con el encabezado: `Content-Type: application/json`.
    

---

## **3. ¿Cómo viaja el JSON? (El flujo)**

Cuando envías un JSON, el proceso es el siguiente:

1. **Petición:** El cliente envía un `POST` o `PUT` con el JSON en el cuerpo.
    
2. **Jackson:** Spring usa una librería interna llamada **Jackson** que actúa como traductor.
    
3. **Mapeo:** Jackson lee el JSON llave por llave y busca el atributo correspondiente en tu clase Java.
    
4. **Inyección:** Spring te entrega el objeto ya "lleno" con los datos dentro del método de tu controlador.
    

---

## **4. Diferencia con otras formas de recibir datos**

Es común confundir de dónde vienen los datos. Aquí la guía rápida:

- **`@RequestBody`**: Para datos complejos o archivos (JSON, XML). Los datos van **ocultos** en el cuerpo de la petición.
    
- **`@PathVariable`**: Para IDs o nombres que van **en la ruta** (`/productos/5`).
    
- **`@RequestParam`**: Para filtros que van **después del signo `?`** (`/productos?categoria=electronica`).
    

---

### **Resumen Visual**

- **Cliente:** "Te envío este paquete (JSON)".
    
- **@RequestBody:** Es la persona que abre el paquete y saca los objetos para que tú los uses.
    

En tu proyecto de microservicios, usarás esto constantemente para recibir la información de formularios o de otras APIs.

Reto 1: Crear una api que sume dos números enviándolos por la URL (sumar 2 números cuyos datos viajen por la URL) y probar la api con POSTMAN y hacer que el resultado se pueda recuperar.

Reto 2: hacer método suma que envié datos por el header y retornar la suma.

Reto 3: sumar 2 numeros que estan en un archivo (.txt, archivo plano, ...) y poder ver la respuesta.

Reto 4: crear formulario que contenga 2 campos/input  y que sume.

Codigo de ControllerCalculadora actualizado con los de restos de esta clase y la clase pasada:
package com.sena.microservices.controller;

  

import org.springframework.web.bind.annotation.*;

import java.util.Map;

import java.util.HashMap;

import java.io.*;

import org.springframework.ui.Model;

import org.springframework.stereotype.Controller;

  

// @RestController

@Controller

@RequestMapping("/calculadora")

public class ControllerCalculadora {

  

    // Suma usando parámetros en la URL (request param)

  

    @GetMapping("/sumar1")

    @ResponseBody

    public int sumar1(@RequestParam int a, @RequestParam int b) {

        return a + b;

    }

  

    @GetMapping("/sumar2")

    @ResponseBody

    public Map<String, Integer> sumar2(@RequestParam int a, @RequestParam int b) {

        Map<String, Integer> resultado = new HashMap<>();

        resultado.put("resultado", a + b);

        return resultado;

    }

  

    @GetMapping("/sumar-header")

    @ResponseBody

    public Map<String, Integer> sumarHeader(

        @RequestHeader("a") int a,

        @RequestHeader("b") int b) {

        Map<String, Integer> resultado = new HashMap<>();

        resultado.put("resultado", a + b);

        return resultado;

    }

  

    // Suma usando el cuerpo de la petición (request body)

    @PostMapping("/sumar")

    @ResponseBody

    public int sumarBody(@RequestBody SumaRequest request) {

        return request.getA() + request.getB();

    }

  

    // Clase interna para mapear el request body

    public static class SumaRequest {

        private int a;

        private int b;

  

        public int getA() { return a; }

        public void setA(int a) { this.a = a; }

        public int getB() { return b; }

        public void setB(int b) { this.b = b; }

    }

  

    @GetMapping("/sumar-archivo")

    @ResponseBody

    public Map<String, Integer> sumarArchivo() throws IOException {

        InputStream is = getClass().getClassLoader().getResourceAsStream("numeros.txt");

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        int a = Integer.parseInt(reader.readLine());

        int b = Integer.parseInt(reader.readLine());

        Map<String, Integer> resultado = new HashMap<>();

        resultado.put("resultado", a + b);

        return resultado;

    }

  

    @GetMapping("/formulario")

    public String mostrarFormulario() {

        return "formulario";

    }

  

    @PostMapping("/sumar-form")

    public String sumarForm(@RequestParam int a, @RequestParam int b, Model model) {

        model.addAttribute("resultado", a + b);

        return "formulario";

    }

}


User: Yo tengo el proyecto "C:\Users\SENA\Downloads\microservices", es un proyecto que estamos desarrollando en clase y en cada clase vamos añadiendo algo nuevo, pero yo me quede atrasado en unas cosas asi que algunos compañero me hicieron el favor de mandarme sus versiones del proyecto ("C:\Users\SENA\Downloads\springBootMicroservices-main", "C:\Users\SENA\Downloads\microservices-main", "C:\Users\SENA\Downloads\microservices_1"), revisalos, analizalos y comparalos con el mio y dime que le hace falta a mi proyecto.

GitHub Copilot: ¡Perfecto! Haré un análisis comparativo entre tu proyecto (microservices) y las versiones que te enviaron tus compañeros (springBootMicroservices-main, microservices-main, microservices_1). El objetivo es identificar qué le hace falta a tu proyecto respecto a los otros.

### 1. Estructura general de carpetas y archivos
- **Todos los proyectos** tienen la estructura estándar de un proyecto Spring Boot:  
  - `src/main/java/com/sena/microservices/`
  - `src/main/resources/`
  - `src/test/java/com/sena/microservices/`
  - Archivos como `pom.xml`, `mvnw`, `mvnw.cmd`, etc.

### 2. Controladores (Controllers)
- **Tu proyecto (microservices)**:
  - Tiene dos controladores:  
    - `ControllerPrueba.java`
    - `ControllerCalculadora.java`
- **microservices_1**:
  - Tiene tres controladores:  
    - `ControllerPrueba.java`
    - `ControllerCalculadora.java`
    - `ControllerAdmin.java` **(NO está en tu proyecto)**
- **microservices-main**:
  - Tiene los mismos dos controladores que tú.
- **springBootMicroservices-main**:
  - Solo tiene `ControllerPrueba.java`.

### 3. Funcionalidad de los controladores
- **ControllerPrueba.java** y **ControllerCalculadora.java** son prácticamente iguales en todos los proyectos donde aparecen.
- **ControllerAdmin.java** solo está en `microservices_1`.  
  **Esto es lo que le falta a tu proyecto respecto a tus compañeros.**

### 4. Dependencias y configuración
- Todos los proyectos tienen un archivo `pom.xml` y usan Maven.
- En la imagen de NetBeans, se ve que uno de los proyectos tiene como dependencia el conector de MySQL (`mysql-connector-j-9.7.0.jar`).  
  **Revisa si tu `pom.xml` tiene esta dependencia, si no, deberías agregarla si vas a trabajar con bases de datos MySQL.**

### 5. Otros archivos y carpetas
- .idea, .obsidian, `.postman/`, notas, etc., son carpetas de configuración o recursos adicionales y no afectan la funcionalidad principal del proyecto Spring Boot.
- Los archivos en `resources/static` y `resources/templates` están vacíos en todos los proyectos.

---

## Resumen: ¿Qué le falta a tu proyecto?

1. **ControllerAdmin.java**  
   - Este controlador solo está en la versión de tu compañero (`microservices_1`). Si tus clases lo están usando o lo necesitas para la práctica, deberías pedirle el código y agregarlo.

2. **Dependencia de MySQL**  
   - Si vas a trabajar con base de datos MySQL y no tienes la dependencia en tu `pom.xml`, agrégala:
   ```xml
   <dependency>
       <groupId>mysql</groupId>
       <artifactId>mysql-connector-java</artifactId>
       <version>9.7.0</version>
   </dependency>
   ```

3. **Revisar cambios menores**  
   - Los demás archivos y controladores son iguales o muy similares. No hay diferencias significativas en la estructura principal.

---

¿Quieres que te ayude a agregar el `ControllerAdmin.java` o revisar si tienes la dependencia de MySQL en tu `pom.xml`? Si tienes el código de ese controlador, pégalo aquí y lo integramos.

User: Creo que solo me falta la connecion con Postman

GitHub Copilot: ¡Perfecto! Si ya tienes los controladores y la dependencia de MySQL igual que tus compañeros, entonces lo único que te faltaría para trabajar con Postman es asegurarte de que tu proyecto Spring Boot expone endpoints correctamente y que puedes probarlos desde Postman.

### ¿Qué necesitas para conectar tu proyecto con Postman?

1. **Tener endpoints REST funcionando**  
   Ya tienes controladores como `ControllerPrueba` y `ControllerCalculadora` con endpoints como `/test`, `/test2`, `/calculadora/sumar`, etc.

2. **Ejecutar tu proyecto Spring Boot**  
   Asegúrate de que tu aplicación esté corriendo (por defecto en `http://localhost:8080`).

3. **Probar los endpoints en Postman**  
   - Abre Postman.
   - Crea una nueva petición.
   - Usa la URL de tus endpoints, por ejemplo:
     - `GET http://localhost:8080/test`
     - `GET http://localhost:8080/calculadora/sumar?a=1&b=2`
     - `POST http://localhost:8080/calculadora/sumar` (con body JSON: `{ "a": 1, "b": 2 }`)

4. **No necesitas código extra en tu proyecto para usar Postman**  
   Postman es solo un cliente HTTP para probar tus APIs.  
   Si tus endpoints responden correctamente en el navegador o con curl, también funcionarán en Postman.

---

### ¿Qué podrías agregar para facilitar el trabajo con Postman?

- **Documentación de tus endpoints** (opcional, pero útil para el equipo)
- **Ejemplo de colección de Postman** (puedes exportar una colección y compartirla con tus compañeros)
- **CORS**: Si vas a consumir tu API desde un frontend en otro puerto, podrías necesitar habilitar CORS.

---

### Resumen

- Tu proyecto ya está listo para ser probado con Postman.
- Solo asegúrate de que la aplicación esté corriendo y usa las URLs correctas en Postman.
- No necesitas agregar nada especial al código para "conectarlo" con Postman.

¿Quieres que te ayude a crear una colección de Postman de ejemplo o necesitas ayuda para probar algún endpoint específico?

User: Reto: Crear una api que sume dos números enviándolos por la URL (sumar 2 números cuyos datos viajen por la URL) y probar la api con POSTMAN y hacer que el resultado se pueda recuperar.

GitHub Copilot: ¡Perfecto! Ya tienes una API que suma dos números usando parámetros en la URL en tu archivo ControllerCalculadora.java:

```java
@GetMapping("/sumar")
public int sumar(@RequestParam int a, @RequestParam int b) {
    return a + b;
}
```

### ¿Cómo probarlo en Postman?

1. **Ejecuta tu proyecto Spring Boot**  
   Asegúrate de que tu aplicación esté corriendo (por defecto en http://localhost:8080).

2. **Abre Postman y crea una nueva petición:**
   - Método: **GET**
   - URL:  
     ```
     http://localhost:8080/calculadora/sumar?a=5&b=7
     ```
   - Presiona "Send".

3. **Verifica la respuesta:**  
   - Deberías recibir como respuesta el número `12`.

---

### ¿Cómo hacer que el resultado se pueda recuperar?

Ya lo puedes recuperar directamente en la respuesta de la API. Si quieres que la respuesta sea más clara (por ejemplo, en formato JSON), puedes modificar el método así:

```java
@GetMapping("/sumar")
public Map<String, Integer> sumar(@RequestParam int a, @RequestParam int b) {
    Map<String, Integer> resultado = new HashMap<>();
    resultado.put("resultado", a + b);
    return resultado;
}
```

Esto devolverá:
```json
{
  "resultado": 12
}
```

¿Quieres que te ayude a modificar el método para que devuelva el resultado en JSON? ¿O necesitas ayuda con la prueba en Postman?

User: C:\Users\SENA\Downloads\microservices\microservices\src\main\java\com\sena\microservices\controller\ControllerCalculadora.java:15:12
java: cannot find symbol
  symbol:   class Map
  location: class com.sena.microservices.controller.ControllerCalculadora
C:\Users\SENA\Downloads\microservices\microservices\src\main\java\com\sena\microservices\controller\ControllerCalculadora.java:16:9
java: cannot find symbol
  symbol:   class Map
  location: class com.sena.microservices.controller.ControllerCalculadora
C:\Users\SENA\Downloads\microservices\microservices\src\main\java\com\sena\microservices\controller\ControllerCalculadora.java:16:46
java: cannot find symbol
  symbol:   class HashMap
  location: class com.sena.microservices.controller.ControllerCalculadora


GitHub Copilot: El error se debe a que olvidaste importar las clases `Map` y `HashMap` de Java.  
Solo necesitas agregar estas líneas al inicio de tu archivo:

```java
import java.util.Map;
import java.util.HashMap;
```

¿Quieres que lo corrija automáticamente en tu archivo?

User: Reto 2: hacer método suma que envié datos por el header y retornar la suma.

GitHub Copilot: Para cumplir el reto 2, debes crear un endpoint que reciba los números a sumar a través de los headers de la petición HTTP y retorne la suma. En Spring Boot, puedes usar la anotación `@RequestHeader` para obtener valores de los headers.

Te agrego el método en tu controlador:

```java
@GetMapping("/sumar-header")
public Map<String, Integer> sumarHeader(
        @RequestHeader("a") int a,
        @RequestHeader("b") int b) {
    Map<String, Integer> resultado = new HashMap<>();
    resultado.put("resultado", a + b);
    return resultado;
}
```

¿Quieres que lo agregue automáticamente a tu archivo ControllerCalculadora.java?

User: Como hacer la peticion por postman

GitHub Copilot: ¡Claro! Aquí tienes los pasos para probar el método de suma por headers en Postman:

---

### 1. Abre Postman y crea una nueva petición

- **Método:** GET  
- **URL:**  
  ```
  http://localhost:8080/calculadora/sumar-header
  ```

---

### 2. Agrega los headers

En la pestaña "Headers" de la petición, agrega dos filas:

| Key | Value |
|-----|-------|
| a   | 5     |
| b   | 7     |

---

### 3. Envía la petición

Haz clic en "Send".

---

### 4. Verifica la respuesta

Deberías recibir una respuesta como esta:
```json
{
  "resultado": 12
}
```

---

¿Te gustaría ver una imagen de ejemplo de cómo se ve en Postman?

User: Reto 3: sumar 2 numeros que estan en un archivo (.txt, archivo plano, ...) y poder ver la respuesta.

Reto 4: crear formulario que contenga 2 campos/input  y que sume.

GitHub Copilot: ¡Perfecto! Aquí tienes cómo abordar ambos retos:

---

## Reto 3: Sumar 2 números que están en un archivo (.txt) y mostrar la respuesta

**Pasos:**
1. Crea un archivo plano (por ejemplo, `numeros.txt`) en la carpeta `resources` con dos números, uno por línea:
   ```
   5
   7
   ```
2. Agrega un método en tu controlador que lea ese archivo, sume los números y retorne el resultado.

**Ejemplo de método:**
```java
@GetMapping("/sumar-archivo")
public Map<String, Integer> sumarArchivo() throws IOException {
    InputStream is = getClass().getClassLoader().getResourceAsStream("numeros.txt");
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    int a = Integer.parseInt(reader.readLine());
    int b = Integer.parseInt(reader.readLine());
    Map<String, Integer> resultado = new HashMap<>();
    resultado.put("resultado", a + b);
    return resultado;
}
```
**Recuerda importar:**
```java
import java.io.*;
```

---

## Reto 4: Crear formulario con 2 campos/input y que sume

**Pasos:**
1. Crea un archivo HTML en `src/main/resources/templates` llamado `formulario.html`.
2. Agrega un método en el controlador para mostrar el formulario y otro para procesar la suma.

**Ejemplo de HTML (formulario.html):**
```html
<!DOCTYPE html>
<html>
<head>
    <title>Formulario de Suma</title>
</head>
<body>
    <form action="/calculadora/sumar-form" method="post">
        <input type="number" name="a" placeholder="Número 1" required>
        <input type="number" name="b" placeholder="Número 2" required>
        <button type="submit">Sumar</button>
    </form>
    <div>
        <p th:if="${resultado != null}">Resultado: <span th:text="${resultado}"></span></p>
    </div>
</body>
</html>
```

**Métodos en el controlador:**
```java
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping("/calculadora")
public class ControllerCalculadora {
    // ...otros métodos...

    @GetMapping("/formulario")
    public String mostrarFormulario() {
        return "formulario";
    }

    @PostMapping("/sumar-form")
    public String sumarForm(@RequestParam int a, @RequestParam int b, Model model) {
        model.addAttribute("resultado", a + b);
        return "formulario";
    }
}
```
**Nota:** Si tu clase ya es `@RestController`, deberás cambiarla a `@Controller` para renderizar vistas.

---

¿Quieres que realice estos cambios automáticamente en tu proyecto?

User: Como los ejecuto y pruebo con IntelliJ y Postman

GitHub Copilot: Optimized tool selectionTe explico cómo ejecutar y probar cada uno de tus endpoints en IntelliJ y Postman:

---

## 1. Ejecutar el proyecto en IntelliJ

1. Abre tu proyecto en IntelliJ.
2. Busca la clase `MicroservicesApplication` (ubicada en `com.sena.microservices`).
3. Haz clic derecho sobre el archivo y selecciona **Run 'MicroservicesApplication.main()'**.
4. Espera a que la consola muestre que el servidor está corriendo en `http://localhost:8080`.

---

## 2. Probar los endpoints en Postman

### a) Suma por parámetros en la URL

- **Endpoint:** `GET /calculadora/sumar1?a=5&b=7`
- **URL completa:**  
  ```
  http://localhost:8080/calculadora/sumar1?a=5&b=7
  ```
- **En Postman:**  
  - Método: GET  
  - Pega la URL y haz clic en "Send".

---

### b) Suma por parámetros en la URL (respuesta en JSON)

- **Endpoint:** `GET /calculadora/sumar2?a=5&b=7`
- **URL completa:**  
  ```
  http://localhost:8080/calculadora/sumar2?a=5&b=7
  ```
- **En Postman:**  
  - Método: GET  
  - Pega la URL y haz clic en "Send".

---

### c) Suma por headers

- **Endpoint:** `GET /calculadora/sumar-header`
- **URL:**  
  ```
  http://localhost:8080/calculadora/sumar-header
  ```
- **En Postman:**  
  - Método: GET  
  - Ve a la pestaña "Headers".
  - Agrega:
    - Key: `a`, Value: `5`
    - Key: `b`, Value: `7`
  - Haz clic en "Send".

---

### d) Suma por archivo plano

- **Endpoint:** `GET /calculadora/sumar-archivo`
- **URL:**  
  ```
  http://localhost:8080/calculadora/sumar-archivo
  ```
- **En Postman:**  
  - Método: GET  
  - Solo pega la URL y haz clic en "Send".
  - Asegúrate de tener el archivo `numeros.txt` en `src/main/resources` con dos números, uno por línea.

---

### e) Formulario web

- **Endpoint:** `GET /calculadora/formulario`
- **URL:**  
  ```
  http://localhost:8080/calculadora/formulario
  ```
- **En el navegador:**  
  - Abre la URL en tu navegador.
  - Llena los campos y haz clic en "Sumar".
  - Verás el resultado en la misma página.

---

¿Quieres una guía visual o tienes algún error específico al ejecutar alguno de estos pasos?

User: Me aparecio esto:
{
    "timestamp": "2026-05-11T19:10:16.278Z",
    "status": 404,
    "error": "Not Found",
    "path": "/calculadora/sumar-archivo"
}

GitHub Copilot: El error 404 ("Not Found") significa que el endpoint `/calculadora/sumar-archivo` no está siendo encontrado por Spring Boot. Esto puede deberse a:

1. **El controlador está anotado como `@Controller` en vez de `@RestController`**  
   Cuando usas `@Controller`, los métodos que retornan objetos (como `Map`) intentan buscar una vista (archivo HTML) en vez de devolver JSON.  
   Para endpoints que retornan datos (JSON), debes usar `@RestController`.

2. **El endpoint no está bien mapeado o el servidor no está corriendo.**

---

### Solución

Debes cambiar la anotación de tu clase de:
```java
@Controller
```
a:
```java
@RestController
```

Así todos los métodos devolverán JSON correctamente y el endpoint será accesible.

---

¿Quieres que realice el cambio automáticamente en tu archivo ControllerCalculadora.java?

User: Solo aparee la palabra "formulario"

GitHub Copilot: Esto ocurre porque tu clase `ControllerCalculadora` está anotada como `@RestController`, lo que hace que Spring Boot devuelva texto plano o JSON en vez de renderizar una vista HTML. Por eso, cuando accedes a `/calculadora/formulario`, solo ves la palabra "formulario" y no el formulario HTML.

### Solución

Debes hacer lo siguiente:
- Cambia la anotación de la clase de `@RestController` a `@Controller`.
- Mantén los métodos que retornan JSON con la anotación `@ResponseBody`.

Voy a corregir tu archivo para que:
- Los métodos que retornan datos (JSON) tengan `@ResponseBody`.
- El formulario se renderice correctamente.

¿Quieres que aplique este cambio automáticamente?

User: Ahora aparece:
Whitelabel Error Page
This application has no explicit mapping for /error, so you are seeing this as a fallback.

Mon May 11 14:19:23 GMT-05:00 2026
There was an unexpected error (type=Internal Server Error, status=500).

{
    "timestamp": "2026-05-11T19:19:39.440Z",
    "status": 500,
    "error": "Internal Server Error",
    "path": "/calculadora/formulario"
}

GitHub Copilot: Este error ocurre porque tu método `/calculadora/formulario` intenta renderizar una vista llamada `formulario`, pero tu clase está anotada como `@Controller` y tienes métodos que retornan objetos (como `Map`) sin la anotación `@ResponseBody`. Además, para que Thymeleaf funcione correctamente, debes tener la dependencia de Thymeleaf en tu `pom.xml`.

### Pasos para solucionar:

1. **Agrega la dependencia de Thymeleaf en tu `pom.xml`**  
   Asegúrate de tener esto en la sección `<dependencies>`:
   ```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-thymeleaf</artifactId>
   </dependency>
   ```

2. **Asegúrate de que tu archivo `formulario.html` esté en `src/main/resources/templates/`**  
   (¡Ya lo tienes bien!)

3. **Corrige el controlador:**
   - Métodos que retornan datos (JSON o Map): ponles `@ResponseBody`.
   - Métodos que retornan vistas (como el formulario): déjalos sin `@ResponseBody`.

Voy a corregir tu archivo para que funcione correctamente. 

Generating patch (87 lines) in [](file:///c%3A/Users/SENA/Downloads/microservices/microservices/src/main/java/com/sena/microservices/controller/ControllerCalculadora.java)


```

```
¡Listo!  
Ahora tu controlador está corregido para que:

- Los métodos que retornan datos (como JSON o Map) usan `@ResponseBody`.
- El método del formulario funciona correctamente con Thymeleaf.

Asegúrate de tener la dependencia de Thymeleaf en tu `pom.xml` y vuelve a ejecutar tu aplicación.  
Ahora, al entrar a `/calculadora/formulario`, deberías ver el formulario HTML y los endpoints tipo API seguirán funcionando en Postman.

¿Necesitas ayuda para agregar la dependencia de Thymeleaf o tienes otro error?

Se realizaron cambios.

User: Pero entonces por ejemplo http://localhost:8081/calculadora/sumar1?a=5&b=7 ya no serviria?

GitHub Copilot: ¡Sí funciona!  
El endpoint http://localhost:8081/calculadora/sumar1?a=5&b=7 seguirá funcionando perfectamente, porque:

- El método sumar1 está anotado con `@ResponseBody`, lo que hace que Spring devuelva el resultado directamente como respuesta (en este caso, el número 12).
- La clase está anotada como `@Controller`, pero los métodos tipo API tienen `@ResponseBody`, así que se comportan igual que en un `@RestController`.

**En resumen:**  
Puedes usar tanto endpoints tipo API (para Postman, AJAX, etc.) como vistas (formulario HTML) en el mismo controlador, siempre que uses `@ResponseBody` en los métodos que retornan datos.

¡Puedes probarlo y verás que funciona!

User: Funcionaria entonces en el navegador pero no en Postman?

GitHub Copilot: ¡Funciona en ambos!  
Tus endpoints con `@ResponseBody` funcionan igual en el navegador y en Postman.

- Si accedes a `http://localhost:8081/calculadora/sumar1?a=5&b=7` desde el navegador, verás el resultado (por ejemplo, `12`).
- Si haces la misma petición desde Postman (GET con los mismos parámetros), también recibirás el resultado.

**Resumen:**  
- Los endpoints tipo API (`@ResponseBody`) funcionan en navegador, Postman, curl, AJAX, etc.
- Los endpoints que retornan vistas (como el formulario) solo se ven bien en el navegador.

¡Puedes usar ambos sin problema!