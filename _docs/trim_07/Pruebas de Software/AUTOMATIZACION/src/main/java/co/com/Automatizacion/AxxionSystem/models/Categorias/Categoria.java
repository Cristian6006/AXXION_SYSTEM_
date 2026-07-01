package co.com.Automatizacion.AxxionSystem.models.Categorias;

import java.util.Date;

public class Categoria {
    private String nombre;
    private String tipoCategoria;
    private String descripcion;
    private String fecha;

    private Categoria() {}

    public String getNombre() { return nombre; }
    public String getTipoCategoria() { return tipoCategoria; }
    public String getDescripcion() { return descripcion; }
    public String getFullCategory() { return nombre + " " + tipoCategoria; }
    public String getFecha() { return fecha; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final Categoria categoria = new Categoria();

        public Builder nombre(String nombre) {
            categoria.nombre = nombre;
            return this;
        }
        public Builder tipoCategoria(String tipoCategoria) {
            categoria.tipoCategoria = tipoCategoria;
            return this;
        }
        public Builder descripcion(String descripcion) {
            categoria.descripcion = descripcion;
            return this;
        }
        public Builder fecha(String fecha) {
            categoria.fecha = fecha;
            return this;
        }

        public Categoria build() { return categoria; }
    }
}
