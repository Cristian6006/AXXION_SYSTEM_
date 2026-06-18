package co.com.AutomatizacionAxxionSystem.tasks;

import co.com.AutomatizacionAxxionSystem.models.DatosEquipo;
import co.com.AutomatizacionAxxionSystem.models.SesionVariable;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static co.com.AutomatizacionAxxionSystem.userinterfaces.InventarioUI.BTN_ALQUILAR_EN_TARJETA;

/**
 * Task: garantiza que exista un equipo cuyo nombre contenga el criterio indicado
 * y que esté disponible para alquilar (botón Alquilar visible).
 */
public class AsegurarEquipoDisponible implements Task {

    private static final String NOMBRE_MONITOR_QA = "Monitor QA AutoTest";

    private final String criterioNombre;

    public AsegurarEquipoDisponible(String criterioNombre) {
        this.criterioNombre = criterioNombre;
    }

    public static AsegurarEquipoDisponible conCriterio(String criterioNombre) {
        return Tasks.instrumented(AsegurarEquipoDisponible.class, criterioNombre);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(NavegarAInventario.elModulo());

        if (!botonAlquilarVisible(actor, criterioNombre)) {
            DatosEquipo equipoQa = new DatosEquipo(
                    NOMBRE_MONITOR_QA,
                    "LG",
                    "29WL500",
                    "QA-MON-" + System.currentTimeMillis(),
                    "Video",
                    "45000"
            );
            actor.attemptsTo(AgregarEquipoAlInventario.con(equipoQa));
            actor.attemptsTo(NavegarAInventario.elModulo());
        }

        actor.remember(SesionVariable.equipoCotizacion.toString(), criterioNombre);
    }

    private boolean botonAlquilarVisible(Actor actor, String criterio) {
        try {
            var driver = actor.usingAbilityTo(BrowseTheWeb.class).getDriver();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.visibilityOf(
                    BTN_ALQUILAR_EN_TARJETA.of(criterio).resolveFor(actor)
            ));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
