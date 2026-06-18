package co.com.AutomatizacionAxxionSystem.models;

/**
 * Modelo de datos para la creación de un equipo en el inventario.
 * Representa los campos requeridos por el formulario EquipmentForm del sistema Axxion.
 */
public class DatosEquipo {

    private String nombre;
    private String marca;
    private String modelo;
    private String numeroSerie;
    private String categoria;
    private String tarifaDiaria;

    // Constructor completo
    public DatosEquipo(String nombre, String marca, String modelo,
                       String numeroSerie, String categoria, String tarifaDiaria) {
        this.nombre = nombre;
        this.marca = marca;
        this.modelo = modelo;
        this.numeroSerie = numeroSerie;
        this.categoria = categoria;
        this.tarifaDiaria = tarifaDiaria;
    }

    // Constructor simplificado (sin modelo ni categoría, para el escenario InventarioAgregarEquipoExito)
    public DatosEquipo(String nombre, String marca, String numeroSerie, String tarifaDiaria) {
        this.nombre = nombre;
        this.marca = marca;
        this.modelo = "N/A";
        this.numeroSerie = numeroSerie;
        this.categoria = "Equipos de Sonido"; // categoría por defecto alineada con el formulario
        this.tarifaDiaria = tarifaDiaria;
    }

    public String getNombre()       { return nombre; }
    public String getMarca()        { return marca; }
    public String getModelo()       { return modelo; }
    public String getNumeroSerie()  { return numeroSerie; }
    public String getCategoria()    { return categoria; }
    public String getTarifaDiaria() { return tarifaDiaria; }

    public void setNombre(String nombre)             { this.nombre = nombre; }
    public void setMarca(String marca)               { this.marca = marca; }
    public void setModelo(String modelo)             { this.modelo = modelo; }
    public void setNumeroSerie(String numeroSerie)   { this.numeroSerie = numeroSerie; }
    public void setCategoria(String categoria)       { this.categoria = categoria; }
    public void setTarifaDiaria(String tarifaDiaria) { this.tarifaDiaria = tarifaDiaria; }
}
