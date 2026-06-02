package co.com.Automatizacion.AxxionSystem.tasks;

import co.com.Automatizacion.AxxionSystem.models.Usuario;
import co.com.Automatizacion.AxxionSystem.userInterfaces.LoginUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;

import java.util.List;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class IniciarSesion implements Task {
    private final List<Usuario> credenciales;

    public IniciarSesion(List<Usuario> credenciales) {
        this.credenciales = credenciales;
    }


    @Override
    public <T extends Actor> void performAs(T actor) {
        String user = credenciales.get(0).getUsuario();
        String pass = credenciales.get(0).getContraseña();

        actor.attemptsTo(
                Enter.theValue(user).into(LoginUI.INPUT_EMAIL),
                Enter.theValue(pass).into(LoginUI.INPUT_PASSWORD),
                Click.on(LoginUI.BOTON_ACCEDER)
        );
    }

    public static IniciarSesion aute(List<Usuario> credenciales) {
        return instrumented(IniciarSesion.class, credenciales);
    }
}
