package co.com.Automatizacion.AxxionSystem.models.Autenticacion;

public class Credenciales {
    private String usuario;
    private String clave;

    public Credenciales() {}

    public Credenciales(String usuario, String clave) {
        this.usuario = usuario;
        this.clave = clave;
    }

    public static Credenciales admin() {
        return new Credenciales(
            "p@example.com",
                "12345678"
        );
    }

    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }
    public void setClave(String clave) {
        this.clave = clave;
    }
}
