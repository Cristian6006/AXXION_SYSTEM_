package co.com.Automatizacion.AxxionSystem.stepDefintions.Usuarios;

import co.com.Automatizacion.AxxionSystem.models.Navegacion.Pagina;
import co.com.Automatizacion.AxxionSystem.tasks.Navegacion.Navegar;
import io.cucumber.java.es.Dado;
import net.serenitybdd.screenplay.actors.OnStage;

public class UsuariosStepDefinition {
    @Dado("que se encuentre en la pagina de gestion gestion de usuarios")
    public void que_se_encuentre_en_la_pagina_de_gestion_gestion_de_usuarios() {
        OnStage.theActorInTheSpotlight().attemptsTo(
                Navegar.a(Pagina.USUARIOS)
        );
    }
}
