package RED.productos.catalogo;

public class Producto {
    protected long id;
    protected String nombre;
    protected double precio_base;
    protected String descripcion;
    protected boolean activo;
    protected boolean aprovado;
    protected String fecha_creacion;
    protected String fecha_actualización;

    public Producto() {
    }

    public Producto(String nombre, long id, double precio_base, String descripcion, boolean activo, boolean aprovado, String fecha_creacion, String fecha_actualización) {
        this.nombre = nombre;
        this.id = id;
        this.precio_base = precio_base;
        this.descripcion = descripcion;
        this.activo = activo;
        this.aprovado = aprovado;
        this.fecha_creacion = fecha_creacion;
        this.fecha_actualización = fecha_actualización;
    }

    /* public void setId(long id) {
        this.id = id;
    } */

    public void setId(long idR) {
        id = idR;
    }

    public long getId() {
        return id;
    }

    public void setNombre(String nombreR) {
        nombre = nombreR;
    }

    public String getNombre() {
        return nombre;
    }

    public void setPrecio_base(double precio_baseR) {
        precio_base = precio_baseR;
    }

    public double getPrecio_base() {
        return precio_base;
    }

    public void setDescripcion(String descripcionR) {
        descripcion = descripcionR;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setIs_activo(boolean esta_activo) {
        activo = esta_activo;
    }

    public boolean is_activo() {
        return activo;
    }

    public void setIs_aprovado(boolean esta_aprovado) {
        aprovado = esta_aprovado;
    }

    public boolean is_aprovado() {
        return aprovado;
    }

    public void setFecha_creacion(String fecha_creacionR) {
        fecha_creacion = fecha_creacionR;
    }

    public String getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_actualización(String fecha_actualizaciónR) {
        fecha_actualización = fecha_actualizaciónR;
    }

    public String getFecha_actualización() {
        return fecha_actualización;
    }
}
