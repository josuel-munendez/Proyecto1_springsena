Competencia: DESARROLLAR LA SOLUCIÓN DE SOFTWARE DE ACUERDO CON EL DISEÑO Y METODOLOGÍAS DE DESARROLLO Comprobante de Desempeño: 04 CODIFICAR EL SOFTWARE DE ACUERDO CON EL DISEÑO ESTABLECIDO.

“Un microservicio es un **enfoque arquitectónico y organizativo** para el desarrollo de software en el que una aplicación se estructura como un conjunto de **servicios pequeños, autónomos e independientes**.  Cada microservicio se ejecuta en su propio proceso, se comunica a través de **APIs bien definidas** (como REST o mensajería asíncrona) y se enfoca en una **capacidad empresarial específica**.”

“Es más fácil diseñar, probar, implementar y actualizar microservicios que aplicaciones monolíticas. [Red Hat considera que esto responde a la pregunta ‘¿cómo logro que mi empresa reaccione más rápido ante las demandas nuevas](https://www.redhat.com/es/topics/cloud-native-apps/why-choose-red-hat-cloud-native-1), en lugar de tener que esperar la cantidad de años que supone el desarrollo tradicional de software?’. En la actualidad, las distintas partes del equipo de desarrollo pueden trabajar simultáneamente en los productos de un [modo ágil](https://www.redhat.com/es/topics/devops/what-is-agile-methodology) para ofrecer beneficios a los clientes de inmediato.”

**“Spring Boot** es un marco de trabajo de código abierto basado en Java que simplifica y agiliza el desarrollo de aplicaciones web, APIs RESTful y microservicios.  Funciona como una extensión del **Spring Framework**, aplicando el principio de "convención sobre configuración" para eliminar la necesidad de configuraciones manuales complejas y archivos XML.

Sus características principales incluyen:

- **Autoconfiguración:** Analiza las dependencias del proyecto (como los "Starters") y configura automáticamente los componentes necesarios (bases de datos, servidores web, seguridad) según mejores prácticas.
- **Servidor embebido:** Incluye servidores web integrados como **Tomcat**, Jetty o Undertow, permitiendo ejecutar la aplicación como un archivo JAR autónomo sin necesidad de instalar un servidor externo.
- **Desarrollo rápido:** Facilita la creación de aplicaciones independientes listas para producción, permitiendo a los desarrolladores centrarse en la lógica de negocio en lugar de la infraestructura.

A diferencia del Spring Framework tradicional, que ofrece máxima flexibilidad mediante configuración manual, Spring Boot prioriza la **facilidad de uso** y la productividad, siendo actualmente el estándar para el desarrollo backend en la nube y arquitecturas de microservicios.”

Pendiente: mejorar la hoja de vida y Github

Carpetas del proyecto

.idea: configuracion del proyecto para que el IDE lea y abra el proyecto

.mvn: corre el maven del proyecto

.src: contiene el codigo fuente (source)

src/main: donde va a estar el backend

./main/resources: donde va a estar el frontend

test: la carpeta donde esta la actualizacion de pruebas

mvnw: iniciador del proyecto

pom.xml: donde se van a inicializar las librerias que necesita el proyecto

“Este proyecto es una aplicación de **Spring Boot** estructurada con **Maven**, que es el estándar de la industria para proyectos Java. Las dos imágenes muestran lo mismo, pero desde perspectivas diferentes: la primera es la **vista de archivos real** (estilo IntelliJ/VS Code) y la segunda es una **vista lógica** (estilo NetBeans).

Aquí tienes el desglose de qué hace cada parte:

---

## 1. Carpetas Principales de Código (`src`)

Es el corazón de tu aplicación. Se divide según el estándar de Maven:

- **`src/main/java` (Source Packages):** Aquí va todo tu código fuente de Java.
    - **`com.sena.microservices`:** Es el paquete base.
    - **`MicroservicesApplication.java`:** Es la clase principal. Contiene el método `main` que arranca todo el framework de Spring Boot.
- **`src/main/resources` (Other Sources):** Archivos que no son código Java pero que la aplicación necesita.
    - **`static/`:** Para archivos web estáticos (CSS, imágenes, JavaScript).
    - **`templates/`:** Para plantillas HTML (si usas motores como Thymeleaf).
    - **`application.properties`:** El archivo más importante de configuración. Aquí defines el puerto del servidor, la conexión a la base de datos (como el MySQL que se ve en la imagen), etc.
- **`src/test/java` (Test Packages):** Aquí escribes las pruebas unitarias y de integración para asegurar que tu código funciona bien sin afectar la aplicación real.

---

## 2. Archivos de Configuración de Maven

Maven es la herramienta que gestiona las librerías y la construcción del proyecto.

- **`pom.xml`:** Es el "manual de instrucciones" del proyecto. Aquí declaras qué librerías usas (dependencias), la versión de Java y el nombre del proyecto.
- **`mvnw` y `mvnw.cmd`:** Es el "Maven Wrapper". Permite que cualquier persona ejecute el proyecto incluso si no tiene Maven instalado en su computadora.
- **`.mvn/wrapper`:** Archivos internos necesarios para que el Wrapper funcione.

---

## 3. Carpetas de Herramientas e IDE

- **`.idea/`:** Carpeta oculta de IntelliJ IDEA. Guarda tus preferencias personales del editor (colores, fuentes, historial). No debe tocarse ni subirse a GitHub normalmente.
- **`.gitignore`:** Le dice a Git qué archivos **no** debe subir a la nube (como carpetas de configuración personal o archivos temporales).
- **`HELP.md`:** Un archivo de ayuda generado automáticamente con enlaces a la documentación de Spring.

---

## 4. Dependencias (Vista de la segunda imagen)

La segunda imagen muestra cómo el IDE organiza las librerías externas que tu proyecto está usando:

- **`mysql-connector-j-9.7.0.jar`:** Es el "driver" que permite que tu código Java hable con una base de datos MySQL.
- **`JDK 25`:** Indica que estás usando la versión 25 del Java Development Kit para compilar el código.

---

### Resumen del flujo

1. Configuras tus librerías en el **`pom.xml`**.
2. Escribes tu lógica en **`src/main/java`**.
3. Configuras la base de datos en **`application.properties`**.
4. Arrancas el proyecto desde **`MicroservicesApplication`**.

¿Estás intentando conectar este proyecto a una base de datos MySQL o recién estás empezando con la estructura?
![[Pasted image 20260504131418.png]]
![[Pasted image 20260504131437.png]]
![[Pasted image 20260504131503.png]]

```
C:\\\\Users\\\\SENA\\\\.jdks\\\\openjdk-24.0.1\\\\bin\\\\java.exe "-javaagent:C:\\\\Program Files\\\\JetBrains\\\\IntelliJ IDEA Community Edition 2025.1.4.1\\\\lib\\\\idea_rt.jar=65505" -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath C:\\\\Users\\\\SENA\\\\Downloads\\\\microservices\\\\microservices\\\\target\\\\classes;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\org\\\\springframework\\\\boot\\\\spring-boot-starter-webmvc\\\\4.0.6\\\\spring-boot-starter-webmvc-4.0.6.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\org\\\\springframework\\\\boot\\\\spring-boot-starter\\\\4.0.6\\\\spring-boot-starter-4.0.6.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\org\\\\springframework\\\\boot\\\\spring-boot-starter-logging\\\\4.0.6\\\\spring-boot-starter-logging-4.0.6.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\ch\\\\qos\\\\logback\\\\logback-classic\\\\1.5.32\\\\logback-classic-1.5.32.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\ch\\\\qos\\\\logback\\\\logback-core\\\\1.5.32\\\\logback-core-1.5.32.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\org\\\\apache\\\\logging\\\\log4j\\\\log4j-to-slf4j\\\\2.25.4\\\\log4j-to-slf4j-2.25.4.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\org\\\\apache\\\\logging\\\\log4j\\\\log4j-api\\\\2.25.4\\\\log4j-api-2.25.4.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\org\\\\slf4j\\\\jul-to-slf4j\\\\2.0.17\\\\jul-to-slf4j-2.0.17.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\org\\\\springframework\\\\boot\\\\spring-boot-autoconfigure\\\\4.0.6\\\\spring-boot-autoconfigure-4.0.6.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\jakarta\\\\annotation\\\\jakarta.annotation-api\\\\3.0.0\\\\jakarta.annotation-api-3.0.0.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\org\\\\yaml\\\\snakeyaml\\\\2.5\\\\snakeyaml-2.5.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\org\\\\springframework\\\\boot\\\\spring-boot-starter-jackson\\\\4.0.6\\\\spring-boot-starter-jackson-4.0.6.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\org\\\\springframework\\\\boot\\\\spring-boot-jackson\\\\4.0.6\\\\spring-boot-jackson-4.0.6.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\tools\\\\jackson\\\\core\\\\jackson-databind\\\\3.1.2\\\\jackson-databind-3.1.2.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\com\\\\fasterxml\\\\jackson\\\\core\\\\jackson-annotations\\\\2.21\\\\jackson-annotations-2.21.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\tools\\\\jackson\\\\core\\\\jackson-core\\\\3.1.2\\\\jackson-core-3.1.2.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\org\\\\springframework\\\\boot\\\\spring-boot-starter-tomcat\\\\4.0.6\\\\spring-boot-starter-tomcat-4.0.6.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\org\\\\springframework\\\\boot\\\\spring-boot-starter-tomcat-runtime\\\\4.0.6\\\\spring-boot-starter-tomcat-runtime-4.0.6.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\org\\\\apache\\\\tomcat\\\\embed\\\\tomcat-embed-core\\\\11.0.21\\\\tomcat-embed-core-11.0.21.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\org\\\\apache\\\\tomcat\\\\embed\\\\tomcat-embed-el\\\\11.0.21\\\\tomcat-embed-el-11.0.21.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\org\\\\apache\\\\tomcat\\\\embed\\\\tomcat-embed-websocket\\\\11.0.21\\\\tomcat-embed-websocket-11.0.21.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\org\\\\springframework\\\\boot\\\\spring-boot-tomcat\\\\4.0.6\\\\spring-boot-tomcat-4.0.6.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\org\\\\springframework\\\\boot\\\\spring-boot-http-converter\\\\4.0.6\\\\spring-boot-http-converter-4.0.6.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\org\\\\springframework\\\\boot\\\\spring-boot\\\\4.0.6\\\\spring-boot-4.0.6.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\org\\\\springframework\\\\spring-context\\\\7.0.7\\\\spring-context-7.0.7.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\org\\\\springframework\\\\spring-web\\\\7.0.7\\\\spring-web-7.0.7.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\org\\\\springframework\\\\spring-beans\\\\7.0.7\\\\spring-beans-7.0.7.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\io\\\\micrometer\\\\micrometer-observation\\\\1.16.5\\\\micrometer-observation-1.16.5.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\io\\\\micrometer\\\\micrometer-commons\\\\1.16.5\\\\micrometer-commons-1.16.5.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\org\\\\springframework\\\\boot\\\\spring-boot-webmvc\\\\4.0.6\\\\spring-boot-webmvc-4.0.6.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\org\\\\springframework\\\\boot\\\\spring-boot-servlet\\\\4.0.6\\\\spring-boot-servlet-4.0.6.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\org\\\\springframework\\\\spring-webmvc\\\\7.0.7\\\\spring-webmvc-7.0.7.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\org\\\\springframework\\\\spring-aop\\\\7.0.7\\\\spring-aop-7.0.7.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\org\\\\springframework\\\\spring-expression\\\\7.0.7\\\\spring-expression-7.0.7.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\com\\\\mysql\\\\mysql-connector-j\\\\9.7.0\\\\mysql-connector-j-9.7.0.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\org\\\\slf4j\\\\slf4j-api\\\\2.0.17\\\\slf4j-api-2.0.17.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\org\\\\springframework\\\\spring-core\\\\7.0.7\\\\spring-core-7.0.7.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\commons-logging\\\\commons-logging\\\\1.3.6\\\\commons-logging-1.3.6.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\org\\\\jspecify\\\\jspecify\\\\1.0.0\\\\jspecify-1.0.0.jar;C:\\\\Users\\\\SENA\\\\.m2\\\\repository\\\\org\\\\springframework\\\\boot\\\\spring-boot-web-server\\\\4.0.6\\\\spring-boot-web-server-4.0.6.jar com.sena.microservices.MicroservicesApplication
```

```
.   ____          _            __ _ _
/\\\\\\\\ / *' __ _ () __  __ _ \\\\ \\\\ \\\\ \\\\
( ( )\\\\*__ \\| '_ \\| '*\\| \\| '* \\\\/ *\\` \\| \\\\ \\\\ \\\\ \\\\
\\\\\\\\/  )\\| \\|)\\| \\| \\| \\| \\| \\|\\| (\\| \\|  ) ) ) )
'  \\|\\| .__\\|\\| \\|*\\|*\\| \\|*\\\\**, \\| / / / /
=========\\|*\\|==============\\|***/=/*/*/_/
```

```
:: Spring Boot ::                (v4.0.6)
```

```
2026-04-27T13:03:25.922-05:00  INFO 13092 --- \\[microservices\\] \\[           main\\] c.s.m.MicroservicesApplication           : Starting MicroservicesApplication using Java 24.0.1 with PID 13092 (C:\\\\Users\\\\SENA\\\\Downloads\\\\microservices\\\\microservices\\\\target\\\\classes started by SENA in C:\\\\Users\\\\SENA\\\\Downloads\\\\microservices)
2026-04-27T13:03:25.931-05:00  INFO 13092 --- \\[microservices\\] \\[           main\\] c.s.m.MicroservicesApplication           : No active profile set, falling back to 1 default profile: "default"
2026-04-27T13:03:27.902-05:00  INFO 13092 --- \\[microservices\\] \\[           main\\] o.s.boot.tomcat.TomcatWebServer          : Tomcat initialized with port 8080 (http)
2026-04-27T13:03:27.933-05:00  INFO 13092 --- \\[microservices\\] \\[           main\\] o.apache.catalina.core.StandardService   : Starting service \\[Tomcat\\]
2026-04-27T13:03:27.934-05:00  INFO 13092 --- \\[microservices\\] \\[           main\\] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: \\[Apache Tomcat/11.0.21\\]
2026-04-27T13:03:28.048-05:00  INFO 13092 --- \\[microservices\\] \\[           main\\] b.w.c.s.WebApplicationContextInitializer : Root WebApplicationContext: initialization completed in 1968 ms
2026-04-27T13:03:28.678-05:00  INFO 13092 --- \\[microservices\\] \\[           main\\] o.s.boot.tomcat.TomcatWebServer          : Tomcat started on port 8080 (http) with context path '/'
2026-04-27T13:03:28.685-05:00  INFO 13092 --- \\[microservices\\] \\[           main\\] c.s.m.MicroservicesApplication           : Started MicroservicesApplication in 3.929 seconds (process running for 5.305)
```

![[Pasted image 20260504131629.png]]

Notaciones: ”En programación, una **notación** es un sistema de símbolos y reglas que permite expresar ideas complejas de forma compacta y estandarizada. **Resumen en Java** En lugar de usar lenguaje natural, la notación permite que el código sea legible y ejecutable. • **Anotaciones de Java:** El uso de `@` (como `@Override` o `@GetMapping`) es una notación que añade metadatos. Indica comportamiento (ej. "esto es un controlador") sin escribir párrafos de configuración. • **Complejidad (Big O):** Usar $O(1)$ o $O(n)$ para describir qué tan rápido es un algoritmo según sus datos. • **UML:** Dibujar flechas y cajas para representar cómo se hereda una clase de otra sin leer todo el código. • **Sintaxis de Código:** La forma de declarar variables (`int x = 10;`) o usar puntos (`objeto.metodo()`) es la notación propia del lenguaje. **Diferencia Clave** • **Notación:** El "dibujo" o símbolo (el qué). • **Sintaxis:** Las reglas gramaticales para que ese símbolo funcione (el cómo). **Dato importante:** En Java, muchas veces se confunde "notación" con **"anotación"** (`@`). La anotación es una herramienta específica de Java que utiliza una notación especial para dar instrucciones al compilador o al framework.”

Conclusion 1: Si usamos controler necesitamos especificar la resupuesta con @ResponseBody, muy utilizado para mandar una respuesta tipo html. Y @RestControler se usa para mandar respuestas màs planas: sin diseño, solo texto.

## **“@Controller vs @RestController**

Ambos actúan como "recepcionistas" de peticiones HTTP, pero entregan resultados diferentes:

### **1. @Controller (Páginas Web)**

Se usa para devolver **vistas (HTML)** (ej. usando Thymeleaf). Spring busca un archivo físico con ese nombre para mostrarlo en el navegador.

- **Flujo:** Petición → Lógica → Nombre de archivo → Renderiza HTML.
- **Ejemplo:**

Java

`@Controller public class UsuarioController { @GetMapping("/hola") public String saludo() { return "hola"; // Carga el archivo hola.html } }`

### **2. @RestController (Datos/APIs)**

Se usa para devolver **datos crudos** (JSON, XML o texto). Es el estándar para aplicaciones modernas que conectan con React, Angular o Apps móviles.

- **Flujo:** Petición → Lógica → Datos → Conversión automática a JSON.
- **Ejemplo:**

Java

`@RestController public class UsuarioApi { @GetMapping("/api/hola") public String saludo() { return "Hola mundo"; // Envía el texto directo al cliente }

```
@GetMapping("/usuario")
public Usuario obtener() {
    return new Usuario("Carlos"); // Spring lo transforma a JSON: {"nombre": "Carlos"}
}
```

}`

---

### **Diferencias Clave**

|**Característica**|**@Controller**|**@RestController**|
|---|---|---|
|**Resultado**|Vista (HTML / Plantilla)|Datos (JSON / XML)|
|**Uso común**|Aplicaciones Web tradicionales|APIs REST / Microservicios|
|**Composición**|Es la base|`@Controller` + `@ResponseBody`|

**La clave:** `@RestController` incluye automáticamente `@ResponseBody`, lo que le dice a Spring: _"No busques un archivo HTML, envía el objeto directamente como datos"_.

**Analogía rápida:**

- **@Controller:** Un mesero que te trae el plato ya servido (la página lista).
- **@RestController:** Un proveedor que te entrega los ingredientes (los datos) para que tú los prepares en otro lugar.“

@GetMapping:

“Es una anotación que vincula una **ruta URL** con un **método Java**. Le indica a Spring que ejecute una función específica cuando recibe una petición de lectura.

### **Cómo se usa en Java**

Conecta el navegador (o cliente) con tu código:

Java

`@GetMapping("/hola") public String saludo() { return "Hola"; }`

- **Acción:** Si entras a `http://localhost:8080/hola`.
- **Resultado:** Spring ejecuta `saludo()` y el usuario recibe el texto "Hola".

### **Retorno de Objetos (JSON)**

Si usas `@RestController`, Spring transforma automáticamente tus objetos:

Java

`@GetMapping("/usuario") public Usuario obtener() { return new Usuario("Ana"); }`

- **Resultado en el cliente:** `{"nombre": "Ana"}`.

---

### **Conceptos Clave**

- **Verbo GET:** Se usa exclusivamente para **consultar o leer** información (no para modificar datos).
    
- **Atajo Técnico:** `@GetMapping("/ruta")` es la versión simplificada de:Java
    
    `@RequestMapping(value = "/ruta", method = RequestMethod.GET)`
    

### **Analogía y Flujo**

Es como un **timbre con etiqueta**:

1. **Petición:** El cliente toca el timbre marcado como `/usuarios`.
2. **Detección:** Spring busca qué método tiene la etiqueta `@GetMapping("/usuarios")`.
3. **Ejecución:** Se abre la "puerta" (el método Java) y se entrega la información solicitada.

---

**Dato extra:** Existen anotaciones hermanas para otras acciones:

- `@PostMapping` (Crear)
- `@PutMapping` (Actualizar)
- `@DeleteMapping` (Borrar)”

## **Flujo de Datos:**

“Es el recorrido que hace la información desde que el usuario interactúa con la aplicación hasta que recibe una respuesta. Define **cómo viajan los datos** a través de las capas del sistema.

### **El Viaje de la Información**

Los datos se mueven en cascada (bajan a la base de datos y suben de vuelta al cliente):

1. **Cliente** (Navegador/Postman)
2. **Controller** (Punto de entrada/Recepción)
3. **Service** (Lógica de negocio)
4. **Repository** (Acceso a datos)
5. **Base de Datos** (Almacenamiento)

### **Cómo "viajan" en Java**

Los datos se encapsulan en **objetos**. Por ejemplo, al crear un usuario:

Java

`@PostMapping("/usuarios") public Usuario crear(@RequestBody Usuario usuario) { // El objeto 'usuario' viaja hacia las capas inferiores return usuarioService.guardar(usuario); }`

- **Entrada:** Un JSON `{ "nombre": "Ana" }` se convierte en un objeto Java.
- **Trayecto:** El objeto `usuario` pasa del **Controller** al **Service**, y de ahí al **Repository**.
- **Salida:** El flujo se invierte y el objeto vuelve al cliente como respuesta.

---

### **El Rol del Controller en el Flujo**

Es el **orquestador**. Su trabajo no es procesar cálculos complejos ni guardar datos, sino:

- Interpretar la URL y recibir los datos.
- Activar el proceso correcto llamando al **Service**.
- Entregar la respuesta final al usuario.

### **Analogía del Edificio**

- **Información:** Es un visitante.
- **Controller (Recepción):** Recibe al visitante y lo dirige al departamento correcto.
- **Service (Oficina interna):** Realiza el trabajo o trámite.
- **Repository/DB (Archivo):** Donde se busca o guarda el expediente.

---

**Resumen en una frase:** El flujo de datos es lo que permite que los componentes de tu proyecto (los que viste en las carpetas de la primera imagen) dejen de estar aislados y empiecen a colaborar para resolver una petición.”