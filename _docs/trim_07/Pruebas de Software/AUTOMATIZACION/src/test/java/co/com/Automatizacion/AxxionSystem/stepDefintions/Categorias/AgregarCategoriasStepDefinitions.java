package co.com.Automatizacion.AxxionSystem.stepDefintions.Categorias;

import co.com.Automatizacion.AxxionSystem.models.Categoria;
import co.com.Automatizacion.AxxionSystem.tasks.Categorias.AgregarCategoria;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.screenplay.actors.OnStage;

import java.util.List;

public class AgregarCategoriasStepDefinitions {
    @Cuando("el usuario crea una nueva categoría")
    public void elUsuarioCreaUnaNuevaCategoría(List<Categoria> categoria) {
        OnStage.theActorInTheSpotlight().attemptsTo(
                AgregarCategoria.add(categoria)
        );
    }
    @Entonces("la categoría {string} debería estar visible en la lista de categorías")
    public void laCategoríaDeberíaEstarVisibleEnLaListaDeCategorías(String string) {
        // Write code here that turns the phrase above into concrete actions
        // throw new io.cucumber.java.PendingException();
    }
}
