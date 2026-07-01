package co.com.Automatizacion.AxxionSystem.stepDefintions.Inventario;

import co.com.Automatizacion.AxxionSystem.models.Inventario.Equipo;
import co.com.Automatizacion.AxxionSystem.questions.Inventario.AlertaInventarioVisible;
import co.com.Automatizacion.AxxionSystem.questions.Inventario.EquipoVisibleEnListado;
import co.com.Automatizacion.AxxionSystem.tasks.Inventario.BuscarEquipo;
import co.com.Automatizacion.AxxionSystem.tasks.Inventario.EliminarEquipo;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.is;

public class EliminarEquipoStepDefinition {

    @Cuando("el usuario elimina el equipo de prueba desde el listado")
    public void el_usuario_elimina_el_equipo_de_prueba_desde_el_listado() {
        Actor actor = OnStage.theActorInTheSpotlight();
        Equipo equipo = actor.recall("EQUIPO");
        actor.attemptsTo(EliminarEquipo.con(equipo));
    }

    @Entonces("el sistema muestra una alerta de éxito de eliminación")
    public void el_sistema_muestra_una_alerta_de_exito_de_eliminacion() {
        Actor actor = OnStage.theActorInTheSpotlight();
        boolean alertaVisible = AlertaInventarioVisible.conTexto("Producto Eliminado").answeredBy(actor);
        if (!alertaVisible) {
            Equipo equipo = actor.recall("EQUIPO");
            String criterio = BuscarEquipo.normalizarCriterioBusqueda(equipo.getNombre());
            actor.attemptsTo(BuscarEquipo.porNombre(equipo.getNombre()));
            actor.should(seeThat(EquipoVisibleEnListado.conNombre(criterio), is(false)));
        }
    }

    @Y("el equipo de prueba ya no aparece en el listado")
    public void el_equipo_de_prueba_ya_no_aparece_en_el_listado() {
        Actor actor = OnStage.theActorInTheSpotlight();
        Equipo equipo = actor.recall("EQUIPO");
        String criterio = BuscarEquipo.normalizarCriterioBusqueda(equipo.getNombre());
        actor.attemptsTo(BuscarEquipo.porNombre(equipo.getNombre()));
        actor.should(seeThat(EquipoVisibleEnListado.conNombre(criterio), is(false)));
    }
}
