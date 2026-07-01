package co.com.Automatizacion.AxxionSystem.tasks.Categorias;

import co.com.Automatizacion.AxxionSystem.models.Categorias.Categoria;
import co.com.Automatizacion.AxxionSystem.userInterfaces.Categorias.CategoriasUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Enter;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class ConsultarCategoriaFecha implements Task {
    private final Categoria categoria;

    public ConsultarCategoriaFecha(Categoria categoria) {
        this.categoria = categoria;
    }

    public static ConsultarCategoriaFecha conFecha(Categoria categoria) {
        return instrumented(ConsultarCategoriaFecha.class, categoria);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String fechaActual = categoria.getFecha();
        actor.attemptsTo(
                Enter.theValue(fechaActual).into(CategoriasUI.INPUT_BUSQUEDA_FECHA)
        );
    }
}
