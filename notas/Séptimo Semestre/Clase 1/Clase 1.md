# Séptimo Semestre — Clase 1

**Fecha:** 27 de julio de 2026

---

## 📋 Plan del trimestre: 3 proyectos

Vamos a desarrollar **3 proyectos** en el trimestre, además del proyecto calculadora básica y las prácticas del trimestre pasado.

### Proyecto 1 — Spring Boot + JDBC + MySQL
- Tecnología: **Spring Boot** con **JDBC** para conectarse a **MySQL**.
- Buenas prácticas de desarrollo.
- Encriptación de datos.
- Arquitectura de microservicios.
- Consumo de APIs con **fetch**.
- Validación de datos en el **front end** y en el **backend**.
- Programación por capas.
- Documentación de código.
- Pruebas de la API con **Postman**.
- Paginación de datos.

📅 **Fecha de entrega:** 24 de agosto — **sustentación**.

### Proyecto 2 — Microservicio con ORM (JPA)
- Microservicio con **ORM (JPA)**.
- Manejo de interfaces.
- Implementación de métodos personalizados.
- Arquitectura **DDD** (orientada al dominio).
- Validación de datos en el backend.
- Manejo de códigos de estado.
- Uso de herencia.
- Documentación de código.
- Pruebas con Postman.
- Configuración de **Spring Security**.
- Proyecto conectado a **MySQL**.

### Proyecto 3 — Igual al 2 pero con MongoDB
- Mismo alcance que el proyecto 2, pero conectado a **MongoDB**.

📅 **Proyectos 2 y 3:** entre el **28 de septiembre y el 5 de octubre**.

---

## 🛠️ Lo que hicimos en clase

### 1. Creación del proyecto Spring Boot (`demo`)
- Proyecto generado con **Spring Initializr**.
- **Java 21**, empaquetado Maven.
- Dependencias principales (`pom.xml`):
  - `spring-boot-starter-jdbc` → conexión JDBC a MySQL.
  - `spring-boot-starter-webmvc` → API web / controladores.
  - `mysql-connector-j` → driver de MySQL.
- Clase principal generada: `springsena.demo.DemoApplication`.

### 2. Estructura de paquetes creada

```
demo/src/main/java
├── springsena.demo
│   └── DemoApplication.java        → arranque de Spring Boot
├── RED
│   ├── RedApplication.java         → clase principal del proyecto RED
│   └── productos
│       └── catalogo
│           └── Producto.java       → entidad Producto
└── Parqueadero
    ├── ParqueaderoApplication.java → clase principal del proyecto Parqueadero
    └── entity
        └── Vehiculo.java           → entidad Vehiculo
```

- **RED** → entidad **Producto** (id, nombre, precio_base, descripcion, is_activo, is_aprovado, fecha_creacion, fecha_actualización) con sus getters y setters.
- **Parqueadero** → entidad **Vehiculo** (id, placa, marca, modelo, propetario).

### 3. Configuración `application.properties`

```properties
spring.application.name=demo
server.port=8081

spring.datasource.url=jdbc:mysql://localhost:3304/test
spring.datasource.username=springsena
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.configuration.maximum-pool-size=30
```

- Puerto del servidor: **8081**.
- Conexión JDBC a MySQL en `localhost:3304`, usuario `springsena`.

---

## 📚 Tarea
- **Estudiar automatización de pruebas.**

---

## 💬 Pregunta de la clase: getters de booleanos en Java

**¿Hay alguna diferencia o razón para usar un nombre de variable sobre el otro?**

```java
public boolean isIs_aprovado() { return is_aprovado; }
public boolean getIs_aprovado() { return is_aprovado; }
```

Sí, **hay una diferencia crucial** y una **razón contundente** para elegir una sobre la otra. Además, **ambas opciones revelan un problema de fondo** en el nombre de la variable.

### 1. Según la especificación JavaBeans (la norma oficial)
- Para variables **boolean** primitivas, el estándar **exige** el prefijo `is`. Por lo tanto, `isIs_aprovado()` es el método **correcto** según la especificación.
- Para variables de tipo envolvente (`Boolean`) o cualquier otro tipo, se usa `get`. Por lo tanto, `getIs_aprovado()` **no cumple** con el estándar para un `boolean` primitivo.

### 2. El peligro real: frameworks y serialización (JSON, JPA, Spring)
Si ambos métodos conviven en la misma clase (o si usas `getIs_aprovado` en lugar de `isIs_aprovado`), los frameworks de serialización (como **Jackson** o **Gson**) y los EL (Expression Language) de JSP/JSF se basan en la introspección de JavaBeans para encontrar la propiedad.

**El problema concreto:**
- Si la variable se llama `is_aprovado` y pones `getIs_aprovado()`, muchos frameworks **no** la reconocerán como el getter de esa variable, porque esperan `isIs_aprovado()`.
- **Peor aún:** si pones **ambos métodos** en la clase, Jackson (usado en Spring Boot) lanzará una excepción del tipo `JsonMappingException` (conflicto de definiciones de getters) porque detecta dos accesores apuntando a la misma propiedad interna.

### 3. Legibilidad y semántica
- `is...` transmite inmediatamente que es un estado booleano (pregunta algo). Es más idiomático en Java.
- `get...` para un booleano suena a que estás obteniendo un objeto o un contenedor, no una condición. Aunque funcione en tiempo de ejecución, ensucia la intención del código.

### ⚠️ El verdadero problema (y la solución recomendada)
El nombre de la variable **`is_aprovado`** es el culpable de esta confusión. Tener el prefijo `is` dentro del nombre de la variable hace que los getters generados sean antiestéticos (`isIs_aprovado`). Además, el guion bajo (`_`) en Java se suele reservar para constantes.

**Refactoriza:**

```java
// En lugar de: private boolean is_aprovado;
private boolean aprovado;   // O mejor aún, en inglés: private boolean approved;
```

Getter estándar resultante:

```java
public boolean isAprovado() {   // O isApproved()
    return aprovado;
}
```

### ✅ Conclusión final: ¿cuál elijo si NO puedo cambiar el nombre?
- **Usa `public boolean isIs_aprovado()`**:
  - Cumple el estándar.
  - Es lo que los frameworks esperan por defecto.
- **NUNCA uses `getIs_aprovado()`** para un `boolean` primitivo.
- **NUNCA tengas ambos métodos a la vez**, porque confundirás a los serializadores y provocarás errores en tiempo de ejecución.

---

## 🖼️ Material de clase

![[Pasted image 20260727150132.png]]

![[Pasted image 20260727151158.png]]
