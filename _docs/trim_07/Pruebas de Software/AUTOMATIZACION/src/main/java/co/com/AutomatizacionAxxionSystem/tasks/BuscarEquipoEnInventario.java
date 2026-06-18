package co.com.AutomatizacionAxxionSystem.tasks;

import co.com.AutomatizacionAxxionSystem.interactions.EsperarFiltroInventario;
import co.com.AutomatizacionAxxionSystem.models.SesionVariable;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Clear;
import net.serenitybdd.screenplay.actions.Enter;

import static co.com.AutomatizacionAxxionSystem.userinterfaces.InventarioUI.INPUT_BUSQUEDA;

/**
 * Task: escribe un criterio en el campo de búsqueda global del inventario.
 */
public class BuscarEquipoEnInventario implements Task {

    private final String criterio;

    public BuscarEquipoEnInventario(String criterio) {
        this.criterio = criterio;
    }

    public static BuscarEquipoEnInventario conCriterio(String criterio) {
        return Tasks.instrumented(BuscarEquipoEnInventario.class, criterio);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        if (criterio == null || criterio.isBlank()) {
            throw new IllegalArgumentException("El criterio de búsqueda no puede estar vacío");
        }

        actor.attemptsTo(
                Clear.field(INPUT_BUSQUEDA),
                Enter.theValue(criterio).into(INPUT_BUSQUEDA),
                EsperarFiltroInventario.estabilizado()
        );

        actor.remember(SesionVariable.criterioBusqueda.toString(), criterio);
    }
}
