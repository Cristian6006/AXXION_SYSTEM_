package co.com.Automatizacion.AxxionSystem.stepDefintions.Categorias;

import co.com.Automatizacion.AxxionSystem.models.Navegacion.Pagina;
import co.com.Automatizacion.AxxionSystem.questions.Categorias.MensajeExito;
import co.com.Automatizacion.AxxionSystem.tasks.Navegacion.Navegar;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.screenplay.actors.OnStage;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;

public class CategoriasStepDefinition {
    @Dado("se encuentre en la pagina de gestion gestion de categorias")
    public void se_encuentre_en_la_pagina_de_gestion_gestion_de_categorias() {
        OnStage.theActorInTheSpotlight().attemptsTo(
                Navegar.a(Pagina.CATEGORIAS)
        );
    }
    @Entonces("debería ver un mensaje de éxito")
    public void debería_ver_un_mensaje_de_éxito() {
        OnStage.theActorInTheSpotlight()
                .should(seeThat(MensajeExito.mensajeExito()));
    }
}
