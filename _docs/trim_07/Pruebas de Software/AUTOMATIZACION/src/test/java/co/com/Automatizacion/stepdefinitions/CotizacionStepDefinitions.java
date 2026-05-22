package co.com.Automatizacion.stepdefinitions;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;

public class CotizacionStepDefinitions {

    @Cuando("el usuario genera una cotizacion para el cliente {string} con el equipo {string} entre las fechas {string} y {string}")
    public void elUsuarioGeneraUnaCotizacionParaElClienteConElEquipoEntreLasFechas(
            String cliente,
            String equipo,
            String fechaInicio,
            String fechaFin) {
    }

    @Entonces("el sistema genera la cotización con estado {string}")
    public void elSistemaGeneraLaCotizacionConEstado(String estado) {
    }

    @Entonces("el usuario visualiza un desglose de costos acorde al número de días del periodo seleccionado")
    public void elUsuarioVisualizaUnDesgloseDeCostosAcordeAlNumeroDeDiasDelPeriodoSeleccionado() {
    }
}
