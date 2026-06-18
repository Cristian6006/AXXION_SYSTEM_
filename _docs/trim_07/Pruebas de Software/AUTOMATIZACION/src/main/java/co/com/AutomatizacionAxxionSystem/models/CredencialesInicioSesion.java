package co.com.AutomatizacionAxxionSystem.models;

public class CredencialesInicioSesion {

    private String usuario;
    private String clave;



    public CredencialesInicioSesion(String usuario, String clave) {
        this.usuario = usuario;
        this.clave = clave;
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
