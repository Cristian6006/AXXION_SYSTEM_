package co.com.AutomatizacionAxxionSystem.questions;

import co.com.AutomatizacionAxxionSystem.interactions.EsperarElemento;
import co.com.AutomatizacionAxxionSystem.interactions.EsperarListadoEstable;
import co.com.AutomatizacionAxxionSystem.tasks.BuscarEquipoEnInventario;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.questions.Text;

import static co.com.AutomatizacionAxxionSystem.userinterfaces.InventarioUI.*;

/**
 * Verifica alerta de éxito, cierre del modal y equipo en primera posición del listado.
 */
public class ValidacionRegistroEquipoExitoso implements Question<Boolean> {

    private static final int MAX_REINTENTOS = 6;
    private static final long PAUSA_MS = 1000;

    private final String nombreEquipo;

    public ValidacionRegistroEquipoExitoso(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public static ValidacionRegistroEquipoExitoso paraEquipo(String nombreEquipo) {
        return new ValidacionRegistroEquipoExitoso(nombreEquipo);
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            actor.attemptsTo(
                    EsperarElemento.seaVisible(ALERTA_EXITO_EQUIPO_AGREGADO),
                    Click.on(BTN_ACTUALIZAR_VISTA),
                    EsperarListadoEstable.enInventario(),
                    BuscarEquipoEnInventario.conCriterio(nombreEquipo)
            );

            String alerta = Text.of(ALERTA_EXITO_EQUIPO_AGREGADO).answeredBy(actor);

            boolean modalCerrado;
            try {
                modalCerrado = !MODAL_TITULO.resolveFor(actor).isDisplayed();
            } catch (Exception ex) {
                modalCerrado = true;
            }

            boolean equipoVisible = !Text.of(EQUIPO_POR_NOMBRE.of(nombreEquipo))
                    .answeredBy(actor).isBlank();
            boolean enPrimeraPosicion = verificarPrimeraPosicion(actor);

            return modalCerrado
                    && alerta != null
                    && alerta.contains("Equipo Agregado")
                    && equipoVisible
                    && enPrimeraPosicion;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Tras filtrar por nombre, el equipo debe aparecer en la primera tarjeta visible.
     */
    private boolean verificarPrimeraPosicion(Actor actor) {
        for (int intento = 0; intento < MAX_REINTENTOS; intento++) {
            try {
                actor.attemptsTo(EsperarElemento.seaVisible(PRIMER_EQUIPO_EN_LISTA));
                String primerEquipo = Text.of(PRIMER_EQUIPO_EN_LISTA).answeredBy(actor).trim();
                if (nombreEquipo.equals(primerEquipo)) {
                    return true;
                }
            } catch (Exception ignored) {
                // Reintentar tras la recarga progresiva del frontend
            }
            try {
                Thread.sleep(PAUSA_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return false;
    }
}
