package co.com.Automatizacion.AxxionSystem.stepDefintions.Autenticacion;

import co.com.Automatizacion.AxxionSystem.models.Usuario;
import co.com.Automatizacion.AxxionSystem.questions.Autenticacion.UrlActual;
import co.com.Automatizacion.AxxionSystem.questions.Autenticacion.MensajeBienvenida;
import co.com.Automatizacion.AxxionSystem.tasks.IniciarSesion;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.screenplay.actions.Open;
import net.serenitybdd.screenplay.actors.OnStage;

import java.util.List;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.containsString;

public class AutenticacionStepDefinitions {

    @Dado("que el usuario se encuentra en la pagina de inicio de sesion de Axxion System")
    public void queElUsuarioSeEncuentraEnLaPaginaDeInicioDeSesionDeAxxionSystem() {
        OnStage.theActorInTheSpotlight().wasAbleTo(Open.url("http://localhost:5173/login"));
    }
    @Cuando("inicie sesion con las credenciales \\(usuario y contraseña)")
    public void inicieSesionConLasCredencialesUsuarioYContraseña(List<Usuario> credenciales) {
        OnStage.theActorInTheSpotlight().attemptsTo(
                IniciarSesion.aute(credenciales)
        );
    }
    @Entonces("se deberia verificar que el usuario haya sido autenticado correctamente")
    public void seDeberiaVerificarQueElUsuarioHayaSidoAutenticadoCorrectamente() {
        OnStage.theActorInTheSpotlight()
                .should(seeThat(MensajeBienvenida.mensajeBienvenida()));
    }
    @Entonces("redirigido a la pagina principal de Axxion System")
    public void redirigidoALaPaginaPrincipalDeAxxionSystem() {
        OnStage.theActorInTheSpotlight().should(
                seeThat("La url del navegador",
                        UrlActual.actual(),
                        containsString("Home")
                )
        );
    }
}
