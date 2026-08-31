package Parqueadero.repository;

import Parqueadero.entity.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ══════════════════════════════════════════════════════════
 *  CAPA DE PERSISTENCIA — Módulo Vehículos
 * ══════════════════════════════════════════════════════════
 *
 * Interfaz JPA que extiende JpaRepository para operaciones CRUD
 * sobre la tabla "vehiculos". Spring genera la implementación
 * automáticamente.
 *
 * JpaRepository提供: findAll(), findById(), save(), delete(), etc.
 * Método personalizado: findByPlaca() para buscar por placa.
 */
@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    Optional<Vehiculo> findByPlaca(String placa);

    boolean existsByPlaca(String placa);
}
