package co.com.Automatizacion.AxxionSystem.stepDefintions.Categorias;

import co.com.Automatizacion.AxxionSystem.models.Categorias.Categoria;
import co.com.Automatizacion.AxxionSystem.questions.Categorias.CategoriaExiste;
import co.com.Automatizacion.AxxionSystem.tasks.Categorias.BorrarCategoria;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.GivenWhenThen;
import net.serenitybdd.screenplay.actors.OnStage;

import static org.hamcrest.Matchers.is;

public class BorrarCategoriaStepDefinition {
    @Cuando("el usuario elimina la categoría por su nombre")
    public void el_usuario_elimina_la_categoría_por_su_nombre() {
        Actor actor = OnStage.theActorInTheSpotlight();
        Categoria categoriaActual = actor.recall("CATEGORIA");
        actor.attemptsTo(BorrarCategoria.con(categoriaActual));
    }
    @Entonces("que la categoría ya no debería existir en el sistema")
    public void que_la_categoría_ya_no_debería_existir_en_el_sistema() {
        Actor actor = OnStage.theActorInTheSpotlight();
        Categoria categoriaActual = actor.recall("CATEGORIA");
        actor.should(GivenWhenThen.seeThat(
                        "La ausencia en la lista de la categoria" + categoriaActual,
                        CategoriaExiste.enLaLista(categoriaActual),
                        is(false)
                )
        );
    }
}
