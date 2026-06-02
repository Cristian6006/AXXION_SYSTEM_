package co.com.Automatizacion.tasks;

import co.com.Automatizacion.model.CredencialesInicioSesion;
import co.com.Automatizacion.model.SesionVariable;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;

import java.util.List;

import static co.com.Automatizacion.userinterfaces.autenticacion.BTN_INICIOSESION;
import static co.com.Automatizacion.userinterfaces.autenticacion.INPUT_CLAVE;
import static co.com.Automatizacion.userinterfaces.autenticacion.INPUT_USUARIO;

public class Autenticarse implements Task {

    private final List<CredencialesInicioSesion> credenciales;

    public Autenticarse(List<CredencialesInicioSesion> credenciales) {
        this.credenciales = credenciales;
    }

    public static Autenticarse aute(List<CredencialesInicioSesion> credenciales) {
        return Tasks.instrumented(Autenticarse.class, credenciales);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        CredencialesInicioSesion credencial = credenciales.get(0);

        actor.attemptsTo(
                Click.on(INPUT_USUARIO),
                Enter.theValue(credencial.getUsuario()).into(INPUT_USUARIO),
                Click.on(INPUT_CLAVE),
                Enter.theValue(credencial.getClave()).into(INPUT_CLAVE),
                Click.on(BTN_INICIOSESION)
        );

        actor.remember(SesionVariable.usuario.toString(), credencial.getUsuario());
    }
}
