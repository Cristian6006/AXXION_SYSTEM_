package co.com.Automatizacion.AxxionSystem.stepDefintions.Categorias;

import co.com.Automatizacion.AxxionSystem.factory.Categoria.CategoriaFactory;
import co.com.Automatizacion.AxxionSystem.models.Categorias.Categoria;
import co.com.Automatizacion.AxxionSystem.questions.Categorias.CategoriaExiste;
import co.com.Automatizacion.AxxionSystem.tasks.Categorias.EditarCategoria;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;

public class EditarCategoriaStepDefinition {
    private Categoria updateCategory;

    @Cuando("el usuario actualiza el nombre de la categoría")
    public void el_usuario_actualiza_el_nombre_de_la_categoría() {
        Actor actor = OnStage.theActorInTheSpotlight();
        Categoria categoriaActual = actor.recall("CATEGORIA");
        updateCategory = CategoriaFactory.updateCategory(categoriaActual);
        actor.attemptsTo(EditarCategoria.con(updateCategory));
    }
    @Entonces("que la categoría debería estar visible en la tabla")
    public void que_la_categoría_debería_estar_visible_en_la_tabla() {
        Actor actor = OnStage.theActorInTheSpotlight();
        actor.should(seeThat(CategoriaExiste.enLaLista(updateCategory)));
    }
}
