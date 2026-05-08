package co.com.Automatizacion.AxxionSystem.stepDefintions.Autenticacion;

import co.com.Automatizacion.AxxionSystem.questions.Autenticacion.urlCorrecta;
import co.com.Automatizacion.AxxionSystem.questions.Autenticacion.MensajeBienvenida;
import co.com.Automatizacion.AxxionSystem.tasks.IniciarSesion;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.screenplay.GivenWhenThen;
import net.serenitybdd.screenplay.actions.Open;
import net.serenitybdd.screenplay.actors.OnStage;
import static org.hamcrest.Matchers.containsString;

public class AutenticacionStepDefinitions {

    @Dado("que el usuario se encuentra en la pagina de inicio de sesion de Axxion System")
    public void queElUsuarioSeEncuentraEnLaPaginaDeInicioDeSesionDeAxxionSystem() {
        OnStage.theActorCalled("Admin").wasAbleTo(Open.url("http://localhost:5173/login"));
    }
    @Cuando("ingrese las credenciales correctas {string} y {string}")
    public void ingreseLasCredencialesCorrectasY(String correo, String password, io.cucumber.datatable.DataTable dataTable) {
        OnStage.theActorInTheSpotlight().attemptsTo(IniciarSesion.conCredenciales("p@example.com", "Us123456"));
    }
    @Entonces("se deberia verificar que el usuario haya sido autenticado correctamente")
    public void seDeberiaVerificarQueElUsuarioHayaSidoAutenticadoCorrectamente() {
        OnStage.theActorInTheSpotlight().should(GivenWhenThen.seeThat("Mensaje de bienvenida", MensajeBienvenida.esVisible()));
    }
    @Entonces("redirigido a la pagina prinncipal de Axxion System")
    public void redirigidoALaPaginaPrinncipalDeAxxionSystem() {
        OnStage.theActorInTheSpotlight().should(
                GivenWhenThen.seeThat("La url del navegador",
                        urlCorrecta.actual(),
                        containsString("Home")
                )
        );
    }

    @Dado("que el usuario ha iniciado sesión en la aplicación correctamente")
    public void queElUsuarioHaIniciadoSesiónEnLaAplicaciónCorrectamente() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

}
