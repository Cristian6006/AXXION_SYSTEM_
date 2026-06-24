package co.com.Automatizacion.AxxionSystem.stepDefintions.Autenticacion;

import co.com.Automatizacion.AxxionSystem.questions.Autenticacion.TituloDashboard;
import co.com.Automatizacion.AxxionSystem.tasks.Autenticacion.IniciarSesion;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.screenplay.actors.OnStage;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;

public class AutenticacionStepDefinition {
    @Cuando("el administrador inicie sesion con las credenciales correctas")
    public void el_administrador_inicie_sesion_con_las_credenciales_correctas() {
        OnStage.theActorInTheSpotlight().attemptsTo(
                IniciarSesion.comoAdministrador()
        );
    }
    @Entonces("deberia ser redirigido a la pagina principal de Axxion System")
    public void deberia_ser_redirigido_a_la_pagina_principal_de_axxion_system() {
        OnStage.theActorInTheSpotlight().should(
                seeThat(TituloDashboard.tituloDashboard())
        );
    }
}
