package co.com.Automatizacion.AxxionSystem.tasks.Categorias;

import co.com.Automatizacion.AxxionSystem.models.Categoria;
import co.com.Automatizacion.AxxionSystem.tasks.AgregarCategoria;
import co.com.Automatizacion.AxxionSystem.userInterfaces.CategoriasUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.waits.WaitUntil;

import java.util.List;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class BorrarCategoria implements Task {
    private final List<Categoria> categoria;

    public BorrarCategoria(List<Categoria> categoria) { this.categoria = categoria; }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String name = categoria.get(0).getNombre();

        actor.attemptsTo(
                Click.on(CategoriasUI.BOTON_BORRAR_CATEGORIA.of(name)),
                WaitUntil.the(CategoriasUI.BOTON_CONFIRMAR_BORRAR, isVisible()).forNoMoreThan(3).seconds(),
                Click.on(CategoriasUI.BOTON_CONFIRMAR_BORRAR)
        );
    }

    public static BorrarCategoria delete(List<Categoria> categoria) {
        return instrumented(BorrarCategoria.class, categoria);
    }
}

