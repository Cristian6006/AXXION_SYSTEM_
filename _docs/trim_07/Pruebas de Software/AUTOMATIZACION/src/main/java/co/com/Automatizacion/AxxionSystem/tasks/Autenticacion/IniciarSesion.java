package co.com.Automatizacion.AxxionSystem.tasks.Autenticacion;

import co.com.Automatizacion.AxxionSystem.models.Autenticacion.Credenciales;
import co.com.Automatizacion.AxxionSystem.userInterfaces.Autenticacion.LoginPage;
import co.com.Automatizacion.AxxionSystem.userInterfaces.Autenticacion.LoginUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.Open;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class IniciarSesion implements Task {
    private final Credenciales credenciales;

    public IniciarSesion(Credenciales credenciales) {
        this.credenciales = credenciales;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Open.browserOn(new LoginPage()),
                Enter.theValue(credenciales.getUsuario()).into(LoginUI.INPUT_EMAIL),
                Enter.theValue(credenciales.getClave()).into(LoginUI.INPUT_CLAVE),
                Click.on(LoginUI.BOTON_ACCEDER)
        );
    }

    public static Performable comoAdministrador() {
        return instrumented(
                IniciarSesion.class,
                Credenciales.admin()
        );
    }
}
