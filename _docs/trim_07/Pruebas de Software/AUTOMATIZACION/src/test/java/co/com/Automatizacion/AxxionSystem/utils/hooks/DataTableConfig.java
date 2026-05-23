package co.com.Automatizacion.AxxionSystem.utils.hooks;

import co.com.Automatizacion.AxxionSystem.models.Usuario;
import io.cucumber.java.DataTableType;
import java.util.Map;

public class DataTableConfig {
    @DataTableType
    public Usuario credencialesInicioSesion(Map<String, String> fila) {
        Usuario credenciales = new Usuario();

        credenciales.setUsuario(fila.get("usuario"));

        credenciales.setContraseña(fila.get("contraseña"));

        return credenciales;
    }
}
