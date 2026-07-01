package co.com.Automatizacion.AxxionSystem.questions.Renta;

import co.com.Automatizacion.AxxionSystem.userInterfaces.Renta.RentalUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class RentaRegistradaConEstado implements Question<Boolean> {
    private static final Logger logger = LoggerFactory.getLogger(RentaRegistradaConEstado.class);
    private final String estadoEsperado;

    public RentaRegistradaConEstado(String estadoEsperado) {
        this.estadoEsperado = estadoEsperado;
    }

    public static RentaRegistradaConEstado conEstado(String estadoEsperado) {
        return new RentaRegistradaConEstado(estadoEsperado);
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            var driver = BrowseTheWeb.as(actor).getDriver();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(45));

            wait.until(ExpectedConditions.urlContains("/Rental"));
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.presenceOfElementLocated(By.xpath(RentalUI.TITULO_MODULO.getCssOrXPathSelector())),
                    ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), estadoEsperado)
            ));

            String url = driver.getCurrentUrl();
            String pageText = driver.findElement(By.tagName("body")).getText();
            logger.info("URL renta: {}", url);
            logger.info("Estado esperado: {}", estadoEsperado);

            return url.contains("/Rental") && pageText.contains(estadoEsperado);
        } catch (Exception e) {
            logger.error("Renta con estado esperado no encontrada: {}", e.getMessage());
            return false;
        }
    }
}
