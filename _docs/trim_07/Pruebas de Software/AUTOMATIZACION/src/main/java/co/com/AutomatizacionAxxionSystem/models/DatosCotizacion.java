package co.com.AutomatizacionAxxionSystem.models;

/**
 * Modelo de datos para el proceso de generación de una cotización.
 * Encapsula cliente, equipo a cotizar y rango de fechas.
 */
public class DatosCotizacion {

    private String nombreCliente;
    private String nombreEquipo;
    private String fechaInicio;
    private String fechaFin;

    public DatosCotizacion(String nombreCliente, String nombreEquipo,
                           String fechaInicio, String fechaFin) {
        this.nombreCliente = nombreCliente;
        this.nombreEquipo  = nombreEquipo;
        this.fechaInicio   = fechaInicio;
        this.fechaFin      = fechaFin;
    }

    public String getNombreCliente() { return nombreCliente; }
    public String getNombreEquipo()  { return nombreEquipo; }
    public String getFechaInicio()   { return fechaInicio; }
    public String getFechaFin()      { return fechaFin; }

    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    public void setNombreEquipo(String nombreEquipo)   { this.nombreEquipo = nombreEquipo; }
    public void setFechaInicio(String fechaInicio)     { this.fechaInicio = fechaInicio; }
    public void setFechaFin(String fechaFin)           { this.fechaFin = fechaFin; }
}
