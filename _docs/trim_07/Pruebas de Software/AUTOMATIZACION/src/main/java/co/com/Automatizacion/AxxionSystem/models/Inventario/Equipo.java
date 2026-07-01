package co.com.Automatizacion.AxxionSystem.models.Inventario;

public class Equipo {
    private String nombre;
    private String marca;
    private String modelo;
    private String serie;
    private String categoria;
    private String tarifaDiaria;

    private Equipo() {}

    public String getNombre() {
        return nombre;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public String getSerie() {
        return serie;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getTarifaDiaria() {
        return tarifaDiaria;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Equipo equipo = new Equipo();

        public Builder nombre(String nombre) {
            equipo.nombre = nombre;
            return this;
        }

        public Builder marca(String marca) {
            equipo.marca = marca;
            return this;
        }

        public Builder modelo(String modelo) {
            equipo.modelo = modelo;
            return this;
        }

        public Builder serie(String serie) {
            equipo.serie = serie;
            return this;
        }

        public Builder categoria(String categoria) {
            equipo.categoria = categoria;
            return this;
        }

        public Builder tarifaDiaria(String tarifaDiaria) {
            equipo.tarifaDiaria = tarifaDiaria;
            return this;
        }

        public Equipo build() {
            if (equipo.nombre == null || equipo.nombre.isBlank()) {
                throw new IllegalArgumentException("El nombre del equipo es obligatorio");
            }
            if (equipo.marca == null || equipo.marca.isBlank()) {
                throw new IllegalArgumentException("La marca del equipo es obligatoria");
            }
            if (equipo.modelo == null || equipo.modelo.isBlank()) {
                throw new IllegalArgumentException("El modelo del equipo es obligatorio");
            }
            if (equipo.serie == null || equipo.serie.isBlank()) {
                throw new IllegalArgumentException("La serie del equipo es obligatoria");
            }
            if (equipo.categoria == null || equipo.categoria.isBlank()) {
                throw new IllegalArgumentException("La categoría del equipo es obligatoria");
            }
            if (equipo.tarifaDiaria == null || equipo.tarifaDiaria.isBlank()) {
                throw new IllegalArgumentException("La tarifa diaria del equipo es obligatoria");
            }
            return equipo;
        }
    }
}
