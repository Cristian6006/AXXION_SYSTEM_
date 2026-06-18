package co.com.AutomatizacionAxxionSystem.stepdefinitions;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import co.com.AutomatizacionAxxionSystem.models.DatosCotizacion;
import co.com.AutomatizacionAxxionSystem.questions.ValidacionEstadoCotizacion;
import co.com.AutomatizacionAxxionSystem.tasks.GenerarCotizacionDesdeInventario;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class CotizacionStepDefinitions {

    @Cuando("el usuario genera una cotizacion para el cliente {string} con el equipo {string} entre las fechas {string} y {string}")
    public void elUsuarioGeneraUnaCotizacionParaElClienteConElEquipoEntreLasFechas(
            String cliente,
            String equipo,
            String fechaInicio,
            String fechaFin) {
        DatosCotizacion datosCotizacion = new DatosCotizacion(cliente, equipo, fechaInicio, fechaFin);
        theActorInTheSpotlight().attemptsTo(GenerarCotizacionDesdeInventario.con(datosCotizacion));
    }

    @Entonces("el sistema genera la cotización con estado {string}")
    public void elSistemaGeneraLaCotizacionConEstado(String estado) {
        theActorInTheSpotlight().should(seeThat(ValidacionEstadoCotizacion.es(estado)));
    }
}
