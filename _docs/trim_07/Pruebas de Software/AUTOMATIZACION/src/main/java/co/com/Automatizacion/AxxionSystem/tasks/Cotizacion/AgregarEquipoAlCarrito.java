package co.com.Automatizacion.AxxionSystem.tasks.Cotizacion;

import co.com.Automatizacion.AxxionSystem.tasks.Inventario.BuscarEquipo;
import co.com.Automatizacion.AxxionSystem.userInterfaces.Cotizacion.CarritoCotizacionUI;
import co.com.Automatizacion.AxxionSystem.userInterfaces.Inventario.InventarioUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.openqa.selenium.TimeoutException;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isClickable;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class AgregarEquipoAlCarrito implements Task {
    private final String nombreEquipo;

    public AgregarEquipoAlCarrito(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public static AgregarEquipoAlCarrito conNombre(String nombreEquipo) {
        return instrumented(AgregarEquipoAlCarrito.class, nombreEquipo);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String criterio = actor.recall("CRITERIO_BUSQUEDA_EQUIPO");
        if (criterio == null) {
            criterio = BuscarEquipo.normalizarCriterioBusqueda(nombreEquipo);
        }

        actor.attemptsTo(BuscarEquipo.porNombre(nombreEquipo));

        try {
            actor.attemptsTo(
                    WaitUntil.the(InventarioUI.BOTON_ALQUILAR_EN_TARJETA.of(criterio), isClickable()).forNoMoreThan(10).seconds(),
                    Click.on(InventarioUI.BOTON_ALQUILAR_EN_TARJETA.of(criterio))
            );
        } catch (AssertionError | TimeoutException ex) {
            actor.attemptsTo(
                    WaitUntil.the(CarritoCotizacionUI.BOTON_VER_CARRITO, isClickable()).forNoMoreThan(5).seconds(),
                    Click.on(CarritoCotizacionUI.BOTON_VER_CARRITO)
            );
        }

        actor.attemptsTo(
                WaitUntil.the(CarritoCotizacionUI.TITULO_CARRITO, isVisible()).forNoMoreThan(10).seconds()
        );
        actor.remember("EQUIPO_COTIZACION", nombreEquipo);
    }

    public static String normalizarCriterioPublico(String nombre) {
        return BuscarEquipo.normalizarCriterioBusqueda(nombre);
    }
}
