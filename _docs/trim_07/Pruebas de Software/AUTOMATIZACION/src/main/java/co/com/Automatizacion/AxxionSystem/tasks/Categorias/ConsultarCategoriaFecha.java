package co.com.Automatizacion.AxxionSystem.tasks.Categorias;

import co.com.Automatizacion.AxxionSystem.models.Categoria;
import co.com.Automatizacion.AxxionSystem.userInterfaces.CategoriasUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Enter;

import java.util.List;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class ConsultarCategoriaFecha implements Task {
    private final List<Categoria> categoria;

    public ConsultarCategoriaFecha(List<Categoria> categoria) { this.categoria = categoria; }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String date = categoria.get(0).getFecha();

        actor.attemptsTo(
                Enter.theValue(date).into(CategoriasUI.INPUT_BUSQUEDA_NOMBRE)
        );
    }

    public static ConsultarCategoriaFecha search(List<Categoria> categoria) {
        return instrumented(ConsultarCategoriaFecha.class, categoria);
    }
}
