# Error de compilación corregido: `variable vr might not have been initialized`

**Archivo:** `demo/src/main/java/Parqueadero/businesslogic/BLVehiculo.java`
**Línea donde fallaba:** 11

---

## ¿Cuál era el error?

```
[ERROR] .../BLVehiculo.java:[11,5] variable vr might not have been initialized
```

Este error **impedía compilar todo el proyecto** (no solo el paquete Parqueadero),
porque Maven compila todos los `.java` juntos. El paquete `com.example.demo_mysql`
también se veía bloqueado por este problema.

## ¿Por qué pasó?

La clase `BLVehiculo` tiene un campo final:

```java
private final VehiculoRepository vr;
```

En Java, un campo `final` **debe inicializarse siempre** (en la declaración o en
un constructor). La clase tenía **dos constructores**:

```java
public BLVehiculo() {                 // <- este NO inicializaba vr
}

public BLVehiculo(VehiculoRepository vr) {
    this.vr = vr;
}
```

El primer constructor (el vacío) no asignaba ningún valor a `vr`. Java no sabe si
ese constructor se va a usar, y como `vr` es `final` y quedó sin inicializar en uno
de los caminos posibles, el compilador lanza el error.

## ¿Cómo se corrigió?

Se inicializó `vr` dentro del constructor vacío:

```java
public BLVehiculo() {
    this.vr = new VehiculoRepository();
}
```

Ahora los **dos** constructores dejan a `vr` inicializado y el proyecto compila.

## ¿Qué se aprendió (fundamento)?

1. Un campo `final` (o un campo sin valor por defecto) debe inicializarse en
   **todos** los constructores que existen en la clase.
2. La inicialización se puede hacer en la declaración:
   `private final VehiculoRepository vr = new VehiculoRepository();`
   o en el constructor. Lo importante es que **ningún camino de creación** deje el
   campo sin valor.
3. Más adelante (programación "correcta" / Spring) esto se reemplaza por la
   **inyección de dependencias** con `@Autowired` o constructores de Spring, que
   se encargan solos de crear las dependencias.
