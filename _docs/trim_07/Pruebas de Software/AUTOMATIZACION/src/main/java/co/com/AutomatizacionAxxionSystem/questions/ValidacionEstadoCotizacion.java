package co.com.AutomatizacionAxxionSystem.questions;

import co.com.AutomatizacionAxxionSystem.interactions.EsperarElemento;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.questions.Text;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static co.com.AutomatizacionAxxionSystem.userinterfaces.CotizacionUI.ESTADO_COTIZACION_ESPECIFICO;
import static co.com.AutomatizacionAxxionSystem.userinterfaces.CotizacionUI.TITULO_DETALLE_COTIZACION;

/**
 * Verifica el estado de la cotización en la vista de detalle.
 */
public class ValidacionEstadoCotizacion implements Question<Boolean> {

    private final String estadoEsperado;

    public ValidacionEstadoCotizacion(String estadoEsperado) {
        this.estadoEsperado = estadoEsperado;
    }

    public static ValidacionEstadoCotizacion es(String estadoEsperado) {
        return new ValidacionEstadoCotizacion(estadoEsperado);
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            var driver = actor.usingAbilityTo(BrowseTheWeb.class).getDriver();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.urlContains("/quotation/"));

            actor.attemptsTo(
                    EsperarElemento.seaVisible(TITULO_DETALLE_COTIZACION, Duration.ofSeconds(30)),
                    EsperarElemento.seaVisible(ESTADO_COTIZACION_ESPECIFICO.of(estadoEsperado), Duration.ofSeconds(15))
            );

            String estado = Text.of(ESTADO_COTIZACION_ESPECIFICO.of(estadoEsperado))
                    .answeredBy(actor).trim();
            return estadoEsperado.equalsIgnoreCase(estado);
        } catch (Exception e) {
            return false;
        }
    }
}
