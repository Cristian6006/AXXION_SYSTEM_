package co.com.Automatizacion.AxxionSystem.questions.Cotizacion;

import co.com.Automatizacion.AxxionSystem.userInterfaces.Cotizacion.CotizacionUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class CotizacionDetalleVisible implements Question<Boolean> {
    private static final Logger logger = LoggerFactory.getLogger(CotizacionDetalleVisible.class);
    private final String estadoEsperado;

    public CotizacionDetalleVisible(String estadoEsperado) {
        this.estadoEsperado = estadoEsperado;
    }

    public static CotizacionDetalleVisible conEstado(String estadoEsperado) {
        return new CotizacionDetalleVisible(estadoEsperado);
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            String url = BrowseTheWeb.as(actor).getDriver().getCurrentUrl();
            WaitUntil.the(CotizacionUI.TITULO_COTIZACION, isVisible()).forNoMoreThan(15).seconds();
            String titulo = Text.of(CotizacionUI.TITULO_COTIZACION).answeredBy(actor).trim();
            String estado = Text.of(CotizacionUI.BADGE_ESTADO).answeredBy(actor).trim();

            logger.info("URL cotizacion: {}", url);
            logger.info("Titulo: {}, estado esperado: {}, encontrado: {}", titulo, estadoEsperado, estado);

            return url.contains("/quotation/") && estado.equalsIgnoreCase(estadoEsperado);
        } catch (Exception e) {
            logger.error("Detalle de cotizacion no visible: {}", e.getMessage());
            return false;
        }
    }
}
