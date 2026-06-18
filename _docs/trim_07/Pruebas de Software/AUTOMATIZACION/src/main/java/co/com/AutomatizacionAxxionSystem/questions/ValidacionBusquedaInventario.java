package co.com.AutomatizacionAxxionSystem.questions;

import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static co.com.AutomatizacionAxxionSystem.userinterfaces.InventarioUI.TARJETAS_EQUIPO_VISIBLES;

/**
 * Verifica que cada equipo visible cumple el criterio en nombre, marca o serie.
 * Una lista vacía también es válida cuando ningún equipo coincide (ej. criterio "Dell").
 */
public class ValidacionBusquedaInventario implements Question<Boolean> {

    private final String criterio;

    public ValidacionBusquedaInventario(String criterio) {
        this.criterio = criterio;
    }

    public static ValidacionBusquedaInventario conCriterio(String criterio) {
        return new ValidacionBusquedaInventario(criterio);
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        String criterioLower = criterio.toLowerCase().trim();

        try {
            var encabezados = TARJETAS_EQUIPO_VISIBLES.resolveAllFor(actor);

            if (encabezados.isEmpty()) {
                return true;
            }

            for (WebElementFacade h3 : encabezados) {
                WebElement elemento = h3.getElement();
                WebElement tarjeta = elemento.findElement(By.xpath(
                        "./ancestor::div[contains(@class,'equipment-card') or contains(@class,'p-6')][1]"));
                String textoTarjeta = tarjeta.getText().toLowerCase();
                if (!textoTarjeta.contains(criterioLower)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
