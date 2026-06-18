package co.com.AutomatizacionAxxionSystem.interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static co.com.AutomatizacionAxxionSystem.userinterfaces.InventarioUI.TARJETAS_EQUIPO_VISIBLES;

/**
 * Espera a que el filtro de búsqueda de inventario estabilice el conteo de tarjetas visibles.
 */
public class EsperarFiltroInventario implements Interaction {

    private final Duration timeout;

    public EsperarFiltroInventario(Duration timeout) {
        this.timeout = timeout;
    }

    public static Interaction estabilizado() {
        return new EsperarFiltroInventario(Duration.ofSeconds(5));
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        var driver = actor.usingAbilityTo(BrowseTheWeb.class).getDriver();
        WebDriverWait wait = new WebDriverWait(driver, timeout);

        wait.until(d -> {
            int conteoActual = contarTarjetas(actor);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
            int conteoPosterior = contarTarjetas(actor);
            return conteoActual == conteoPosterior;
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
