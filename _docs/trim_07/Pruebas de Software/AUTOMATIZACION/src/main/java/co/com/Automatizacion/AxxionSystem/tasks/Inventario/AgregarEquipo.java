package co.com.Automatizacion.AxxionSystem.tasks.Inventario;

import co.com.Automatizacion.AxxionSystem.models.Inventario.Equipo;
import co.com.Automatizacion.AxxionSystem.userInterfaces.Inventario.InventarioUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Clear;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.SelectFromOptions;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isClickable;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class AgregarEquipo implements Task {
    private final Equipo equipo;

    public AgregarEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public static AgregarEquipo con(Equipo equipo) {
        return instrumented(AgregarEquipo.class, equipo);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                WaitUntil.the(InventarioUI.BOTON_AGREGAR_EQUIPO, isClickable()).forNoMoreThan(15).seconds(),
                Click.on(InventarioUI.BOTON_AGREGAR_EQUIPO),
                WaitUntil.the(InventarioUI.INPUT_NOMBRE, isVisible()).forNoMoreThan(10).seconds(),
                Clear.field(InventarioUI.INPUT_NOMBRE),
                Enter.theValue(equipo.getNombre()).into(InventarioUI.INPUT_NOMBRE),
                Clear.field(InventarioUI.INPUT_MARCA),
                Enter.theValue(equipo.getMarca()).into(InventarioUI.INPUT_MARCA),
                Clear.field(InventarioUI.INPUT_MODELO),
                Enter.theValue(equipo.getModelo()).into(InventarioUI.INPUT_MODELO),
                Clear.field(InventarioUI.INPUT_SERIE),
                Enter.theValue(equipo.getSerie()).into(InventarioUI.INPUT_SERIE),
                SelectFromOptions.byVisibleText(equipo.getCategoria()).from(InventarioUI.SELECT_CATEGORIA),
                SelectFromOptions.byValue("disponible").from(InventarioUI.SELECT_ESTADO),
                Clear.field(InventarioUI.INPUT_TARIFA_DIARIA),
                Enter.theValue(equipo.getTarifaDiaria()).into(InventarioUI.INPUT_TARIFA_DIARIA),
                Click.on(InventarioUI.BOTON_GUARDAR_EQUIPO)
        );

        String criterio = BuscarEquipo.normalizarCriterioBusqueda(equipo.getNombre());
        actor.attemptsTo(
                BuscarEquipo.porNombre(equipo.getNombre()),
                WaitUntil.the(InventarioUI.NOMBRE_EQUIPO_EN_LISTADO.of(criterio), isVisible()).forNoMoreThan(60).seconds()
        );
        actor.remember("EQUIPO", equipo);
    }
}
