package co.com.Automatizacion.AxxionSystem.tasks;

import co.com.Automatizacion.AxxionSystem.userInterfaces.LoginUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class IniciarSesion implements Task {
    private final String correo;
    private final String clave;

    public IniciarSesion(String correo, String clave) {
        this.correo = correo;
        this.clave = clave;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Enter.theValue(correo).into(LoginUI.INPUT_EMAIL),
                Enter.theValue(clave).into(LoginUI.INPUT_PASSWORD),
                Click.on(LoginUI.BOTON_ACCEDER)
        );
    }

    public static IniciarSesion conCredenciales(String correo, String clave) {
        return instrumented(IniciarSesion.class, correo, clave);
    }
}
