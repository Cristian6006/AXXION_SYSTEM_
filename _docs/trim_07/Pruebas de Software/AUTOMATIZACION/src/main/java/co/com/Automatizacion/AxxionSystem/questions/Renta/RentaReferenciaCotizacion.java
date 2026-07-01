package co.com.Automatizacion.AxxionSystem.questions.Renta;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RentaReferenciaCotizacion implements Question<Boolean> {
    private static final Logger logger = LoggerFactory.getLogger(RentaReferenciaCotizacion.class);

    public static RentaReferenciaCotizacion mantieneReferencia() {
        return new RentaReferenciaCotizacion();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            String cotizacionId = actor.recall("COTIZACION_ID");
            String pageText = BrowseTheWeb.as(actor).getDriver().findElement(org.openqa.selenium.By.tagName("body")).getText();

            logger.info("Buscando referencia a cotizacion #{}", cotizacionId);

            return pageText.contains("Generado desde Cotización #" + cotizacionId)
                    || pageText.contains("Cotización #" + cotizacionId)
                    || pageText.contains("cotizacion #" + cotizacionId);
        } catch (Exception e) {
            logger.error("No se encontro referencia a la cotizacion de origen: {}", e.getMessage());
            return false;
        }
    }
}
