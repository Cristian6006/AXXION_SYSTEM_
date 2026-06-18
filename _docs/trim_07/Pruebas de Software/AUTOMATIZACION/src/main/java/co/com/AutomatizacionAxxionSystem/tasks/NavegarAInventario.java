package co.com.AutomatizacionAxxionSystem.tasks;

import co.com.AutomatizacionAxxionSystem.interactions.EsperarCargaInventario;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Open;


public class NavegarAInventario implements Task {

    public static NavegarAInventario elModulo() {
        return Tasks.instrumented(NavegarAInventario.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Open.url("http://localhost:5173/Inventory"),
                EsperarCargaInventario.completa()
        );
    }
}
