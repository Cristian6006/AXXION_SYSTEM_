package co.com.Automatizacion.AxxionSystem.tasks;

import co.com.Automatizacion.AxxionSystem.models.Usuario;
import co.com.Automatizacion.AxxionSystem.userInterfaces.LoginUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class IniciarSesion implements Task {
    private final Usuario usuario;

    public IniciarSesion(Usuario usuario) {
        this.usuario = usuario;
    }


    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Enter.theValue(usuario.getCorreo()).into(LoginUI.INPUT_EMAIL),
                Enter.theValue(usuario.getClave()).into(LoginUI.INPUT_PASSWORD),
                Click.on(LoginUI.BOTON_ACCEDER)
        );
    }

    public static IniciarSesion con(Usuario usuario) {
        return instrumented(IniciarSesion.class, usuario);
    }
}
