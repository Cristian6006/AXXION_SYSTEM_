package co.com.Automatizacion.AxxionSystem.stepDefintions.Autenticacion;

import co.com.Automatizacion.AxxionSystem.pages.LoginPage;
import co.com.Automatizacion.AxxionSystem.pages.PaginaPrincipal;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.annotations.Steps;

import static org.junit.Assert.assertTrue;

public class AutenticacionStepDefinitions {

    @Steps
    LoginPage loginPage;
    PaginaPrincipal paginaPrincipal;

    @Dado("que el usuario se encuentra en la pagina de inicio de sesion de Axxion System")
    public void queElUsuarioSeEncuentraEnLaPaginaDeInicioDeSesionDeAxxionSystem() {
        loginPage.openUrl("http://localhost:5173/login");
    }
    @Cuando("ingrese las credenciales correctas {string} y {string}")
    public void ingreseLasCredencialesCorrectasY(String string, String string2, io.cucumber.datatable.DataTable dataTable) {
        loginPage.iniciarSesion("p@example.com", "Us123456");
    }
    @Entonces("se deberia verificar que el usuario haya sido autenticado correctamente")
    public void seDeberiaVerificarQueElUsuarioHayaSidoAutenticadoCorrectamente() {
        assertTrue("ERROR: No se encontró el mensaje de bienvenida de Axxion System",
                paginaPrincipal.mensajeBienvenidaEsVisible());
    }
    @Entonces("redirigido a la pagina prinncipal de Axxion System")
    public void redirigidoALaPaginaPrinncipalDeAxxionSystem() {
        String urlActual = paginaPrincipal.obtenerUrlActual();
        assertTrue("No se redirigió a la página principal", urlActual.contains("Home"));
    }

}
