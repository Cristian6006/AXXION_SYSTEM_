package co.com.Automatizacion.AxxionSystem.tasks.Categorias;

import co.com.Automatizacion.AxxionSystem.models.Categoria;
import co.com.Automatizacion.AxxionSystem.userInterfaces.CategoriasUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.openqa.selenium.Keys;

import java.util.List;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class EditarCategoria implements Task {
    private final List<Categoria> categoria;

    public EditarCategoria(List<Categoria> categoria) { this.categoria = categoria; }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String newname = categoria.get(0).getNuevoNombre();
        String name = categoria.get(0).getNombre();
        String borrarTodo = Keys.chord(Keys.CONTROL, "a") + Keys.BACK_SPACE;

        actor.attemptsTo(
                Click.on(CategoriasUI.BOTON_EDITAR_CATEGORIA.of(name)),
                WaitUntil.the(CategoriasUI.INPUT_NOMBRE, isVisible()).forNoMoreThan(5).seconds(),
                Enter.theValue(borrarTodo).into(CategoriasUI.INPUT_NOMBRE),
                Enter.theValue(newname).into(CategoriasUI.INPUT_NOMBRE).thenHit(Keys.TAB),
                Click.on(CategoriasUI.BOTON_CONFIRMAR_EDITAR)
        );
    }

    public static EditarCategoria edit(List<Categoria> categoria) {
        return instrumented(EditarCategoria.class, categoria);
    }
}
