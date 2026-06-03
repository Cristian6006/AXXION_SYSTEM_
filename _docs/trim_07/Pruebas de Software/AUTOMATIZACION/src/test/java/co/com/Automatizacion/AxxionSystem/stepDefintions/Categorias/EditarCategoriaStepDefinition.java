package co.com.Automatizacion.AxxionSystem.stepDefintions.Categorias;

import co.com.Automatizacion.AxxionSystem.models.Categoria;
import co.com.Automatizacion.AxxionSystem.questions.Categorias.LaCategoriaEsVisible;
import co.com.Automatizacion.AxxionSystem.tasks.Categorias.EditarCategoria;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.screenplay.GivenWhenThen;
import net.serenitybdd.screenplay.actors.OnStage;

import java.util.List;

public class EditarCategoriaStepDefinition {
    @Cuando("el usuario actualiza el nombre de la categoría por el nuevo nombre")
    public void elUsuarioActualizaElNombreDeLaCategoríaPorElNuevoNombre(List<Categoria> categoria) {
        OnStage.theActorInTheSpotlight().attemptsTo(
                EditarCategoria.edit(categoria)
        );
    }
    @Entonces("que la categoría {string} debería estar visible en la tabla")
    public void queLaCategoríaDeberíaEstarVisibleEnLaTabla(String categoriaBuscada) {
        OnStage.theActorInTheSpotlight().should(
                GivenWhenThen.seeThat(
                        "La Categoria Editada",
                        LaCategoriaEsVisible.enLosResultados(categoriaBuscada)
                )
        );
    }
}
