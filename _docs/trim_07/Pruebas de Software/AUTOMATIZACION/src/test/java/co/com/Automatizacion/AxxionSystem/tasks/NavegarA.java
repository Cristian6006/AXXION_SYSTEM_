package co.com.Automatizacion.AxxionSystem.tasks;

import co.com.Automatizacion.AxxionSystem.userInterfaces.CategoriasUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class NavegarA implements Task {

    @Override
    public <T extends Actor> void  performAs(T actor) {
        actor.attemptsTo(
                Click.on(CategoriasUI.BOTON_NAV_CATEGORIAS)
        );
    }
     public static NavegarA laPaginaCategorias() {
        return instrumented(NavegarA.class);
     }
}
