package co.com.Automatizacion.AxxionSystem.stepDefintions.Usuarios.helper;

import co.com.Automatizacion.AxxionSystem.factory.Usuario.UsuarioFactory;
import co.com.Automatizacion.AxxionSystem.models.Usuarios.Usuario;
import co.com.Automatizacion.AxxionSystem.userInterfaces.Usuarios.UsuariosUI;
import net.serenitybdd.screenplay.actors.OnStage;

public class UsuarioHelper {
    private UsuarioHelper() {}

    public static Usuario crearUsuario() {
        Usuario usuario = UsuarioFactory.ramdomUser();
        OnStage.theActorInTheSpotlight()
                .remember("USUARIO", usuario);
        return usuario;
    }
}
