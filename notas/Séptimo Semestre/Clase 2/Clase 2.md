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
