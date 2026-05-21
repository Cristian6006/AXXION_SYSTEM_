package co.com.Automatizacion.AxxionSystem.models;

public class Usuario {
    private final String correo;
    private final String clave;

    public Usuario(String correo, String clave) {
        this.correo = correo;
        this.clave = clave;
    }

    public String getCorreo() { return correo; }
    public String getClave() { return clave; }
}
