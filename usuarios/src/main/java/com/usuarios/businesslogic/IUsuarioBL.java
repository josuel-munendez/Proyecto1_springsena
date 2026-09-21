package com.usuarios.businesslogic;

import com.usuarios.models.Usuario;

import java.util.List;

/**
 * Interfaz de negocio para el microservicio de Usuarios.
 * Define el contrato que cualquier implementación debe cumplir.
 */
public interface IUsuarioBL {

    boolean validarUsuario(Usuario u);

    boolean registrarUsuario(Usuario u);

    boolean eliminarUsuario(Long id);

    boolean actualizarUsuario(Usuario u);

    List<Usuario> listarUsuarios();

    Usuario obtenerUsuario(Long id);

    List<Usuario> listarPaginado(int pagina, int tamanio);
}
