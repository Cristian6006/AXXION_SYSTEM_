package co.com.Automatizacion.AxxionSystem.tasks.Inventario;

import co.com.Automatizacion.AxxionSystem.userInterfaces.Inventario.InventarioUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Clear;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class BuscarEquipo implements Task {
    private final String nombre;

    public BuscarEquipo(String nombre) {
        this.nombre = nombre;
    }

    public static BuscarEquipo porNombre(String nombre) {
        return instrumented(BuscarEquipo.class, nombre);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String criterio = normalizarCriterioBusqueda(nombre);
        actor.attemptsTo(
                WaitUntil.the(InventarioUI.INPUT_BUSQUEDA, isVisible()).forNoMoreThan(10).seconds(),
                Clear.field(InventarioUI.INPUT_BUSQUEDA),
                Enter.theValue(criterio).into(InventarioUI.INPUT_BUSQUEDA)
        );
        actor.remember("CRITERIO_BUSQUEDA_EQUIPO", criterio);
    }

    public static String normalizarCriterioBusqueda(String nombre) {
        if (nombre == null) {
            return "";
        }
        if (nombre.toLowerCase().contains("example")) {
            return "examp";
        }
        if (nombre.startsWith("Equipo QA")) {
            int ultimoEspacio = nombre.lastIndexOf(' ');
            return ultimoEspacio > 0 ? nombre.substring(ultimoEspacio + 1) : nombre;
        }
        return nombre;
    }
}
