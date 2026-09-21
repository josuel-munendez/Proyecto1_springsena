package com.productos.businesslogic;

import com.productos.models.Producto;

import java.util.List;

/**
 * Interfaz de negocio para el microservicio de Productos.
 * Define el contrato que cualquier implementación debe cumplir.
 */
public interface IProductoBL {

    boolean validarProducto(Producto p);

    boolean guardarProducto(Producto p);

    boolean eliminarProducto(Long id);

    boolean actualizarProducto(Producto p);

    List<Producto> listarProductos();

    Producto obtenerProducto(Long id);

    List<Producto> listarPaginado(int pagina, int tamanio);
}
