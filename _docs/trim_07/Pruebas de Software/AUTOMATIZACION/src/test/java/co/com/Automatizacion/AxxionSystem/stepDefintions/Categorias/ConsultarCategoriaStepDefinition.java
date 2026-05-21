package co.com.Automatizacion.AxxionSystem.stepDefintions.Categorias;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;

public class ConsultarCategoriaStepDefinition {
    // FILTRO POR BUSQUEDA

    @Cuando("el usuario ingresa {string} en el campo de búsqueda")
    public void elUsuarioIngresaEnElCampoDeBúsqueda(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    @Entonces("debería ver unicamente resultados que contengan {string} en la lista")
    public void deberíaVerUnicamenteResultadosQueContenganEnLaLista(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    // FILTRO POR FECHA

    @Cuando("el usuario selecciona la fecha {string} en el filtro de fechas")
    public void elUsuarioSeleccionaLaFechaEnElFiltroDeFechas(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    @Entonces("debería ver solo las categorias creadas en {string}")
    public void deberíaVerSoloLasCategoriasCreadasEn(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
}
