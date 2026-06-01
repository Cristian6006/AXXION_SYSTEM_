package co.com.Automatizacion.AxxionSystem.stepDefintions.Categorias;

import co.com.Automatizacion.AxxionSystem.models.Categoria;
import co.com.Automatizacion.AxxionSystem.tasks.Categorias.BorrarCategoria;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.screenplay.actors.OnStage;

import java.util.List;

public class BorrarCategoriaStepDefinition {
    @Cuando("el usuario elimina la categoría por su nombre")
    public void elUsuarioEliminaLaCategoríaPorSuNombre(List<Categoria> categoria) {
        OnStage.theActorInTheSpotlight().attemptsTo(
                BorrarCategoria.delete(categoria)
        );
    }
    @Entonces("que la categoría {string} ya no debería existir en el sistema")
    public void queLaCategoríaYaNoDeberíaExistirEnElSistema(String string) {
        // Write code here that turns the phrase above into concrete actions
        // throw new io.cucumber.java.PendingException();
    }
}
