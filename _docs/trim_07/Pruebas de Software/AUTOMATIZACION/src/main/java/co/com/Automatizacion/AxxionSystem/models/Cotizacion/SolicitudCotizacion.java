package co.com.Automatizacion.AxxionSystem.models.Cotizacion;

public class SolicitudCotizacion {
    private String cliente;
    private String equipo;
    private String fechaInicio;
    private String fechaFin;

    private SolicitudCotizacion() {}

    public String getCliente() {
        return cliente;
    }

    public String getEquipo() {
        return equipo;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public String getFechaInicioFormateada() {
        return fechaInicio + "T10:00";
    }

    public String getFechaFinFormateada() {
        return fechaFin + "T18:00";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final SolicitudCotizacion solicitud = new SolicitudCotizacion();

        public Builder cliente(String cliente) {
            solicitud.cliente = cliente;
            return this;
        }

        public Builder equipo(String equipo) {
            solicitud.equipo = equipo;
            return this;
        }

        public Builder fechaInicio(String fechaInicio) {
            solicitud.fechaInicio = fechaInicio;
            return this;
        }

        public Builder fechaFin(String fechaFin) {
            solicitud.fechaFin = fechaFin;
            return this;
        }

        public SolicitudCotizacion build() {
            if (solicitud.cliente == null || solicitud.cliente.isBlank()) {
                throw new IllegalArgumentException("El cliente es obligatorio");
            }
            if (solicitud.equipo == null || solicitud.equipo.isBlank()) {
                throw new IllegalArgumentException("El equipo es obligatorio");
            }
            if (solicitud.fechaInicio == null || solicitud.fechaInicio.isBlank()) {
                throw new IllegalArgumentException("La fecha de inicio es obligatoria");
            }
            if (solicitud.fechaFin == null || solicitud.fechaFin.isBlank()) {
                throw new IllegalArgumentException("La fecha de fin es obligatoria");
            }
            return solicitud;
        }
    }
}
