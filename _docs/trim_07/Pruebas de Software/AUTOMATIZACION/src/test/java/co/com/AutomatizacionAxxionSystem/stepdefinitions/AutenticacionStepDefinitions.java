package co.com.AutomatizacionAxxionSystem.stepdefinitions;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import co.com.AutomatizacionAxxionSystem.models.CredencialesInicioSesion;
import co.com.AutomatizacionAxxionSystem.tasks.AbrirPagina;
import co.com.AutomatizacionAxxionSystem.tasks.Autenticarse;
import co.com.AutomatizacionAxxionSystem.questions.ValidacionLogin;
import java.util.List;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class AutenticacionStepDefinitions {

    @Dado("que el usuario se encuentra en la pagina de inicio de sesion")
    public void queElUsuarioSeEncuentraEnLaPaginaDeInicioDeSesion() {
        theActorInTheSpotlight().wasAbleTo(AbrirPagina.laPagina());
    }

    @Cuando("Ingrese las credenciales correctas (usuario y contrasena)")
    public void ingreseLasCredenciales(List<CredencialesInicioSesion> credenciales) {
        theActorInTheSpotlight().attemptsTo(Autenticarse.aute(credenciales));
    }

    @Entonces("se debe verificar que el usuario haya sido autenticado correctamente...")
    public void seDebeVerificarQueElUsuarioHayaSidoAutenticado() {
        theActorInTheSpotlight().should(seeThat(ValidacionLogin.validacionLogin()));
    }
}
