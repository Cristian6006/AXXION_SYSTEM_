package co.com.AutomatizacionAxxionSystem.stepdefinitions;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import co.com.AutomatizacionAxxionSystem.models.DatosEquipo;
import co.com.AutomatizacionAxxionSystem.questions.ValidacionBusquedaInventario;
import co.com.AutomatizacionAxxionSystem.questions.ValidacionFiltroInmediato;
import co.com.AutomatizacionAxxionSystem.questions.ValidacionRegistroEquipoExitoso;
import co.com.AutomatizacionAxxionSystem.tasks.AgregarEquipoAlInventario;
import co.com.AutomatizacionAxxionSystem.tasks.BuscarEquipoEnInventario;
import co.com.AutomatizacionAxxionSystem.tasks.NavegarAInventario;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class InventarioStepDefinitions {

    private String criterioBusqueda;

    @Dado("que estoy en el modulo de gestion de inventario")
    public void queEstoyEnElModuloDeGestionDeInventario() {
        theActorInTheSpotlight().attemptsTo(NavegarAInventario.elModulo());
    }

    @Cuando("el usuario registra un equipo nuevo con nombre {string}, marca {string}, modelo {string}, serie {string}, categoria {string} y tarifa diaria {string}")
    public void elUsuarioRegistraUnEquipoNuevoConNombreMarcaModeloSerieCategoriaYTarifaDiaria(
            String nombre, String marca, String modelo, String serie, String categoria, String tarifa) {
        DatosEquipo datosEquipo = new DatosEquipo(nombre, marca, modelo, serie, categoria, tarifa);
        theActorInTheSpotlight().attemptsTo(AgregarEquipoAlInventario.con(datosEquipo));
    }

    @Entonces("el equipo {string} deberia aparecer en la lista de inventario con un mensaje de exito {string}")
    public void elEquipoDeberiaAparecerEnLaListaDeInventarioConUnMensajeDeExito(String nombreEquipo, String mensaje) {
        theActorInTheSpotlight().should(seeThat(ValidacionRegistroEquipoExitoso.paraEquipo(nombreEquipo)));
    }

    @Cuando("el usuario escribe {string} en el campo de búsqueda de equipos")
    public void elUsuarioEscribeEnElCampoDeBusquedaDeEquipos(String criterio) {
        this.criterioBusqueda = criterio;
        theActorInTheSpotlight().attemptsTo(BuscarEquipoEnInventario.conCriterio(criterio));
    }

    @Entonces("el listado muestra únicamente equipos cuyo nombre, marca o número de serie contiene el criterio buscado")
    public void elListadoMuestraUnicamenteEquiposCuyoNombreMarcaONumeroDeSerieContieneElCriterioBuscado() {
        theActorInTheSpotlight().should(seeThat(ValidacionBusquedaInventario.conCriterio(criterioBusqueda)));
    }

    @Y("el listado refleja el filtro de forma inmediata")
    public void elListadoReflejaElFiltroDeFormaInmediata() {
        theActorInTheSpotlight().should(seeThat(ValidacionFiltroInmediato.sinRecargarPagina()));
    }
}
