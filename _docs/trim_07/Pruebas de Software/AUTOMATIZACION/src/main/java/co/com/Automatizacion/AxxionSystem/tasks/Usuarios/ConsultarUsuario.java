package co.com.Automatizacion.AxxionSystem.tasks.Usuarios;

import co.com.Automatizacion.AxxionSystem.models.Usuarios.Usuario;
import co.com.Automatizacion.AxxionSystem.userInterfaces.Usuarios.UsuariosUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Enter;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class ConsultarUsuario implements Task {
    private final Usuario usuario;

    public ConsultarUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public static ConsultarUsuario conNombre(Usuario usuario) {
        return instrumented(ConsultarUsuario.class, usuario);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Enter.theValue(usuario.getPrimerNombre()).into(UsuariosUI.INPUT_BUSQUEDA_NOMBRE)
        );
    }
}
