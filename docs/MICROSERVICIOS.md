# 🧩 Microservicios — Concepto y Cómo se Aplica Aquí

Documento conceptual para entender **qué es un microservicio**, por qué se eligió esta
arquitectura y cómo se ve en este proyecto (clave para la sustentación).

---

## 1. ¿Qué es un microservicio?

Un **microservicio** es un servicio **pequeño, autónomo e independiente** que implementa
una única capacidad del negocio. A diferencia de un **monolito** (una sola aplicación que
lo hace todo), cada microservicio:

- Tiene su **propio proceso** (se ejecuta y se detiene por separado).
- Tiene **su propia base de datos** (patrón *Database-per-Service*).
- Expone **su propia API** (normalmente REST).
- Se **despliega y escala** de forma independiente.
- Se comunica con los demás **por red (HTTP)**.

```
         Monolito                          Microservicios
   ┌───────────────────────┐      ┌────────┐   ┌────────┐   ┌────────┐
   │ app.jar (todo: users, │      │ users  │   │product │   │vehicles│
   │ productos, vehiculos) │      │ :8081  │   │ :8082  │   │ :8080  │
   │ una BD, un puerto     │      │ BD-A   │   │ BD-B   │   │ BD-C   │
   └───────────────────────┘      └────────┘   └────────┘   └────────┘
     todo junto = siishdd todo   cada uno independiente = fallas aisladas
```

**Ventajas:** fallas aisladas, despliegue independiente, escalado selectivo, equipos
autónomos.
**Costos:** más complejidad de red, de despliegue y de observabilidad.

---

## 2. Evidencia de microservicios en este proyecto

Cada módulo es un **proyecto Maven independiente** (`pom.xml` propio) con su propio
proceso, puerto y base de datos:

| Microservicio | Carpeta | Puerto | Base de datos | Tecnología |
| :------------ | :------ | :----- | :------------ | :--------- |
| **ms-parqueadero** (Vehículos) | `vehiculos/` | 8080 | H2 (memoria) | JPA + Thymeleaf |
| **ms-usuarios** | `usuarios/` | 8081 | `mi_base_datos` | JDBC puro |
| **ms-productos** | `productos/` | 8082 | `db_productos` | JDBC puro |

**Prueba física de la separación:** tres `pom.xml`, tres `main`, tres puertos y dos
bases de datos en MySQL + una en memoria. Si `productos` cae, `usuarios` sigue vivo.

```
   [Navegador]                [Postman]
       │ fetch                     │ HTTP
       ▼                           ▼
 ┌──────────────┐  ┌──────────────┐  ┌──────────────┐
 │ ms-usuarios  │  │ ms-productos │  │ ms-parqueadero│
 │ :8081        │  │ :8082        │  │ :8080        │
 │ mi_base_datos│  │ db_productos │  │ H2           │
 └──────────────┘  └──────────────┘  └──────────────┘
```

---

## 3. ¿Es realmente una arquitectura de microservicios completa?

**Parcialmente.** Se cumple la parte de **independencia** (procesos, puertos y BDs
separadas), que es el requisito mínimo señalado por el profesor. Sin embargo, para que
sea un sistema de microservicios **integrado** faltaría:

1. **Comunicación entre ellos por REST** (`RestTemplate`) — p. ej. que `ms-parqueadero`
   consulte el saldo de un usuario en `ms-usuarios`.
2. **Un API REST** en `vehiculos` (hoy usa Thymeleaf, no `@RestController`).
3. **Opcional para el trimestre:** Gateway / API Gateway, registro de servicios,
   balanceo de carga, Docker.

> Para la sustentación del **trimestre** basta con demonstrar 2+ proyectos en puertos
> distintos y comunicación REST entre al menos dos. Ver `ROADMAP_Y_PENDIENTES.md`.

---

## 4. El patrón Database-per-Service

Cada microservicio posee **su propia base de datos**:

- `usuarios` → `mi_base_datos` (tabla `usuario`).
- `productos` → `db_productos` (tabla `producto`).
- `vehiculos` → H2 en memoria (tabla `vehiculos`).

**Ventaja:** un microservicio no toca las tablas del otro; los esquemas evolucionan de
forma independiente. **Costo:** las consultas entre entidades requieren llamadas REST.

---

## 5. Glosario

| Término | Significado |
| :------ | :---------- |
| **Monolito** | Una sola aplicación que concentra toda la lógica y un solo punto de falla. |
| **Microservicio** | Servicio pequeño, autónomo e independiente con su propio proceso/BD/API. |
| **Database-per-Service** | Cada microservicio tiene su propia base de datos exclusiva. |
| **API REST** | Interfaz de comunicación sobre HTTP usando verbos (GET, POST, PUT, DELETE). |
| **RestTemplate** | Cliente HTTP de Spring para que un servicio llame al API de otro. |
| **Gateway** | Punto único de entrada que enruta peticiones a los microservicios. |
