package co.com.AutomatizacionAxxionSystem.tasks;

import co.com.AutomatizacionAxxionSystem.interactions.EsperarCargaInventario;
import co.com.AutomatizacionAxxionSystem.interactions.EsperarElemento;
import co.com.AutomatizacionAxxionSystem.interactions.SeleccionarCategoriaEquipo;
import co.com.AutomatizacionAxxionSystem.models.DatosEquipo;
import co.com.AutomatizacionAxxionSystem.models.SesionVariable;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static co.com.AutomatizacionAxxionSystem.userinterfaces.InventarioUI.*;

public class AgregarEquipoAlInventario implements Task {

    private final DatosEquipo datosEquipo;

    public AgregarEquipoAlInventario(DatosEquipo datosEquipo) {
        this.datosEquipo = datosEquipo;
    }

    public static AgregarEquipoAlInventario con(DatosEquipo datosEquipo) {
        return Tasks.instrumented(AgregarEquipoAlInventario.class, datosEquipo);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        validarDatos();

        // Serie única por ejecución para evitar rechazo por duplicado en BD
        String serieUnica = datosEquipo.getNumeroSerie() + "-" + System.currentTimeMillis();

        actor.attemptsTo(
                EsperarCargaInventario.completa(),
                Click.on(BTN_AGREGAR_EQUIPO),
                EsperarElemento.seaVisible(MODAL_TITULO),
                Enter.theValue(datosEquipo.getNombre()).into(INPUT_NOMBRE_EQUIPO),
                Enter.theValue(datosEquipo.getMarca()).into(INPUT_MARCA),
                Enter.theValue(datosEquipo.getModelo()).into(INPUT_MODELO),
                Enter.theValue(serieUnica).into(INPUT_SERIE),
                SeleccionarCategoriaEquipo.conValor(datosEquipo.getCategoria()),
                Enter.theValue(datosEquipo.getTarifaDiaria()).into(INPUT_TARIFA_DIARIA),
                Click.on(BTN_SUBMIT_MODAL)
        );

        esperarModalCerrado(actor);
        actor.attemptsTo(EsperarElemento.seaVisible(ALERTA_EXITO_EQUIPO_AGREGADO));

        actor.remember(SesionVariable.equipoRegistrado.toString(), datosEquipo.getNombre());
    }

    private <T extends Actor> void esperarModalCerrado(T actor) {
        var driver = actor.usingAbilityTo(BrowseTheWeb.class).getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        By modalTitulo = By.xpath("//div[contains(@class,'font-semibold') and contains(., 'Agregar Nuevo Equipo')]");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(modalTitulo));
    }

    private void validarDatos() {
        if (datosEquipo.getNombre() == null || datosEquipo.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del equipo es obligatorio");
        }
        if (datosEquipo.getMarca() == null || datosEquipo.getMarca().isBlank()) {
            throw new IllegalArgumentException("La marca del equipo es obligatoria");
        }
        if (datosEquipo.getNumeroSerie() == null || datosEquipo.getNumeroSerie().isBlank()) {
            throw new IllegalArgumentException("El número de serie es obligatorio");
        }
        if (datosEquipo.getTarifaDiaria() == null || datosEquipo.getTarifaDiaria().isBlank()) {
            throw new IllegalArgumentException("La tarifa diaria es obligatoria");
        }
    }
}
