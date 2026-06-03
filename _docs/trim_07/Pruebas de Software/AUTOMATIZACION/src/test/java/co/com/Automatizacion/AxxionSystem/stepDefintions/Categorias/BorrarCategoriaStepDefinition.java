package co.com.Automatizacion.AxxionSystem.stepDefintions.Categorias;

import co.com.Automatizacion.AxxionSystem.models.Categoria;
import co.com.Automatizacion.AxxionSystem.questions.Categorias.LaCategoriaExiste;
import co.com.Automatizacion.AxxionSystem.tasks.Categorias.BorrarCategoria;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.screenplay.GivenWhenThen;
import net.serenitybdd.screenplay.actors.OnStage;
import java.util.List;
import static org.hamcrest.Matchers.is;

public class BorrarCategoriaStepDefinition {
    @Cuando("el usuario elimina la categoría por su nombre")
    public void elUsuarioEliminaLaCategoríaPorSuNombre(List<Categoria> categoria) {
        OnStage.theActorInTheSpotlight().attemptsTo(
                BorrarCategoria.delete(categoria)
        );
    }
    @Entonces("que la categoría {string} ya no debería existir en el sistema")
    public void queLaCategoríaYaNoDeberíaExistirEnElSistema(String nombreCategoria) {
        OnStage.theActorInTheSpotlight().should(
                GivenWhenThen.seeThat(
                        "La visibilidad de la categoria" + nombreCategoria,
                        LaCategoriaExiste.enLaLista(nombreCategoria),
                        is(false)
                )
        );
    }
}
