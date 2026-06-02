package co.com.Automatizacion.AxxionSystem.stepDefintions.Categorias;

import co.com.Automatizacion.AxxionSystem.questions.Categorias.MensajeGuardado;
import co.com.Automatizacion.AxxionSystem.tasks.NavegarA;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.screenplay.actors.OnStage;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;

public class CategoriasStepDefinition {
    @Dado("que se muestra la pagina de gestión de categorías")
    public void queSeMuestraLaPaginaDeGestiónDeCategorías() {
        OnStage.theActorInTheSpotlight().attemptsTo(
                NavegarA.laPaginaCategorias()
        );
    }
    @Entonces("debería ver un mensaje de éxito {string}")
    public void deberíaVerUnMensajeDeÉxito(String guardado) {
        OnStage.theActorInTheSpotlight()
                .should(seeThat(MensajeGuardado.mensajeGuardado()));
    }
}
