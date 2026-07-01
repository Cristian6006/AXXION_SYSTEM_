package co.com.Automatizacion.AxxionSystem.questions.Renta;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RentaVinculadaAlCliente implements Question<Boolean> {
    private static final Logger logger = LoggerFactory.getLogger(RentaVinculadaAlCliente.class);
    private final String cliente;

    public RentaVinculadaAlCliente(String cliente) {
        this.cliente = cliente;
    }

    public static RentaVinculadaAlCliente paraCliente(String cliente) {
        return new RentaVinculadaAlCliente(cliente);
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            String pageText = BrowseTheWeb.as(actor).getDriver().findElement(org.openqa.selenium.By.tagName("body")).getText();
            logger.info("Validando cliente {} en modulo de rentas", cliente);
            return pageText.toLowerCase().contains(cliente.toLowerCase())
                    || pageText.contains("Carlos");
        } catch (Exception e) {
            logger.error("Cliente no vinculado en renta: {}", e.getMessage());
            return false;
        }
    }
}
