package co.com.AutomatizacionAxxionSystem.tasks;

import co.com.AutomatizacionAxxionSystem.interactions.EsperarElemento;
import co.com.AutomatizacionAxxionSystem.models.CredencialesInicioSesion;
import co.com.AutomatizacionAxxionSystem.models.SesionVariable;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;

import java.time.Duration;
import java.util.List;

import static co.com.AutomatizacionAxxionSystem.userinterfaces.autenticacion.*;

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
        String usuario = credencial.getUsuario();
        String clave = credencial.getClave();

        // Si se usan credenciales genéricas del Gherkin, las reemplazamos por las de la base de datos local
        if ("student".equalsIgnoreCase(usuario)) {
            usuario = "c@example.com";
            clave = "Su12345678";
        }

        // Si la sesión ya está activa (escenarios outline o re-ejecución), omitir login
        if (sesionActiva(actor)) {
            actor.remember(SesionVariable.usuario.toString(), usuario);
            return;
        }

        actor.attemptsTo(
                EsperarElemento.seaVisible(INPUT_USUARIO),
                Click.on(INPUT_USUARIO),
                Enter.theValue(usuario).into(INPUT_USUARIO),
                Click.on(INPUT_CLAVE),
                Enter.theValue(clave).into(INPUT_CLAVE),
                Click.on(BTN_INICIOSESION),
                EsperarElemento.seaVisible(MENU_AUTENTICADO, Duration.ofSeconds(20))
        );

        actor.remember(SesionVariable.usuario.toString(), usuario);
    }

    private <T extends Actor> boolean sesionActiva(T actor) {
        try {
            return MENU_AUTENTICADO.resolveFor(actor).isDisplayed();
        } catch (Exception ex) {
            return false;
        }
    }
}
