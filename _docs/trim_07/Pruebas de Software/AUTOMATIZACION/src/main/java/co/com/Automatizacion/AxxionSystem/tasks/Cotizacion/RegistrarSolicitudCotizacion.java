package co.com.Automatizacion.AxxionSystem.tasks.Cotizacion;

import co.com.Automatizacion.AxxionSystem.models.Cotizacion.SolicitudCotizacion;
import co.com.Automatizacion.AxxionSystem.userInterfaces.Cotizacion.CarritoCotizacionUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.SelectFromOptions;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isClickable;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class RegistrarSolicitudCotizacion implements Task {
    private final SolicitudCotizacion solicitud;

    public RegistrarSolicitudCotizacion(SolicitudCotizacion solicitud) {
        this.solicitud = solicitud;
    }

    public static RegistrarSolicitudCotizacion con(SolicitudCotizacion solicitud) {
        return instrumented(RegistrarSolicitudCotizacion.class, solicitud);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                WaitUntil.the(CarritoCotizacionUI.SELECT_CLIENTE, isVisible()).forNoMoreThan(10).seconds()
        );

        seleccionarCliente(actor);
        diligenciarFecha(actor, "fecha_inicio", solicitud.getFechaInicioFormateada());
        diligenciarFecha(actor, "fecha_fin", solicitud.getFechaFinFormateada());

        actor.attemptsTo(
                WaitUntil.the(CarritoCotizacionUI.BOTON_GENERAR_COTIZACION, isClickable()).forNoMoreThan(20).seconds(),
                Click.on(CarritoCotizacionUI.BOTON_GENERAR_COTIZACION)
        );

        WebDriverWait wait = new WebDriverWait(BrowseTheWeb.as(actor).getDriver(), Duration.ofSeconds(45));
        wait.until(ExpectedConditions.urlContains("/quotation/"));

        String url = BrowseTheWeb.as(actor).getDriver().getCurrentUrl();
        String cotizacionId = url.substring(url.lastIndexOf('/') + 1);
        actor.remember("COTIZACION_ID", cotizacionId);
        actor.remember("SOLICITUD_COTIZACION", solicitud);
    }

    private <T extends Actor> void seleccionarCliente(T actor) {
        try {
            actor.attemptsTo(
                    SelectFromOptions.byVisibleText(solicitud.getCliente()).from(CarritoCotizacionUI.SELECT_CLIENTE)
            );
        } catch (Exception ex) {
            actor.attemptsTo(
                    SelectFromOptions.byVisibleText("Carlos").from(CarritoCotizacionUI.SELECT_CLIENTE)
            );
        }
    }

    private <T extends Actor> void diligenciarFecha(T actor, String idCampo, String valor) {
        WebElement campo = BrowseTheWeb.as(actor).getDriver().findElement(By.id(idCampo));
        JavascriptExecutor js = (JavascriptExecutor) BrowseTheWeb.as(actor).getDriver();
        js.executeScript(
                "arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('input', { bubbles: true })); arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                campo,
                valor
        );
    }
}
