package co.com.AutomatizacionAxxionSystem.interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static co.com.AutomatizacionAxxionSystem.userinterfaces.InventarioUI.TARJETAS_EQUIPO_VISIBLES;

/**
 * Espera a que el listado de equipos deje de cambiar (carga progresiva del frontend).
 */
public class EsperarListadoEstable implements Interaction {

    private final Duration timeout;

    public EsperarListadoEstable(Duration timeout) {
        this.timeout = timeout;
    }

    public static Interaction enInventario() {
        return new EsperarListadoEstable(Duration.ofSeconds(10));
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        var driver = actor.usingAbilityTo(BrowseTheWeb.class).getDriver();
        WebDriverWait wait = new WebDriverWait(driver, timeout);

        wait.until(d -> {
            int conteo = contarTarjetas(actor);
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
            return conteo == contarTarjetas(actor);
        });
    }

    private int contarTarjetas(Actor actor) {
        try {
            return TARJETAS_EQUIPO_VISIBLES.resolveAllFor(actor).size();
        } catch (Exception ex) {
            return 0;
        }
    }
}
