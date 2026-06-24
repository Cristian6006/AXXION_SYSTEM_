package co.com.Automatizacion.AxxionSystem.stepDefintions.Categorias;

import co.com.Automatizacion.AxxionSystem.models.Categorias.Categoria;
import co.com.Automatizacion.AxxionSystem.questions.Categorias.CategoriaExiste;
import co.com.Automatizacion.AxxionSystem.stepDefintions.Categorias.helper.CategoriaHelper;
import co.com.Automatizacion.AxxionSystem.tasks.Categorias.AgregarCategoria;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;

public class AgregarCategoriaStepDefinition {
    @Cuando("este crea una nueva categoría")
    public void el_usuario_crea_una_nueva_categoría() {
        Categoria categoriaActual = CategoriaHelper.crearCategoria();
        OnStage.theActorInTheSpotlight().attemptsTo(
                AgregarCategoria.con(categoriaActual)
        );
    }
    @Entonces("la categoría debería estar visible en la lista de categorías")
    public void la_categoría_debería_estar_visible_en_la_lista_de_categorías() {
        Actor actor = OnStage.theActorInTheSpotlight();
        Categoria categoriaActual = actor.recall("CATEGORIA");
        actor.should(
                seeThat(CategoriaExiste.enLaLista(categoriaActual))
        );
    }
}
