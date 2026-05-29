package co.com.Automatizacion.AxxionSystem.utils.hooks;

import co.com.Automatizacion.AxxionSystem.models.Categoria;
import co.com.Automatizacion.AxxionSystem.models.Usuario;
import io.cucumber.java.DataTableType;
import org.jspecify.annotations.NonNull;

import java.util.Map;

public class DataTableConfig {
    @DataTableType
    public Usuario credencialesInicioSesion(Map<String, String> fila) {
        Usuario credenciales = new Usuario();

        credenciales.setUsuario(fila.get("usuario"));
        credenciales.setContraseña(fila.get("contraseña"));

        return credenciales;
    }

    @DataTableType
    public Categoria nuevaCategoria(@NonNull Map<String, String> fila) {
        Categoria categoria = new Categoria();

        categoria.setNombre(fila.get("nombre"));
        categoria.setTipoCategoria(fila.get("tipoCategoria"));
        categoria.setDescripcion(fila.get("descripcion"));

        return categoria;
    }
}
