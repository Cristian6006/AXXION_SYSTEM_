package co.com.Automatizacion.AxxionSystem.tasks.Cotizacion;

import co.com.Automatizacion.AxxionSystem.userInterfaces.Cotizacion.CotizacionUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.Open;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class AbrirDetalleCotizacion implements Task {
    private final String cotizacionId;

    public AbrirDetalleCotizacion(String cotizacionId) {
        this.cotizacionId = cotizacionId;
    }

    public static AbrirDetalleCotizacion conId(String cotizacionId) {
        return instrumented(AbrirDetalleCotizacion.class, cotizacionId);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String rutaCotizacion = "/quotation/" + cotizacionId;
        String urlActual = BrowseTheWeb.as(actor).getDriver().getCurrentUrl();

        if (!urlActual.contains(rutaCotizacion)) {
            actor.attemptsTo(Open.url("https://axxion-frontend.onrender.com" + rutaCotizacion));
        }

        actor.attemptsTo(
                WaitUntil.the(CotizacionUI.TITULO_COTIZACION, isVisible()).forNoMoreThan(45).seconds()
        );
    }
}
