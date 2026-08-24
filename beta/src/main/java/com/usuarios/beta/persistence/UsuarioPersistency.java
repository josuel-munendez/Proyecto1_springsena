package com.usuarios.beta.persistence;

import com.usuarios.beta.models.Usuario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * ══════════════════════════════════════════════════════════
 *  CAPA DE PERSISTENCIA (DAO) — Microservicio de Usuarios
 * ══════════════════════════════════════════════════════════
 *
 * Aquí vive TODO el SQL del microservicio: guardar, eliminar,
 * actualizar, listar y consultar por id. Ninguna otra capa escribe
 * SQL (Separation of Concerns).
 *
 * PATRÓN DE DISEÑO — DAO (Data Access Object):
 * La clase expone métodos en lenguaje del dominio
 * ("guardarUsuario", "listarUsuarios") y OCULTA cómo se habla con
 * MySQL. Si mañana cambiamos a otro motor o a JPA, el resto de la
 * aplicación no cambia ni una línea: solo esta clase. Eso es BAJO
 * ACOPLAMIENTO entre negocio y datos.
 *
 * TECNOLOGÍA — JDBC PURO (bajo el capó de Spring):
 *  1. DriverManager.getConnection(URL, USER, PASSWORD)
 *     → abre una conexión real contra MySQL.
 *  2. connection.prepareStatement(sql)
 *     → prepara la sentencia con huecos `?` (precompilada).
 *  3. statement.setX(1, valor) → rellena cada `?` de forma segura.
 *  4. executeUpdate() para INSERT/UPDATE/DELETE (devuelve # filas
 *     afectadas) o executeQuery() para SELECT (devuelve ResultSet).
 *  5. ResultSet → cursor que recorre las filas con .next().
 *
 * SEGURIDAD — ¿POR QUÉ PreparedStatement Y NO CONCATENAR STRINGS?
 * Los parámetros `?` se envían SEPARADOS del SQL; el driver los trata
 * como DATOS, nunca como código SQL. Así se previene la INYECCIÓN SQL
 * (ej. que alguien escriba nombre = "'; DROP TABLE usuario; --").
 *
 * BUENA PRÁCTICA — TRY-WITH-RESOURCES:
 * Connection, PreparedStatement y ResultSet son RECURSOS DEL SISTEMA
 * (sockets con la BD). Declarados dentro del try(...), Java los cierra
 * SOLO al terminar (incluso si hay excepción). Sin esto, una fuga de
 * conexiones tumba el servidor en producción.
 */
public class UsuarioPersistency {

    /**
     * Credenciales de conexión de este microservicio (constantes de
     * instancia). Cada microservicio apunta a SU base de datos
     * (patrón Database-per-Service). Pendiente de mejora futura:
     * externalizarlas con @Value desde application.properties.
     */
    private final String URL = "jdbc:mysql://localhost:3306/mi_base_datos";
    private final String USER = "root";
    private final String PASSWORD = "123456";

    /**
     * CREATE — INSERTA un nuevo usuario en la tabla.
     * El id NO se envía: lo genera AUTO_INCREMENT en MySQL.
     *
     * @param u usuario a guardar (sin id).
     * @return true si alguna fila fue insertada (executeUpdate > 0).
     */
    public boolean guardarUsuario(Usuario u) {

        // Text block (Java 15+): permite escribir SQL multilínea legible.
        String sql = """
                INSERT INTO usuario (nombre, direccion, telefono, correo, saldo)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Se asignan los `?` EN ORDEN, empezando en 1 (no en 0).
            statement.setString(1, u.getNombre());
            statement.setString(2, u.getDireccion());
            statement.setInt(3, u.getTelefono());
            statement.setString(4, u.getCorreo());
            statement.setInt(5, u.getSaldo());

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
     * DELETE — Elimina un usuario por su id.
     *
     * @param id identificador del usuario a eliminar.
     * @return true si existía una fila con ese id y fue borrada.
     */
    public boolean eliminarUsuario(Long id) {

        String sql = "DELETE FROM usuario WHERE id = ?";

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
     * UPDATE — Actualiza los datos de un usuario existente.
     *
     * @param u usuario CON id y con los nuevos valores en el resto
     *          de atributos.
     * @return true si la fila existió y fue actualizada.
     */
    public boolean actualizarUsuario(Usuario u) {

        String sql = """
                UPDATE usuario
                SET nombre = ?, direccion = ?, telefono = ?, correo = ?, saldo = ?
                WHERE id = ?
                """;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, u.getNombre());
            statement.setString(2, u.getDireccion());
            statement.setInt(3, u.getTelefono());
            statement.setString(4, u.getCorreo());
            statement.setInt(5, u.getSaldo());
            // El último `?` corresponde al WHERE: sin él actualizaría TODAS las filas.
            statement.setLong(6, u.getId());

            if (statement.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * READ — Devuelve TODOS los usuarios de la tabla.
     *
     * @return lista de usuarios; vacía (nunca null) si no hay datos
     *         o si falló la conexión (el frontend puede iterar sin miedo).
     */
    public List<Usuario> listarUsuarios() {

        List<Usuario> usuarios = new ArrayList<>();

        String sql = """
                SELECT id, nombre, direccion, telefono, correo, saldo
                FROM usuario
                """;

        // El ResultSet también va en el try-with-resources: se cierra solo.
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            // .next() mueve el cursor fila por fila; false al acabarse.
            while (resultSet.next()) {
                usuarios.add(mapearFila(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuarios;
    }

    /**
     * READ individual — Devuelve UN usuario por su id.
     * Lo usa el endpoint GET /{id} cuando el frontend edita un registro.
     *
     * @param id identificador buscado.
     * @return el usuario encontrado, o null si no existe (convención).
     */
    public Usuario obtenerUsuario(Long id) {

        String sql = """
                SELECT id, nombre, direccion, telefono, correo, saldo
                FROM usuario
                WHERE id = ?
                """;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            // try anidado: el ResultSet necesita cerrarse también.
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
     * BUENA PRÁCTICA — DRY (Don't Repeat Yourself): convertir un
     * ResultSet en objeto Usuario era código repetido en listar() y
     * obtener(); ahora vive aquí una sola vez. Si mañana se agrega una
     * columna nueva, se cambia en UN lugar.
     *
     * @param rs fila actual del cursor (ya posicionada con .next()).
     * @return el Usuario construido con los valores de esa fila.
     * @throws SQLException si una columna no existe o el tipo no coincide.
     */
    private Usuario mapearFila(ResultSet rs) throws SQLException {

        Usuario usuario = new Usuario();

        usuario.setId(rs.getLong("id"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setDireccion(rs.getString("direccion"));
        usuario.setTelefono(rs.getInt("telefono"));
        usuario.setCorreo(rs.getString("correo"));
        usuario.setSaldo(rs.getInt("saldo"));

        return usuario;
    }
}
