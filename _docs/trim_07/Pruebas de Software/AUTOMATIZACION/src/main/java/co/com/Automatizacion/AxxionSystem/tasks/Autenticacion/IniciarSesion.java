package co.com.Automatizacion.AxxionSystem.tasks.Autenticacion;

import co.com.Automatizacion.AxxionSystem.models.Autenticacion.Credenciales;
import co.com.Automatizacion.AxxionSystem.userInterfaces.Autenticacion.LoginUI;
import co.com.Automatizacion.AxxionSystem.userInterfaces.Dashboard.DashboardUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.Open;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isClickable;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class IniciarSesion implements Task {
    private static final String LOGIN_URL = "https://axxion-frontend.onrender.com/login";

    private final Credenciales credenciales;

    public IniciarSesion(Credenciales credenciales) {
        this.credenciales = credenciales;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Open.url(LOGIN_URL),
                WaitUntil.the(LoginUI.INPUT_EMAIL, isVisible()).forNoMoreThan(60).seconds(),
                Enter.theValue(credenciales.getUsuario()).into(LoginUI.INPUT_EMAIL),
                Enter.theValue(credenciales.getClave()).into(LoginUI.INPUT_CLAVE),
                WaitUntil.the(LoginUI.BOTON_ACCEDER, isClickable()).forNoMoreThan(10).seconds(),
                Click.on(LoginUI.BOTON_ACCEDER),
                WaitUntil.the(DashboardUI.TITULO_DASHBOARD, isVisible()).forNoMoreThan(60).seconds()
        );
    }

    public static Performable comoAdministrador() {
        return instrumented(
                IniciarSesion.class,
                Credenciales.admin()
        );
    }
}
