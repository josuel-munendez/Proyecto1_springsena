# ms-parqueadero (Vehículos)

Microservicio **Vehículos / Parqueadero** — Spring Boot + **JPA** + **Thymeleaf**.
Puerto **8080** | Base de datos: **H2 en memoria**.

> **Nota:** a diferencia de `usuarios` y `productos` (JDBC puro + `fetch`), este módulo
> usa **JPA / Spring Data** y renderiza sus vistas con **Thymeleaf** en el servidor.

## Arquitectura por capas

```
src/main/java/
├── springsena/vehiculos/VehiculosApplication.java  → punto de entrada (@SpringBootApplication)
└── Parqueadero/
    ├── entity/Vehiculo.java          → Entidad JPA (tabla `vehiculos`)
    ├── repository/VehiculoRepository.java → Spring Data JPA
    ├── businesslogic/BLVehiculo.java → Reglas de negocio (@Service)
    └── controller/ControllerVehiculo.java → Controlador web (@Controller + Thymeleaf)

src/main/resources/templates/vehiculos/  → vistas Thymeleaf (listar, formulario, detalle)
```

## Reglas de negocio (BLVehiculo)

- **Placa:** obligatoria, exactamente **6 caracteres**, única.
- **Marca:** obligatoria (no vacía).
- **Modelo:** obligatorio, exactamente **4 caracteres** (año).

## Cómo correr

```bash
./mvnw spring-boot:run
```

Abrir `http://localhost:8080/vehiculos`.

- **H2 Console:** `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:parqueadero` · User: `sa` · Password: *(vacío)*

## Endpoints (vistas web)

| Ruta | Método | Descripción |
| :--- | :----- | :---------- |
| `/vehiculos` | GET | Lista todos los vehículos |
| `/vehiculos/nuevo` | GET | Formulario para crear |
| `/vehiculos/{id}` | GET | Detalle de un vehículo |
| `/vehiculos/{id}/editar` | GET | Formulario para editar |
| `/vehiculos/guardar` | POST | Guarda (crea o actualiza) |
| `/vehiculos/{id}/eliminar` | POST | Elimina un vehículo |
