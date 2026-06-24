package co.com.Automatizacion.AxxionSystem.tasks.Categorias;

import co.com.Automatizacion.AxxionSystem.models.Categorias.Categoria;
import co.com.Automatizacion.AxxionSystem.userInterfaces.Categorias.CategoriasUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class AgregarCategoria implements Task {
    private final Categoria categoria;

    public AgregarCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public static AgregarCategoria con(Categoria categoria) {
        return instrumented(AgregarCategoria.class, categoria);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(CategoriasUI.BOTON_AGREGAR_CATEGORIAS),
                Enter.theValue(categoria.getNombre()).into(CategoriasUI.INPUT_NOMBRE),
                Enter.theValue(categoria.getTipoCategoria()).into(CategoriasUI.INPUT_TIPO_CATEGORIA),
                Enter.theValue(categoria.getDescripcion()).into(CategoriasUI.INPUT_DESCRIPCION),
                Click.on(CategoriasUI.BOTON_CREAR)
        );
    }
}
