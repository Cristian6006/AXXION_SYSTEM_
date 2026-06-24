package co.com.Automatizacion.AxxionSystem.tasks.Categorias;

import co.com.Automatizacion.AxxionSystem.models.Categorias.Categoria;
import co.com.Automatizacion.AxxionSystem.userInterfaces.Categorias.CategoriasUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class BorrarCategoria implements Task {
    private final Categoria categoria;

    public BorrarCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public static BorrarCategoria con(Categoria categoria) {
        return instrumented(BorrarCategoria.class, categoria);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String nombre = categoria.getNombre();

        actor.attemptsTo(
                Click.on(CategoriasUI.BOTON_BORRAR_CATEGORIA.of(nombre)),
                WaitUntil.the(CategoriasUI.BOTON_CONFIRMAR_BORRAR, isVisible()).forNoMoreThan(3).seconds(),
                Click.on(CategoriasUI.BOTON_CONFIRMAR_BORRAR)
        );
    }
}
