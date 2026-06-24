package co.com.Automatizacion.AxxionSystem.utils.hooks;
import io.cucumber.java.DataTableType;
import co.com.Automatizacion.AxxionSystem.models.Autenticacion.Credenciales;
import java.util.Map;

public class DataTableTransformers {
        @DataTableType
        public Credenciales credencialesInicioSesion(Map<String, String> entry) {
        Credenciales credenciales = new Credenciales();

            credenciales.setUsuario(entry.get("usuario"));
            credenciales.setClave(entry.get("clave"));

            return credenciales;
        }
    }
