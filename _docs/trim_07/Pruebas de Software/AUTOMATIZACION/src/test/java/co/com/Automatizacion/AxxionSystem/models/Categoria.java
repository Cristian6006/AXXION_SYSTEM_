package co.com.Automatizacion.AxxionSystem.models;

public class Categoria {
    private String nombre;
    private String tipoCategoria;
    private String descripcion;
    private String nuevoNombre;
    private String fecha;


    public String getNombre() {return nombre;}
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipoCategoria() {return tipoCategoria;}
    public void setTipoCategoria(String tipoCategoria) { this.tipoCategoria = tipoCategoria;}

    public String getDescripcion() {return descripcion;}
    public void setDescripcion(String descripcion) { this.descripcion = descripcion;}

    public String getNuevoNombre() {return nuevoNombre;}
    public void setNuevoNombre(String nuevoNombre) { this.nuevoNombre = nuevoNombre; }

    public String getFecha() {return fecha;}
    public void setFecha(String fecha) { this.fecha = fecha; }
}
