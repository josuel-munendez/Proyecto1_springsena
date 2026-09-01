# 🗺️ Roadmap y Pendientes — Qué le falta al proyecto para estar completo

> **Objetivo:** contrastar lo que promete el `README.md` con lo que realmente está
> implementado, y entregar un **plan de cierre** ordenado y accionable para dejar el
> proyecto 100% completo antes de la sustentación (24 de agosto).

---

## 1. La descripción del README vs. la realidad

El `README.md` original describe el proyecto así:

> *"Proyecto springboot con la tecnologia jdbc, buenas practicas, **encriptacion de
> datos**, arquitectura de **microservicios**, consumo de apis con **fetch**, validacion
> de datos en el front end y en el backend, programacion por capas, documentacion de
> codigo, **pruebas de la api con postman**, **paginacion de datos**."*

A continuación, requisito por requisito, qué está hecho y qué falta:

| # | Requisito prometido        | Estado real | Detalle |
| :-- | :------------------------- | :---------- | :------ |
| 1 | JDBC para MySQL            | 🟡 Parcial  | `usuarios` y `productos` usan JDBC puro. `vehiculos` usa **JPA + H2**, no JDBC/MySQL. |
| 2 | Encriptación de datos      | ⏳ Fuera    | No hay BCrypt. `Usuario` **no tiene campo `password`**. No se guarda nada cifrado. |
| 3 | Arquitectura de microservicios | 🟡 Parcial | 3 proyectos independientes con puerto/BD propios ✅, pero **no se comunican por REST** y no hay gateway. `vehiculos` es una app MVC (Thymeleaf), no expone API REST. |
| 4 | Consumo de API con `fetch` | 🟡 Parcial  | `usuarios` y `productos` sí (JSON + fetch). `vehiculos` usa **Thymeleaf** (renderizado en servidor). |
| 5 | Validación frontend + backend | ✅ Hecho | JS valida y el BL re-valida (Defense in Depth). |
| 6 | Programación por capas     | ✅ Hecho    | model / businesslogic / persistence / controler en los 3 módulos. |
| 7 | Documentación de código    | ✅ Hecho    | Javadoc extenso + `docs/`. |
| 8 | Pruebas con Postman        | 🟡 Parcial  | Endpoints listos para probar, pero **no hay colección Postman ni capturas** versionadas. |
| 9 | Paginación de datos        | ⏳ Fuera    | No hay `page`/`size` ni `LIMIT ? OFFSET ?`. `listar*()` devuelve toda la tabla. |

**Conclusión:** el proyecto está a un **~60–65%** de la descripción. Las dos grandes
funcionalidades ausentes son **encriptación** y **paginación**.

---

## 2. Correcciones críticas de calidad de código (seguridad)

Estas son las mismas que ya se identificaron en `notas/Séptimo Semestre/TAREAS_PENDIENTES.md`
y siguen sin aplicarse:

| # | Problema | Dónde | Corrección |
| :-- | :------- | :---- | :--------- |
| 1 | **Credenciales hardcodeadas** (`URL`, `USER`, `PASSWORD`) en el código fuente. | `UsuarioPersistency`, `ProductoPersistence` | Inyectarlas con `@Value("${spring.datasource.url}")` desde `application.properties`. |
| 2 | **`e.printStackTrace()`** en los `catch`. | Todas las persistencias | Reemplazar por **SLF4J/Logback**: `log.error("mensaje", e)`. |
| 3 | **Sin `password` ni encriptación** en `Usuario`. | `usuarios` | Agregar campo `password`, usar `BCryptPasswordEncoder` en la capa `BL`, columna en `schema.sql`. |
| 4 | **Tipografía**: campo `propetario` (falta la "i"). | `vehiculos/entity/Vehiculo` | Renombrar a `propietario` y ajustar persistencia/templates. |

---

## 3. Funcionalidades que faltan

### 3.1 Encriptación de contraseñas (BCrypt)
- Agregar dependencia `spring-security-crypto` en `usuarios/pom.xml`.
- Campo `password VARCHAR(255)` en `Usuario` y en la tabla `usuario` (`schema.sql`).
- En `UsuarioBL.registrarUsuario()`: `u.setPassword(encoder.encode(...))`.
- Crear un **DTO** (`UsuarioDTO`) **sin** el campo `password` para no filtrar el hash en el JSON.

### 3.2 Paginación end-to-end
- `Controller`: recibir `@RequestParam(defaultValue = "1") int page` y `size`.
- `BL`: calcular `offset = (page - 1) * size`.
- `Persistence`: `SELECT ... LIMIT ? OFFSET ?`.
- Frontend: controles **Anterior / Siguiente** y `GET /api/usuarios?page=1&size=5`.

### 3.3 Comunicación entre microservicios (REST)
- Agregar **`RestTemplate`** a un consumidor y registrar el bean.
- Ej.: `ms-parqueadero` consulta el saldo de un usuario en `http://localhost:8081/api/usuarios/{id}`.
- Dejar evidencia de los 3 puertos y 3 BDs correctos.

### 3.4 Convertir `vehiculos` en un verdadero microservicio REST (opcional para el trimestre)
- Exponer `/api/vehiculos` con `@RestController` además de (o en lugar de) las vistas Thymeleaf.
- Cambiar la base a MySQL (o conservar H2, documentado como decisión).

---

## 4. Pruebas con Postman (colección + capturas)

- [ ] Crear la carpeta de colección (o un archivo JSON de colección Postman) con los 4 verbos.
- [ ] Guardar **bodies de ejemplo** para POST/PUT de usuarios y productos.
- [ ] Tomar **capturas de pantalla** de cada verbo y pegarlas en la sustentación.

---

## 5. Limpieza técnica del repositorio

- [ ] Decidir el destino de `beta/` y `demo/` (versionan código antiguo duplicado).
- [ ] Corregir los `pom.xml` de `usuarios` y `vehiculos` (tienen campos `<name/>`, `<description/>`, licencias y desarrolladores vacíos).
- [ ] Eliminar o consolidar la clase `ParqueaderoApplication` (placeholder) en `vehiculos`.
- [ ] Alinear `docs/SPRING_REST_DOCUMENTATION.md` con el código real (documenta `vehiculos` como `@RestController`, pero es `@Controller` + Thymeleaf).

---

## 6. Checklist final de entrega (sustentación)

- [ ] Credenciales externalizadas con `@Value`.
- [ ] Logger SLF4J (sin `printStackTrace`).
- [ ] Encriptación BCrypt + DTO sin password.
- [ ] Paginación de datos en usuarios y productos (front + back).
- [ ] Comunicación REST entre al menos 2 microservicios (RestTemplate).
- [ ] Colección y capturas de Postman.
- [ ] Javadoc al día.
- [ ] Permisos de carpeta correctos y repo limpio en GitHub.

---

## 7. Orden sugerido de trabajo

1. **Seguridad crítica**: `@Value` + logger (rápido, alto valor).
2. **BCrypt + DTO** en usuarios.
3. **Paginación** en usuarios y productos.
4. **REST entre microservicios** con `RestTemplate`.
5. **Postman** (colección + capturas).
6. **Limpieza** y sustento.
