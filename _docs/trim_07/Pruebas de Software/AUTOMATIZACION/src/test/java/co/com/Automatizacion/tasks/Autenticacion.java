package co.com.Automatizacion.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import co.com.Automatizacion.model.CredencialesInicioSesion;
import java.util.List;

public class Autenticacion implements Task {
    private final List<CredencialesInicioSesion> credenciales;

    private Autenticacion(List<CredencialesInicioSesion> credenciales) {
        this.credenciales = credenciales;
    }

    public static Autenticacion withCredentials(List<CredencialesInicioSesion> credenciales) {
        return Tasks.instrumented(Autenticacion.class, credenciales);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Autenticarse.aute(credenciales));
    }
}
