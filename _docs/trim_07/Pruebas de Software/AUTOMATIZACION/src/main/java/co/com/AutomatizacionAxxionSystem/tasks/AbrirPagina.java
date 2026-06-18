package co.com.AutomatizacionAxxionSystem.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Open;
import co.com.AutomatizacionAxxionSystem.userinterfaces.Inicio;

public class AbrirPagina implements Task {

    public static AbrirPagina laPagina() {
        return Tasks.instrumented(AbrirPagina.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Open.browserOn(new Inicio()));
    }
}
