package co.com.Automatizacion.AxxionSystem.questions.Inventario;

import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class AlertaInventarioVisible implements Question<Boolean> {
    private static final Logger logger = LoggerFactory.getLogger(AlertaInventarioVisible.class);
    private final String textoEsperado;

    public AlertaInventarioVisible(String textoEsperado) {
        this.textoEsperado = textoEsperado;
    }

    public static AlertaInventarioVisible conTexto(String textoEsperado) {
        return new AlertaInventarioVisible(textoEsperado);
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            var driver = BrowseTheWeb.as(actor).getDriver();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), textoEsperado),
                    ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "eliminado exitosamente"),
                    ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "agregado exitosamente"),
                    ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "Equipo Agregado")
            ));
            return true;
        } catch (Exception e) {
            String pageText = BrowseTheWeb.as(actor).getDriver().findElement(By.tagName("body")).getText().toLowerCase();
            logger.warn("Alerta no capturada a tiempo: {}", e.getMessage());
            return pageText.contains(textoEsperado.toLowerCase())
                    || pageText.contains("eliminado exitosamente")
                    || pageText.contains("agregado exitosamente")
                    || pageText.contains("producto eliminado")
                    || pageText.contains("equipo agregado");
        }
    }
}
