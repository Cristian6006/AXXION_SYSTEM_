package co.com.AutomatizacionAxxionSystem.utils.hooks;
import io.cucumber.java.DataTableType;
import co.com.AutomatizacionAxxionSystem.models.CredencialesInicioSesion;

import java.util.Map;
public class DataTableTransformers
    {

        @DataTableType
        public CredencialesInicioSesion credencialesInicioSesion(Map<String, String> entry) {
        return new CredencialesInicioSesion(
                entry.get("usuario"),
                entry.get("clave")
        );
    }
    }
