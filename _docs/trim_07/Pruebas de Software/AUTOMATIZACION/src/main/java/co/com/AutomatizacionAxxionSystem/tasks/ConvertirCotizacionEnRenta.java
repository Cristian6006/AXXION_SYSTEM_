package co.com.AutomatizacionAxxionSystem.tasks;

import co.com.AutomatizacionAxxionSystem.interactions.EsperarElemento;
import co.com.AutomatizacionAxxionSystem.models.SesionVariable;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.Click;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static co.com.AutomatizacionAxxionSystem.userinterfaces.CotizacionUI.ALERTA_RENTA_CREADA;
import static co.com.AutomatizacionAxxionSystem.userinterfaces.CotizacionUI.BTN_CONVERTIR_A_RENTA;

/**
 * Task: convierte la cotización abierta en una renta programada.
 */
public class ConvertirCotizacionEnRenta implements Task {

    public static ConvertirCotizacionEnRenta desdeElDetalle() {
        return Tasks.instrumented(ConvertirCotizacionEnRenta.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        recordarCotizacionActual(actor);

        actor.attemptsTo(
                EsperarElemento.seaClickeable(BTN_CONVERTIR_A_RENTA),
                Click.on(BTN_CONVERTIR_A_RENTA),
                EsperarElemento.seaVisible(ALERTA_RENTA_CREADA, Duration.ofSeconds(20))
        );

        WebDriverWait wait = new WebDriverWait(
                actor.usingAbilityTo(BrowseTheWeb.class).getDriver(),
                Duration.ofSeconds(30));
        wait.until(ExpectedConditions.urlContains("/Rental"));
    }

    private <T extends Actor> void recordarCotizacionActual(T actor) {
        String cotizacionId = actor.recall(SesionVariable.ultimaCotizacionId.toString());
        if (cotizacionId != null && !cotizacionId.isBlank()) {
            return;
        }

        String url = actor.usingAbilityTo(BrowseTheWeb.class).getDriver().getCurrentUrl();
        if (url.contains("/quotation/")) {
            actor.remember(SesionVariable.ultimaCotizacionId.toString(),
                    url.substring(url.lastIndexOf('/') + 1));
        }
    }
}
