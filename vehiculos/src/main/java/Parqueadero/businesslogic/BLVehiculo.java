package Parqueadero.businesslogic;

import Parqueadero.entity.Vehiculo;
import Parqueadero.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * ══════════════════════════════════════════════════════════
 *  CAPA DE NEGOCIO (Business Logic) — Módulo Vehículos
 * ══════════════════════════════════════════════════════════
 *
 * @Service: le indica a Spring que esta clase es un bean de servicio
 * y debe ser gestionada por el contenedor IoC.
 *
 * @Autowired: Spring inyecta automáticamente la dependencia del
 * repositorio (ya no se crea manualmente con new).
 */
@Service
public class BLVehiculo {

    private final VehiculoRepository vr;

    @Autowired
    public BLVehiculo(VehiculoRepository vr) {
        this.vr = vr;
    }

    /**
     * Lista todos los vehículos registrados.
     */
    public List<Vehiculo> listarTodos() {
        return vr.findAll();
    }

    /**
     * Busca un vehículo por su ID.
     */
    public Optional<Vehiculo> buscarPorId(long id) {
        return vr.findById(id);
    }

    /**
     * Busca un vehículo por su placa.
     */
    public Optional<Vehiculo> buscarPorPlaca(String placa) {
        if (placa == null || placa.length() != 6) {
            return Optional.empty();
        }
        return vr.findByPlaca(placa);
    }

    /**
     * Guarda un vehículo (crear o actualizar).
     * Aplica las reglas del negocio antes de persistir.
     *
     * @return el vehículo guardado o null si la validación falla.
     */
    public Vehiculo guardar(Vehiculo v) {
        if (!validarVehiculo(v)) {
            return null;
        }
        return vr.save(v);
    }

    /**
     * Elimina un vehículo por su ID.
     *
     * @return true si se eliminó, false si no existía.
     */
    public boolean eliminar(long id) {
        if (vr.existsById(id)) {
            vr.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * VALIDA un vehículo aplicando las reglas del negocio.
     * Guard Clauses + Fail Fast.
     */
    public boolean validarVehiculo(Vehiculo v) {
        if (v == null) {
            return false;
        }
        String placa = v.getPlaca();
        if (placa == null || placa.length() != 6) {
            return false;
        }
        String marca = v.getMarca();
        if (marca == null || marca.isBlank()) {
            return false;
        }
        String modelo = v.getModelo();
        if (modelo == null || modelo.length() != 4) {
            return false;
        }
        return true;
    }
}
