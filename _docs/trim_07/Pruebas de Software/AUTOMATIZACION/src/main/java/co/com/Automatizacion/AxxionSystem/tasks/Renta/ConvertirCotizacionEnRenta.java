package co.com.Automatizacion.AxxionSystem.tasks.Renta;

import co.com.Automatizacion.AxxionSystem.userInterfaces.Cotizacion.CotizacionUI;
import co.com.Automatizacion.AxxionSystem.userInterfaces.Renta.RentalUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isClickable;

public class ConvertirCotizacionEnRenta implements Task {

    public static ConvertirCotizacionEnRenta ahora() {
        return instrumented(ConvertirCotizacionEnRenta.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String cotizacionId = actor.recall("COTIZACION_ID");
        actor.attemptsTo(
                WaitUntil.the(CotizacionUI.BOTON_CONVERTIR_RENTA, isClickable()).forNoMoreThan(15).seconds(),
                Click.on(CotizacionUI.BOTON_CONVERTIR_RENTA)
        );

        WebDriverWait wait = new WebDriverWait(BrowseTheWeb.as(actor).getDriver(), Duration.ofSeconds(45));
        wait.until(ExpectedConditions.urlContains("/Rental"));
        wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(By.xpath(RentalUI.TITULO_MODULO.getCssOrXPathSelector())),
                ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "Programada"),
                ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "Renta #")
        ));

        actor.remember("RENTA_COTIZACION_ORIGEN", cotizacionId);
    }
}
