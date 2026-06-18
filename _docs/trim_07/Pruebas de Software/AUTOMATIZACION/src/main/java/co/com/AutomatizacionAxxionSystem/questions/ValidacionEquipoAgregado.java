package co.com.AutomatizacionAxxionSystem.questions;

import co.com.AutomatizacionAxxionSystem.interactions.EsperarElemento;
import co.com.AutomatizacionAxxionSystem.interactions.EsperarListadoEstable;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;

import static co.com.AutomatizacionAxxionSystem.userinterfaces.InventarioUI.ALERTA_EXITO_EQUIPO_AGREGADO;
import static co.com.AutomatizacionAxxionSystem.userinterfaces.InventarioUI.EQUIPO_POR_NOMBRE;

/**
 * Verifica mensaje de éxito y presencia del equipo en el listado.
 */
public class ValidacionEquipoAgregado implements Question<Boolean> {

    private final String nombreEquipo;
    private final String mensajeExito;

    public ValidacionEquipoAgregado(String nombreEquipo, String mensajeExito) {
        this.nombreEquipo = nombreEquipo;
        this.mensajeExito = mensajeExito;
    }

    public static ValidacionEquipoAgregado con(String nombreEquipo, String mensajeExito) {
        return new ValidacionEquipoAgregado(nombreEquipo, mensajeExito);
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            actor.attemptsTo(
                    EsperarElemento.seaVisible(ALERTA_EXITO_EQUIPO_AGREGADO),
                    EsperarListadoEstable.enInventario(),
                    EsperarElemento.seaVisible(EQUIPO_POR_NOMBRE.of(nombreEquipo))
            );

            String alerta = Text.of(ALERTA_EXITO_EQUIPO_AGREGADO).answeredBy(actor);
            boolean mensajeOk = alerta != null && alerta.contains(mensajeExito);
            boolean equipoVisible = !Text.of(EQUIPO_POR_NOMBRE.of(nombreEquipo))
                    .answeredBy(actor).isBlank();

            return mensajeOk && equipoVisible;
        } catch (Exception e) {
            return false;
        }
    }
}
