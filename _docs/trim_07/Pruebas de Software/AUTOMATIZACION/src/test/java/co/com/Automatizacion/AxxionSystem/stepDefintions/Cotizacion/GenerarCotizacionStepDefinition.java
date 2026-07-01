package co.com.Automatizacion.AxxionSystem.stepDefintions.Cotizacion;

import co.com.Automatizacion.AxxionSystem.factory.Inventario.EquipoFactory;
import co.com.Automatizacion.AxxionSystem.models.Cotizacion.SolicitudCotizacion;
import co.com.Automatizacion.AxxionSystem.models.Inventario.Equipo;
import co.com.Automatizacion.AxxionSystem.questions.Cotizacion.CotizacionDetalleVisible;
import co.com.Automatizacion.AxxionSystem.questions.Cotizacion.DesgloseCostosCotizacion;
import co.com.Automatizacion.AxxionSystem.tasks.Cotizacion.AgregarEquipoAlCarrito;
import co.com.Automatizacion.AxxionSystem.tasks.Cotizacion.RegistrarSolicitudCotizacion;
import co.com.Automatizacion.AxxionSystem.tasks.Inventario.AgregarEquipo;
import co.com.Automatizacion.AxxionSystem.tasks.Inventario.BuscarEquipo;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.is;

public class GenerarCotizacionStepDefinition {

    @Y("el usuario registra un equipo disponible para cotización")
    public void el_usuario_registra_un_equipo_disponible_para_cotizacion() {
        Equipo equipo = EquipoFactory.equipoParaCotizacion();
        OnStage.theActorInTheSpotlight().attemptsTo(AgregarEquipo.con(equipo));
        OnStage.theActorInTheSpotlight().remember("EQUIPO_COTIZACION", equipo.getNombre());
    }

    @Cuando("el usuario agrega el equipo registrado al carrito de cotización")
    public void el_usuario_agrega_el_equipo_registrado_al_carrito_de_cotizacion() {
        String equipo = OnStage.theActorInTheSpotlight().recall("EQUIPO_COTIZACION");
        OnStage.theActorInTheSpotlight().attemptsTo(AgregarEquipoAlCarrito.conNombre(equipo));
    }

    @Cuando("el usuario agrega el equipo {string} al carrito de cotización")
    public void el_usuario_agrega_el_equipo_al_carrito_de_cotizacion(String equipo) {
        OnStage.theActorInTheSpotlight().attemptsTo(AgregarEquipoAlCarrito.conNombre(equipo));
    }

    @Y("registra la solicitud para el cliente {string} entre las fechas {string} y {string}")
    public void registra_la_solicitud_para_el_cliente_entre_las_fechas(
            String cliente, String fechaInicio, String fechaFin) {
        SolicitudCotizacion solicitud = SolicitudCotizacion.builder()
                .cliente(cliente)
                .equipo(OnStage.theActorInTheSpotlight().recall("EQUIPO_COTIZACION"))
                .fechaInicio(fechaInicio)
                .fechaFin(fechaFin)
                .build();

        OnStage.theActorInTheSpotlight().attemptsTo(RegistrarSolicitudCotizacion.con(solicitud));
    }

    @Entonces("el sistema genera la cotización con estado {string}")
    public void el_sistema_genera_la_cotizacion_con_estado(String estado) {
        OnStage.theActorInTheSpotlight().should(
                seeThat(CotizacionDetalleVisible.conEstado(estado), is(true))
        );
    }

    @Y("el usuario visualiza un desglose de costos acorde al número de días del periodo seleccionado")
    public void el_usuario_visualiza_un_desglose_de_costos() {
        Actor actor = OnStage.theActorInTheSpotlight();
        String equipo = actor.recall("EQUIPO_COTIZACION");
        String criterio = co.com.Automatizacion.AxxionSystem.tasks.Inventario.BuscarEquipo.normalizarCriterioBusqueda(equipo);
        actor.should(seeThat(DesgloseCostosCotizacion.paraEquipo(criterio), is(true)));
    }
}
