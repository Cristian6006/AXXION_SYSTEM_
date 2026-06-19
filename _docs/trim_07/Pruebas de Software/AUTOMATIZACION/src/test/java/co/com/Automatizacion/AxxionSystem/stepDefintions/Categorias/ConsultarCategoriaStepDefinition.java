package co.com.Automatizacion.AxxionSystem.stepDefintions.Categorias;

import co.com.Automatizacion.AxxionSystem.models.Categoria;
import co.com.Automatizacion.AxxionSystem.questions.Categorias.CategoriaConsultada;
import co.com.Automatizacion.AxxionSystem.tasks.Categorias.ConsultarCategoriaNombre;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.screenplay.GivenWhenThen;
import net.serenitybdd.screenplay.actors.OnStage;

import java.util.List;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.CoreMatchers.containsString;

public class ConsultarCategoriaStepDefinition {
    // FILTRO POR BUSQUEDA

    @Cuando("el usuario ingresa el nombre de la categoria buscada")
    public void elUsuarioIngresaElNombreDeLaCategoria(List<Categoria> categoria) {
        OnStage.theActorInTheSpotlight()
                .attemptsTo(ConsultarCategoriaNombre.search(categoria)
                );
    }
    @Entonces("debería ver unicamente resultados que contengan {string}")
    public void deberíaVerUnicamenteResultadosQueContengan(String palabraBuscada) {
        OnStage.theActorInTheSpotlight().should(
                GivenWhenThen.seeThat(
                        "El texto de la categoría encontrada",
                        CategoriaConsultada.categoriaConsultada(),
                        containsString(palabraBuscada)
                )
        );
    }

    // FILTRO POR FECHA

    @Cuando("el usuario selecciona la fecha en el filtro de fechas")
    public void elUsuarioSeleccionaLaFechaEnElFiltroDeFechas(List<Categoria> categoria) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    @Entonces("debería ver solo las categorias creadas en la fecha seleccionada")
    public void deberíaVerSoloLasCategoriasCreadasEnLaFechaSeleccionada() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
}
