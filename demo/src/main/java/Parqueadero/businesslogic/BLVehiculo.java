package Parqueadero.businesslogic;

import Parqueadero.entity.Vehiculo;

import Parqueadero.repository.VehiculoRepository;

/**
 * ══════════════════════════════════════════════════════════
 *  CAPA DE NEGOCIO (Business Logic) — Módulo Vehículos
 * ══════════════════════════════════════════════════════════
 *
 * Es el "cerebro" del módulo: aplica las REGLAS DEL NEGOCIO
 * (validaciones) ANTES de permitir el acceso a los datos.
 *
 * QUÉ HACE ESTA CAPA:
 *  - Recibe peticiones del Controller (nunca del frontend directo).
 *  - Valida que los datos cumplan las reglas (placa de 6 caracteres,
 *    marca obligatoria, modelo de 4 caracteres...).
 *  - SOLO SI la validación pasa, delega en la capa de persistencia.
 *
 * REGLA DE ORO DE LA ARQUITECTURA POR CAPAS: cada capa solo habla con
 * la capa vecina. Este BL conoce a VehiculoRepository, pero NUNCA al
 * Controller ni al frontend. Eso produce ALTA COHESIÓN (cada clase
 * hace una sola cosa bien) y BAJO ACOPLAMIENTO (cambiar una capa no
 * rompe las demás).
 *
 * PATRÓN DE DISEÑO — INYECCIÓN DE DEPENDENCIAS MANUAL (constructor):
 * El BL no crea su dependencia "de cualquier manera" ni la busca él
 * mismo: la RECIBE por constructor. ¿Por qué?
 *  - Facilita PRUEBAS: en un test puedo pasarle un repositorio falso
 *    (mock) sin tocar MySQL.
 *  - Reduce acoplamiento: la clase depende de "un repositorio que me
 *    den", no de construirlo ella misma.
 * En Spring esto se automatiza con @Autowired; aquí lo hacemos a mano
 * para entender el fundamento.
 */
public class BLVehiculo {

    /**
     * Dependencia de acceso a datos. `final` = una vez asignada en el
     * constructor no puede reasignarse (inmutabilidad de referencias:
     * buena práctica para dependencias).
     */
    private final VehiculoRepository vr;

    /**
     * Constructor por defecto: crea su propio repositorio.
     * Es el camino normal en producción.
     */
    public BLVehiculo() {
        this.vr = new VehiculoRepository();
    }

    /**
     * Constructor con inyección manual de dependencias.
     *
     * @param vr repositorio a usar (permite inyectar uno falso en pruebas).
     */
    public BLVehiculo(VehiculoRepository vr) {
        this.vr = vr;
    }

    /**
     * Sobrecarga de validación con parámetros sueltos (id, placa, modelo).
     * NOTA: método sin uso actual; se conserva como ejemplo de
     * SOBRECARGA DE MÉTODOS (mismo nombre, distinta firma). Pendiente
     * de implementar o eliminar.
     *
     * @param id     identificador del vehículo.
     * @param placa  placa del vehículo.
     * @param modelo modelo/año.
     * @return siempre false por ahora.
     */
    public boolean validarVehiculo(long id, String placa, String modelo) {
        return false;
    }

    /**
     * VALIDA un vehículo aplicando las reglas del negocio.
     *
     * Técnica usada: GUARD CLAUSES ("cláusulas guardián") + FAIL FAST.
     * En vez de anidar if dentro de if dentro de if ("flecha de la
     * muerte"), cada regla se evalúa en línea y si falla se retorna
     * false INMEDIATAMENTE. Ventajas: código plano, legible y cada
     * validación es independiente.
     *
     * Además cada regla verifica primero el caso nulo (placa == null)
     * antes de llamar .length(), evitando la excepción
     * NullPointerException (buena práctica defensiva).
     *
     * @param v el vehículo a validar.
     * @return true si pasa TODAS las validaciones.
     */
    public boolean validarVehiculo(Vehiculo v) {
        // Regla 1: el objeto no puede ser nulo.
        if (v == null) {
            System.out.println("Datos vacíos");
            return false;
        }

        // Regla 2: la placa debe existir y tener 6 caracteres.
        String placa = v.getPlaca();
        if (placa == null || placa.length() != 6) {
            System.out.println("Placa incompleta (debe tener 6 caracteres)");
            return false;
        }

        // Regla 3: la marca no puede ser nula ni estar vacía.
        String marca = v.getMarca();
        if (marca == null || marca.isBlank()) {
            System.out.println("Marca vacía");
            return false;
        }

        // Regla 4: el modelo debe tener 4 caracteres (ej. "2026").
        String modelo = v.getModelo();
        if (modelo == null || modelo.length() != 4) {
            System.out.println("Modelo inválido (debe tener 4 caracteres)");
            return false;
        }

        return true;
    }

    /**
     * Elimina un vehículo por placa. Solo delega en el repositorio
     * si la placa cumple la regla del negocio (6 caracteres).
     *
     * @param placa placa del vehículo a eliminar.
     * @return true si se eliminó correctamente.
     */
    public boolean eliminarVehiculo(String placa){
        if (placa != null && placa.length() == 6){
            return vr.eliminarVehiculo(placa);
        }
        return false;
    }


    /**
     * Consulta un vehículo por placa. Aplica la misma validación
     * antes de consultar; si la placa es inválida devuelve null
     * (convención: null = "no encontrado / petición inválida").
     *
     * @param placa placa buscada.
     * @return el vehículo o null.
     */
    public Vehiculo consultarVehiculo(String placa){
        if (placa != null && placa.length() == 6){
            return vr.consultarVehiculo(placa);
        }else {
            return null;
        }
    }
}
