package co.com.Automatizacion.AxxionSystem.tasks.Categorias;

import co.com.Automatizacion.AxxionSystem.models.Categorias.Categoria;
import co.com.Automatizacion.AxxionSystem.userInterfaces.Categorias.CategoriasUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Enter;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class ConsultarCategoriaNombre implements Task {
    private final Categoria categoria;

    public ConsultarCategoriaNombre(Categoria categoria) {
        this.categoria = categoria;
    }

    public static ConsultarCategoriaNombre conNombre(Categoria categoria) {
        return instrumented(ConsultarCategoriaNombre.class, categoria);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String nombre = categoria.getNombre();

        actor.attemptsTo(
                Enter.theValue(nombre).into(CategoriasUI.INPUT_BUSQUEDA_NOMBRE)
        );
    }
}
