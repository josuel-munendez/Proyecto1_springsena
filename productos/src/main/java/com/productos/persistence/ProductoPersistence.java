package com.productos.persistence;

import com.productos.models.Producto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * ══════════════════════════════════════════════════════════
 *  CAPA DE PERSISTENCIA (DAO) — Microservicio de Productos
 * ══════════════════════════════════════════════════════════
 *
 * Aquí vive TODO el SQL del microservicio. Ninguna otra capa escribe
 * SQL ni conoce MySQL (Separation of Concerns).
 *
 * PATRÓN DE DISEÑO — DAO (Data Access Object):
 * Expone métodos en lenguaje del dominio ("guardarProducto",
 * "listarProductos") y oculta los detalles técnicos (DriverManager,
 * SQL, cursores). Si mañana cambiamos JDBC por JPA o cambiamos de
 * motor de BD, las demás capas no se enteran → BAJO ACOPLAMIENTO.
 *
 * TECNOLOGÍA — JDBC PURO, flujo de cada operación:
 *  1. DriverManager.getConnection(URL, USER, PASSWORD) abre la conexión.
 *  2. prepareStatement(sql) precompila la sentencia con huecos `?`.
 *  3. setX(índice, valor) llena cada `?` en orden (empieza en 1).
 *  4a. INSERT/UPDATE/DELETE → executeUpdate() devuelve # filas afectadas.
 *  4b. SELECT → executeQuery() devuelve un ResultSet (cursor).
 *
 * SEGURIDAD — PREPAREDSTATEMENT CONTRA INYECCIÓN SQL:
 * Los valores viajan SEPARADOS del texto SQL y el driver los trata
 * como datos puros, nunca como código. Un nombre como
 * "'; DROP TABLE producto; --" se guarda literal sin ejecutarse.
 *
 * BUENA PRÁCTICA — TRY-WITH-RESOURCES:
 * Connection/PreparedStatement/ResultSet son recursos del sistema que
 * hay que cerrar; declarados en el try(...) Java los cierra SOLO al
 * salir del bloque (normal o con excepción). Evita fugas de conexiones.
 */
public class ProductoPersistence {

    /**
     * Credenciales de conexión de ESTE microservicio (Database-per-
     * Service: db_productos es exclusiva de productos). Pendiente de
     * mejora futura: externalizarlas con @Value desde application.properties.
     */
    private final String URL = "jdbc:mysql://localhost:3306/db_productos";
    private final String USER = "root";
    private final String PASSWORD = "123456";

    /**
     * CREATE — Inserta un nuevo producto.
     * Las FECHAS NO se envían: MySQL las llena solo
     * (fecha_creacion DEFAULT CURRENT_TIMESTAMP).
     *
     * @param p producto a guardar (sin id ni fechas).
     * @return true si alguna fila fue insertada.
     */
    public boolean guardarProducto(Producto p) {

        // Text block (Java 15+): SQL multilínea legible.
        String sql = """
                INSERT INTO producto (nombre, descripcion, precio_base, activo, aprobado)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, p.getNombre());
            statement.setString(2, p.getDescripcion());
            statement.setDouble(3, p.getPrecioBase());
            // Booleano primitivo → MySQL lo guarda como 1 / 0.
            statement.setBoolean(4, p.isActivo());
            statement.setBoolean(5, p.isAprobado());

            if (statement.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // en producción: almacenar en un log (SLF4J), no imprimir
        }
        return false;
    }

    /**
     * DELETE — Elimina un producto por su id.
     *
     * @param id identificador del producto a eliminar.
     * @return true si existía una fila con ese id y fue borrada.
     */
    public boolean eliminarProducto(Long id) {

        String sql = "DELETE FROM producto WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            if (statement.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * UPDATE — Actualiza un producto existente.
     *
     * DETALLE DE DISEÑO: el UPDATE NO toca fecha_creacion ni
     * fecha_actualizacion. ¿Por qué? Porque fecha_actualizacion tiene
     * ON UPDATE CURRENT_TIMESTAMP en MySQL: cada vez que esta sentencia
     * modifica la fila, la BD refresca la fecha sola. La responsabilidad
     * vive donde mejor resuelta está (la BD), no duplicada en Java.
     *
     * @param p producto CON id y nuevos valores.
     * @return true si la fila existió y fue actualizada.
     */
    public boolean actualizarProducto(Producto p) {

        String sql = """
                UPDATE producto
                SET nombre = ?, descripcion = ?, precio_base = ?, activo = ?, aprobado = ?
                WHERE id = ?
                """;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, p.getNombre());
            statement.setString(2, p.getDescripcion());
            statement.setDouble(3, p.getPrecioBase());
            statement.setBoolean(4, p.isActivo());
            statement.setBoolean(5, p.isAprobado());
            // Último `?`: pertenece al WHERE (evita actualizar TODA la tabla).
            statement.setLong(6, p.getId());

            if (statement.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * READ masivo — Devuelve todos los productos del catálogo.
     *
     * @return lista de productos; vacía (nunca null) si no hay datos
     *         o falló la conexión.
     */
    public List<Producto> listarProductos() {

        List<Producto> productos = new ArrayList<>();

        String sql = """
                SELECT id, nombre, descripcion, precio_base, activo, aprobado,
                       fecha_creacion, fecha_actualizacion
                FROM producto
                """;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                productos.add(mapearFila(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productos;
    }

    /**
     * READ individual — Devuelve UN producto por su id.
     * Lo usa el endpoint GET /{id} cuando el frontend edita un registro.
     *
     * @param id identificador buscado.
     * @return el producto encontrado, o null si no existe.
     */
    public Producto obtenerProducto(Long id) {

        String sql = """
                SELECT id, nombre, descripcion, precio_base, activo, aprobado,
                       fecha_creacion, fecha_actualizacion
                FROM producto
                WHERE id = ?
                """;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapearFila(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * TRADUCTOR fila ↔ objeto (helper privado).
     *
     * BUENA PRÁCTICA — DRY (Don't Repeat Yourself): la conversión
     * ResultSet → Producto estaba repetida en listar() y obtener();
     * ahora existe UNA sola vez. Agregar una columna nueva = cambiar
     * aquí únicamente.
     *
     * @param rs cursor posicionado en la fila actual (.next() ya llamado).
     * @return el Producto construido con esa fila.
     * @throws SQLException si falta una columna o el tipo no coincide.
     */
    private Producto mapearFila(ResultSet rs) throws SQLException {

        Producto producto = new Producto();

        producto.setId(rs.getLong("id"));
        producto.setNombre(rs.getString("nombre"));
        producto.setDescripcion(rs.getString("descripcion"));
        producto.setPrecioBase(rs.getDouble("precio_base"));
        producto.setActivo(rs.getBoolean("activo"));
        producto.setAprobado(rs.getBoolean("aprobado"));
        producto.setFechaCreacion(rs.getString("fecha_creacion"));
        producto.setFechaActualizacion(rs.getString("fecha_actualizacion"));

        return producto;
    }
}
