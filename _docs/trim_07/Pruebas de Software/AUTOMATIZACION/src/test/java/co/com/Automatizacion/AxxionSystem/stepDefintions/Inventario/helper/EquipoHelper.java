package co.com.Automatizacion.AxxionSystem.stepDefintions.Inventario.helper;

import co.com.Automatizacion.AxxionSystem.factory.Inventario.EquipoFactory;
import co.com.Automatizacion.AxxionSystem.models.Inventario.Equipo;
import co.com.Automatizacion.AxxionSystem.questions.Inventario.EquipoVisibleEnListado;
import co.com.Automatizacion.AxxionSystem.tasks.Inventario.AgregarEquipo;
import net.serenitybdd.screenplay.actors.OnStage;

public class EquipoHelper {
    private EquipoHelper() {}

    public static Equipo crearEquipoParaEliminar() {
        Equipo equipo = EquipoFactory.equipoParaEliminar();
        OnStage.theActorInTheSpotlight().attemptsTo(AgregarEquipo.con(equipo));
        OnStage.theActorInTheSpotlight().remember("EQUIPO", equipo);
        return equipo;
    }

    public static Equipo crearEquipoDesdeParametros(String nombre, String marca, String serie, String tarifa) {
        Equipo equipo = EquipoFactory.desdeParametros(nombre, marca, serie, tarifa);
        OnStage.theActorInTheSpotlight().remember("EQUIPO", equipo);
        return equipo;
    }

    public static boolean existeEquipoEnListado(String nombre) {
        return EquipoVisibleEnListado.conNombre(nombre)
                .answeredBy(OnStage.theActorInTheSpotlight());
    }
}
