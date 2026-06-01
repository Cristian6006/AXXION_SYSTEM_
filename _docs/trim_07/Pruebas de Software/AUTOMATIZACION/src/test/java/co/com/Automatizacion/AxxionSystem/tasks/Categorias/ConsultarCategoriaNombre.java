package co.com.Automatizacion.AxxionSystem.tasks.Categorias;

import co.com.Automatizacion.AxxionSystem.models.Categoria;
import co.com.Automatizacion.AxxionSystem.userInterfaces.CategoriasUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Enter;
import java.util.List;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class ConsultarCategoriaNombre implements Task {
    private final List<Categoria> categoria;

    public ConsultarCategoriaNombre(List<Categoria> categoria) { this.categoria = categoria; }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String name = categoria.get(0).getNombre();

        actor.attemptsTo(
                Enter.theValue(name).into(CategoriasUI.INPUT_BUSQUEDA_NOMBRE)
        );
    }

    public static ConsultarCategoriaNombre search(List<Categoria> categoria) {
        return instrumented(ConsultarCategoriaNombre.class, categoria);
    }
}

