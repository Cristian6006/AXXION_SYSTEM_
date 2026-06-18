package co.com.AutomatizacionAxxionSystem.questions;

import co.com.AutomatizacionAxxionSystem.interactions.EsperarElemento;
import co.com.AutomatizacionAxxionSystem.models.SesionVariable;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static co.com.AutomatizacionAxxionSystem.userinterfaces.RentaUI.REFERENCIA_COTIZACION;
import static co.com.AutomatizacionAxxionSystem.userinterfaces.RentaUI.RENTA_CON_ESTADO_Y_REFERENCIA;
import static co.com.AutomatizacionAxxionSystem.userinterfaces.RentaUI.TARJETAS_RENTA;

/**
 * Verifica la renta generada desde la última cotización del escenario.
 */
public class ValidacionRentaGenerada implements Question<Boolean> {

    private final String estadoEsperado;
    private final boolean validarReferencia;

    public ValidacionRentaGenerada(String estadoEsperado, boolean validarReferencia) {
        this.estadoEsperado = estadoEsperado;
        this.validarReferencia = validarReferencia;
    }

    public static ValidacionRentaGenerada conEstado(String estadoEsperado) {
        return new ValidacionRentaGenerada(estadoEsperado, false);
    }

    public static ValidacionRentaGenerada conReferenciaCotizacion() {
        return new ValidacionRentaGenerada("Programada", true);
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            String cotizacionId = actor.recall(SesionVariable.ultimaCotizacionId.toString());
            if (cotizacionId == null || cotizacionId.isBlank()) {
                return false;
            }

            WebDriverWait wait = new WebDriverWait(
                    actor.usingAbilityTo(BrowseTheWeb.class).getDriver(),
                    Duration.ofSeconds(30));
            wait.until(ExpectedConditions.urlContains("/Rental"));

            actor.attemptsTo(
                    EsperarElemento.seaVisible(TARJETAS_RENTA, Duration.ofSeconds(30))
            );

            if (validarReferencia) {
                actor.attemptsTo(
                        EsperarElemento.seaVisible(REFERENCIA_COTIZACION.of(cotizacionId), Duration.ofSeconds(30))
                );
                return true;
            }

            actor.attemptsTo(
                    EsperarElemento.seaVisible(
                            RENTA_CON_ESTADO_Y_REFERENCIA.of(estadoEsperado, cotizacionId),
                            Duration.ofSeconds(30))
            );
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
