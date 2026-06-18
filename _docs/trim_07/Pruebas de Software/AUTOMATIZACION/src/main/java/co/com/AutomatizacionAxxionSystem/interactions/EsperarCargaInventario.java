package co.com.AutomatizacionAxxionSystem.interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.Click;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static co.com.AutomatizacionAxxionSystem.userinterfaces.InventarioUI.BTN_ACTUALIZAR_VISTA;
import static co.com.AutomatizacionAxxionSystem.userinterfaces.InventarioUI.BTN_AGREGAR_EQUIPO;
import static co.com.AutomatizacionAxxionSystem.userinterfaces.InventarioUI.TARJETAS_EQUIPO_VISIBLES;
import static co.com.AutomatizacionAxxionSystem.userinterfaces.InventarioUI.TITULO_INVENTARIO;

/**
 * Espera a que la vista de inventario termine de cargar datos del API
 * y el DOM esté listo para interactuar.
 */
public class EsperarCargaInventario implements Interaction {

    private static final By OVERLAY_CARGA = By.cssSelector("main .overlay");

    private final Duration timeout;

    public EsperarCargaInventario(Duration timeout) {
        this.timeout = timeout;
    }

    public static Interaction completa() {
        return new EsperarCargaInventario(Duration.ofSeconds(25));
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        var driver = actor.usingAbilityTo(BrowseTheWeb.class).getDriver();
        WebDriverWait wait = new WebDriverWait(driver, timeout);

        actor.attemptsTo(EsperarElemento.seaVisible(TITULO_INVENTARIO, timeout));

        // Esperar a que desaparezca el overlay de carga si está presente
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(OVERLAY_CARGA));
        } catch (Exception ignored) {
            // El overlay puede no renderizarse si la carga es instantánea
        }

        if (!inventarioListo(actor)) {
            actor.attemptsTo(
                    Click.on(BTN_ACTUALIZAR_VISTA),
                    EsperarElemento.seaVisible(TITULO_INVENTARIO, Duration.ofSeconds(15))
            );
            try {
                wait.until(ExpectedConditions.invisibilityOfElementLocated(OVERLAY_CARGA));
            } catch (Exception ignored) {
                // Continuar si el overlay no aparece tras refrescar
            }
        }

        wait.until(d -> inventarioListo(actor));
    }

    private boolean inventarioListo(Actor actor) {
        try {
            if (BTN_AGREGAR_EQUIPO.resolveFor(actor).isDisplayed()
                    && BTN_AGREGAR_EQUIPO.resolveFor(actor).isEnabled()) {
                List<?> tarjetas = TARJETAS_EQUIPO_VISIBLES.resolveAllFor(actor);
                return true;
            }
        } catch (Exception ex) {
            return false;
        }
        return false;
    }
}
