package co.com.Automatizacion.AxxionSystem.tasks.Inventario;

import co.com.Automatizacion.AxxionSystem.models.Inventario.Equipo;
import co.com.Automatizacion.AxxionSystem.userInterfaces.Inventario.InventarioUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isClickable;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class EliminarEquipo implements Task {
    private final Equipo equipo;

    public EliminarEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public static EliminarEquipo con(Equipo equipo) {
        return instrumented(EliminarEquipo.class, equipo);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String nombre = equipo.getNombre();
        String criterio = BuscarEquipo.normalizarCriterioBusqueda(nombre);
        actor.attemptsTo(
                BuscarEquipo.porNombre(nombre),
                WaitUntil.the(InventarioUI.BOTON_ELIMINAR_EN_TARJETA.of(criterio), isClickable()).forNoMoreThan(10).seconds()
        );

        WebElement botonEliminar = InventarioUI.BOTON_ELIMINAR_EN_TARJETA.of(criterio).resolveFor(actor);
        ((JavascriptExecutor) BrowseTheWeb.as(actor).getDriver())
                .executeScript("arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();", botonEliminar);

        actor.attemptsTo(
                WaitUntil.the(InventarioUI.MODAL_ELIMINAR, isVisible()).forNoMoreThan(10).seconds(),
                WaitUntil.the(InventarioUI.BOTON_CONFIRMAR_ELIMINAR, isClickable()).forNoMoreThan(10).seconds()
        );

        WebElement botonConfirmar = InventarioUI.BOTON_CONFIRMAR_ELIMINAR.resolveFor(actor);
        ((JavascriptExecutor) BrowseTheWeb.as(actor).getDriver())
                .executeScript("arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();", botonConfirmar);
    }
}
