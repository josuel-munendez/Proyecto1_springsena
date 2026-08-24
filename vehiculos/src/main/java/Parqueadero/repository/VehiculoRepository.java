package Parqueadero.repository;

import Parqueadero.entity.Vehiculo;

/**
 * ══════════════════════════════════════════════════════════
 *  CAPA DE PERSISTENCIA / ACCESO A DATOS — Módulo Vehículos
 * ══════════════════════════════════════════════════════════
 *
 * PATRÓN DE DISEÑO: DAO (Data Access Object).
 * ¿Cómo funciona? La clase actúa como "intermediaria" entre el negocio
 * y la base de datos: expone métodos con lenguaje del dominio
 * (eliminarVehiculo, consultarVehiculo) y OCULTA los detalles técnicos
 * (SQL, conexiones). Si mañana cambiamos de MySQL a PostgreSQL o a una
 * API externa, las demás capas NO se enteran: solo cambia esta clase.
 * Eso es bajo acoplamiento.
 *
 * ESTADO ACTUAL (STUB): los métodos devuelven datos de mentira
 * (hardcodeados) porque la conexión real a MySQL de este módulo aún no
 * se implementó. Es una técnica válida en desarrollo: permite avanzar
 * y probar las CAPAS SUPERIORES mientras la persistencia real llega.
 *
 * BUENA PRÁCTICA APLICADA: "programar contra el comportamiento, no
 * contra los detalles". El BL solo conoce estos métodos; no sabe nada
 * de JDBC ni de SQL.
 */
public class VehiculoRepository {

    /**
     * Elimina un vehículo del parqueadero por su placa.
     * (Pendiente: implementar el DELETE real en MySQL).
     *
     * @param placa placa del vehículo a eliminar (6 caracteres).
     * @return true si se eliminó (por ahora siempre true: stub).
     */
    public boolean eliminarVehiculo(String placa){
        return true;
    }

    /**
     * Consulta UN vehículo por su placa.
     * (Pendiente: implementar el SELECT real; hoy devuelve un objeto
     * de prueba con marca "Mazda" para poder probar el flujo completo
     * Controller → BL → Repository sin base de datos).
     *
     * @param placa placa del vehículo buscado (6 caracteres).
     * @return el vehículo encontrado o null si no existe.
     */
    public Vehiculo consultarVehiculo(String placa){
        Vehiculo v = new Vehiculo();
        v.setPlaca(placa);
        v.setMarca("Mazda");
        return v;
    }
}
