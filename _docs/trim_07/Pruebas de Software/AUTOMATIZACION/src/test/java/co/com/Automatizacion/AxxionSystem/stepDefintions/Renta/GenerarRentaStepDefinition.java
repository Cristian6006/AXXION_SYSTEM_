package co.com.Automatizacion.AxxionSystem.stepDefintions.Renta;

import co.com.Automatizacion.AxxionSystem.questions.Renta.RentaReferenciaCotizacion;
import co.com.Automatizacion.AxxionSystem.questions.Renta.RentaRegistradaConEstado;
import co.com.Automatizacion.AxxionSystem.questions.Renta.RentaVinculadaAlCliente;
import co.com.Automatizacion.AxxionSystem.stepDefintions.Cotizacion.helper.CotizacionHelper;
import co.com.Automatizacion.AxxionSystem.tasks.Cotizacion.AbrirDetalleCotizacion;
import co.com.Automatizacion.AxxionSystem.tasks.Renta.ConvertirCotizacionEnRenta;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import net.serenitybdd.screenplay.actors.OnStage;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.is;

public class GenerarRentaStepDefinition {

    @Dado("existe una cotización en estado {string} para el cliente {string}")
    public void existe_una_cotizacion_en_estado_para_el_cliente(String estado, String cliente) {
        CotizacionHelper.generarCotizacionBorrador(cliente, "example 1");
    }

    @Y("el administrador visualiza el detalle de esa cotización")
    public void el_administrador_visualiza_el_detalle_de_esa_cotizacion() {
        String cotizacionId = OnStage.theActorInTheSpotlight().recall("COTIZACION_ID");
        OnStage.theActorInTheSpotlight().attemptsTo(AbrirDetalleCotizacion.conId(cotizacionId));
    }

    @Cuando("el usuario convierte la cotización en renta")
    public void el_usuario_convierte_la_cotizacion_en_renta() {
        OnStage.theActorInTheSpotlight().attemptsTo(ConvertirCotizacionEnRenta.ahora());
    }

    @Entonces("el sistema registra una nueva renta con estado {string}")
    public void el_sistema_registra_una_nueva_renta_con_estado(String estado) {
        OnStage.theActorInTheSpotlight().should(
                seeThat(RentaRegistradaConEstado.conEstado(estado), is(true))
        );
    }

    @Y("la renta mantiene la referencia al número de cotización de origen")
    public void la_renta_mantiene_la_referencia_al_numero_de_cotizacion_de_origen() {
        OnStage.theActorInTheSpotlight().should(
                seeThat(RentaReferenciaCotizacion.mantieneReferencia(), is(true))
        );
    }

    @Y("los equipos de la cotización figuran vinculados al cronograma del cliente {string}")
    public void los_equipos_de_la_cotizacion_figuran_vinculados_al_cronograma_del_cliente(String cliente) {
        OnStage.theActorInTheSpotlight().should(
                seeThat(RentaVinculadaAlCliente.paraCliente(cliente), is(true))
        );
    }
}
