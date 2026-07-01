package co.com.Automatizacion.AxxionSystem.questions.Cotizacion;

import co.com.Automatizacion.AxxionSystem.userInterfaces.Cotizacion.CotizacionUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class DesgloseCostosCotizacion implements Question<Boolean> {
    private static final Logger logger = LoggerFactory.getLogger(DesgloseCostosCotizacion.class);
    private final String nombreEquipo;

    public DesgloseCostosCotizacion(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public static DesgloseCostosCotizacion paraEquipo(String nombreEquipo) {
        return new DesgloseCostosCotizacion(nombreEquipo);
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            WaitUntil.the(CotizacionUI.TABLA_ITEMS, isVisible()).forNoMoreThan(10).seconds();
            String item = Text.of(CotizacionUI.FILA_ITEM_POR_NOMBRE.of(nombreEquipo)).answeredBy(actor).trim();
            String total = Text.of(CotizacionUI.TOTAL_COTIZACION).answeredBy(actor).trim();

            logger.info("Item cotizado: {}, total: {}", item, total);

            return item.toLowerCase().contains(nombreEquipo.toLowerCase())
                    && total != null
                    && !total.isBlank();
        } catch (Exception e) {
            logger.error("Desglose de costos no valido: {}", e.getMessage());
            return false;
        }
    }
}
