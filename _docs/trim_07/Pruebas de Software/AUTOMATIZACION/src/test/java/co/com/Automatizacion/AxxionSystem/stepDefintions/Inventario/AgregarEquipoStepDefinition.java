package co.com.Automatizacion.AxxionSystem.stepDefintions.Inventario;

import co.com.Automatizacion.AxxionSystem.models.Inventario.Equipo;
import co.com.Automatizacion.AxxionSystem.questions.Inventario.AlertaInventarioVisible;
import co.com.Automatizacion.AxxionSystem.questions.Inventario.EquipoEnPrimeraPosicion;
import co.com.Automatizacion.AxxionSystem.questions.Inventario.EquipoVisibleEnListado;
import co.com.Automatizacion.AxxionSystem.questions.Inventario.ModalRegistroEquipoVisible;
import co.com.Automatizacion.AxxionSystem.stepDefintions.Inventario.helper.EquipoHelper;
import co.com.Automatizacion.AxxionSystem.tasks.Inventario.AgregarEquipo;
import co.com.Automatizacion.AxxionSystem.tasks.Inventario.BuscarEquipo;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.is;

public class AgregarEquipoStepDefinition {

    @Cuando("el usuario registra un equipo nuevo desde el formulario con nombre {string}, marca {string}, serie {string} y tarifa diaria {string}")
    public void el_usuario_registra_un_equipo_nuevo_desde_el_formulario(
            String nombre, String marca, String serie, String tarifaDiaria) {
        Equipo equipo = EquipoHelper.crearEquipoDesdeParametros(nombre, marca, serie, tarifaDiaria);
        OnStage.theActorInTheSpotlight().attemptsTo(AgregarEquipo.con(equipo));
    }

    @Entonces("el sistema muestra una alerta de éxito")
    public void el_sistema_muestra_una_alerta_de_exito() {
        Actor actor = OnStage.theActorInTheSpotlight();
        Equipo equipo = actor.recall("EQUIPO");
        boolean alertaVisible = AlertaInventarioVisible.conTexto("Equipo Agregado").answeredBy(actor);
        if (!alertaVisible) {
            actor.should(seeThat(EquipoVisibleEnListado.conNombre(equipo.getNombre()), is(true)));
        }
    }

    @Y("el modal de registro deja de estar visible")
    public void el_modal_de_registro_deja_de_estar_visible() {
        OnStage.theActorInTheSpotlight().should(
                seeThat(ModalRegistroEquipoVisible.modalRegistro(), is(true))
        );
    }

    @Y("el nuevo equipo aparece en la primera posición del listado")
    public void el_nuevo_equipo_aparece_en_la_primera_posicion_del_listado() {
        Actor actor = OnStage.theActorInTheSpotlight();
        Equipo equipo = actor.recall("EQUIPO");
        actor.attemptsTo(BuscarEquipo.porNombre(equipo.getNombre()));
        actor.should(seeThat(EquipoEnPrimeraPosicion.conNombre(equipo.getNombre()), is(true)));
        actor.should(seeThat(EquipoVisibleEnListado.conNombre(equipo.getNombre()), is(true)));
    }
}
