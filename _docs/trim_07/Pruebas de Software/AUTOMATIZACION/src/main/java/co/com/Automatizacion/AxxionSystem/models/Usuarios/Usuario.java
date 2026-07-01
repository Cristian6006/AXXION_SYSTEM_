package co.com.Automatizacion.AxxionSystem.models.Usuarios;

public class Usuario {
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String nombreUsuario;
    private String email;
    private String telefono;
    private String clave;
    private String fullName;

    private Usuario() {}

    public String getPrimerNombre() { return primerNombre; }
    public String getSegundoNombre() { return segundoNombre; }
    public String getPrimerApellido() { return primerApellido; }
    public String getSegundoApellido() { return segundoApellido; }
    public String getNombreUsuario() { return nombreUsuario; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public String getClave() { return clave; }
    public String getFullName() { return primerNombre + " " + segundoNombre + " " + primerApellido + " " + segundoApellido; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Usuario usuario = new Usuario();

        public Builder primerNombre(String primerNombre) {
            usuario.primerNombre = primerNombre;
            return this;
        }

        public Builder segundoNombre(String segundoNombre) {
            usuario.segundoNombre = segundoNombre;
            return this;
        }

        public Builder primerApellido(String primerApellido) {
            usuario.primerApellido = primerApellido;
            return this;
        }

        public Builder segundoApellido(String segundoApellido) {
            usuario.segundoApellido = segundoApellido;
            return this;
        }

        public Builder nombreUsuario(String nombreUsuario) {
            usuario.nombreUsuario = nombreUsuario;
            return this;
        }

        public Builder email(String email) {
            usuario.email = email;
            return this;
        }

        public Builder telefono(String telefono) {
            usuario.telefono = telefono;
            return this;
        }

        public Builder clave(String clave) {
            usuario.clave = clave;
            return this;
        }

        public Usuario build() {
            return usuario;
        }
    }
}
