package co.com.Automatizacion.AxxionSystem.tasks.Categorias;

import co.com.Automatizacion.AxxionSystem.models.Categoria;
import co.com.Automatizacion.AxxionSystem.userInterfaces.CategoriasUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.waits.WaitUntil;

import java.util.List;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class AgregarCategoria implements Task {
    private final List<Categoria> categoria;

    public AgregarCategoria(List<Categoria> categoria) { this.categoria = categoria; }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String name = categoria.get(0).getNombre();
        String type = categoria.get(0).getTipoCategoria();
        String description = categoria.get(0).getDescripcion();


        actor.attemptsTo(
                Click.on(CategoriasUI.BOTON_AGREGAR_CATEGORIAS),
                WaitUntil.the(CategoriasUI.INPUT_NOMBRE, isVisible()).forNoMoreThan(5).seconds(),
                Enter.theValue(name).into(CategoriasUI.INPUT_NOMBRE),
                Enter.theValue(type).into(CategoriasUI.INPUT_TIPO_CATEGORIA),
                Enter.theValue(description).into(CategoriasUI.INPUT_DESCRIPCION),
                Click.on(CategoriasUI.BOTON_CREAR)
        );
    }

    public static AgregarCategoria add(List<Categoria> categoria) {
        return instrumented(AgregarCategoria.class, categoria);
    }
}
