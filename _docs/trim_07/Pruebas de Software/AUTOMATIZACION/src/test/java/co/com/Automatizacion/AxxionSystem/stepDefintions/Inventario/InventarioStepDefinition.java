package co.com.Automatizacion.AxxionSystem.stepDefintions.Inventario;

import co.com.Automatizacion.AxxionSystem.models.Navegacion.Pagina;
import co.com.Automatizacion.AxxionSystem.questions.Autenticacion.TituloDashboard;
import co.com.Automatizacion.AxxionSystem.questions.Inventario.EquipoVisibleEnListado;
import co.com.Automatizacion.AxxionSystem.questions.Inventario.PermisoCrearEquipos;
import co.com.Automatizacion.AxxionSystem.stepDefintions.Inventario.helper.EquipoHelper;
import co.com.Automatizacion.AxxionSystem.tasks.Autenticacion.IniciarSesion;
import co.com.Automatizacion.AxxionSystem.tasks.Inventario.BuscarEquipo;
import co.com.Automatizacion.AxxionSystem.tasks.Navegacion.Navegar;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Y;
import net.serenitybdd.screenplay.actors.OnStage;

import java.util.List;
import java.util.Map;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.is;

public class InventarioStepDefinition {

    @Dado("que el administrador cuenta con sesión iniciada en AXXION SYSTEM")
    public void que_el_administrador_cuenta_con_sesion_iniciada_en_axxion_system() {
        OnStage.theActorInTheSpotlight().attemptsTo(IniciarSesion.comoAdministrador());
        OnStage.theActorInTheSpotlight().should(seeThat(TituloDashboard.tituloDashboard()));
    }

    @Dado("que el usuario cuenta con sesión iniciada en AXXION SYSTEM")
    public void que_el_usuario_cuenta_con_sesion_iniciada_en_axxion_system() {
        que_el_administrador_cuenta_con_sesion_iniciada_en_axxion_system();
    }

    @Dado("el administrador está en el módulo de inventario")
    @Y("el usuario está en el módulo de inventario")
    public void el_usuario_esta_en_el_modulo_de_inventario() {
        OnStage.theActorInTheSpotlight().attemptsTo(Navegar.a(Pagina.INVENTARIO));
    }

    @Y("el usuario cuenta con permisos para crear equipos")
    public void el_usuario_cuenta_con_permisos_para_crear_equipos() {
        OnStage.theActorInTheSpotlight().should(seeThat(PermisoCrearEquipos.disponible(), is(true)));
    }

    @Y("el inventario incluye los equipos con el estado siguiente")
    public void el_inventario_incluye_los_equipos_con_el_estado_siguiente(DataTable dataTable) {
        List<Map<String, String>> equipos = dataTable.asMaps();
        for (Map<String, String> fila : equipos) {
            String nombre = fila.get("equipo");
            String criterio = BuscarEquipo.normalizarCriterioBusqueda(nombre);
            OnStage.theActorInTheSpotlight().attemptsTo(BuscarEquipo.porNombre(nombre));
            OnStage.theActorInTheSpotlight().should(
                    seeThat("El equipo " + nombre + " debe existir en inventario",
                            EquipoVisibleEnListado.conNombre(criterio), is(true))
            );
        }
    }

    @Y("el usuario registra un equipo disponible para pruebas de eliminación")
    public void el_usuario_registra_un_equipo_disponible_para_pruebas_de_eliminacion() {
        EquipoHelper.crearEquipoParaEliminar();
    }
}
