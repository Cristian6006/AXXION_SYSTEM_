package co.com.Automatizacion.AxxionSystem.tasks.Categorias;

import co.com.Automatizacion.AxxionSystem.models.Categorias.Categoria;
import co.com.Automatizacion.AxxionSystem.userInterfaces.Categorias.CategoriasUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class EditarCategoria implements Task {
    private final Categoria updatedCategory;

    public EditarCategoria(Categoria updatedCategory) {
        this.updatedCategory = updatedCategory;
    }

    public static EditarCategoria con(Categoria updatedCategory) {
        return instrumented(EditarCategoria.class, updatedCategory);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String nombre = updatedCategory.getNombre().trim();
        actor.attemptsTo(
                Click.on(CategoriasUI.BOTON_EDITAR_CATEGORIA.of(nombre)),
                LimpiarCampo.con(
                        CategoriasUI.INPUT_TIPO_CATEGORIA,
                        updatedCategory.getTipoCategoria()
                ),
                LimpiarCampo.con(
                        CategoriasUI.INPUT_DESCRIPCION,
                        updatedCategory.getDescripcion()
                ),
                Click.on(CategoriasUI.BOTON_CONFIRMAR_EDITAR)
        );
    }
}
