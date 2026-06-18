package co.com.AutomatizacionAxxionSystem.tasks;

import co.com.AutomatizacionAxxionSystem.interactions.EsperarBotonHabilitado;
import co.com.AutomatizacionAxxionSystem.interactions.EsperarElemento;
import co.com.AutomatizacionAxxionSystem.interactions.EstablecerValorInput;
import co.com.AutomatizacionAxxionSystem.models.DatosCotizacion;
import co.com.AutomatizacionAxxionSystem.models.SesionVariable;
import co.com.AutomatizacionAxxionSystem.utils.FechaHoraUtil;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.SelectFromOptions;

import java.time.Duration;

import static co.com.AutomatizacionAxxionSystem.userinterfaces.CotizacionUI.*;
import static co.com.AutomatizacionAxxionSystem.userinterfaces.InventarioUI.BTN_ALQUILAR_EN_TARJETA;

/**
 * Task: genera una cotización desde inventario (carrito → checkout → detalle).
 */
public class GenerarCotizacionDesdeInventario implements Task {

    private final DatosCotizacion datos;

    public GenerarCotizacionDesdeInventario(DatosCotizacion datos) {
        this.datos = datos;
    }

    public static GenerarCotizacionDesdeInventario con(DatosCotizacion datos) {
        return Tasks.instrumented(GenerarCotizacionDesdeInventario.class, datos);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        validarDatos();
        FechaHoraUtil.validarRangoFechas(datos.getFechaInicio(), datos.getFechaFin());

        String fechaInicio = FechaHoraUtil.fechaInicioCotizacion(datos.getFechaInicio());
        String fechaFin = FechaHoraUtil.fechaFinCotizacion(datos.getFechaFin());

        actor.attemptsTo(
                AsegurarEquipoDisponible.conCriterio(datos.getNombreEquipo()),
                EsperarElemento.seaVisible(BTN_ALQUILAR_EN_TARJETA.of(datos.getNombreEquipo()), Duration.ofSeconds(15)),
                Click.on(BTN_ALQUILAR_EN_TARJETA.of(datos.getNombreEquipo())),
                EsperarElemento.seaVisible(TITULO_CARRITO),
                SelectFromOptions.byVisibleText(datos.getNombreCliente()).from(SELECT_CLIENTE),
                EstablecerValorInput.en(INPUT_FECHA_INICIO, fechaInicio),
                EstablecerValorInput.en(INPUT_FECHA_FIN, fechaFin),
                EsperarBotonHabilitado.de(BTN_GENERAR_COTIZACION, Duration.ofSeconds(20)),
                Click.on(BTN_GENERAR_COTIZACION),
                EsperarElemento.seaVisible(TITULO_DETALLE_COTIZACION, Duration.ofSeconds(30))
        );

        String url = actor.usingAbilityTo(BrowseTheWeb.class).getDriver().getCurrentUrl();
        if (url.contains("/quotation/")) {
            actor.remember(SesionVariable.ultimaCotizacionId.toString(),
                    url.substring(url.lastIndexOf('/') + 1));
        }
    }

    private void validarDatos() {
        if (datos.getNombreCliente() == null || datos.getNombreCliente().isBlank()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio");
        }
        if (datos.getNombreEquipo() == null || datos.getNombreEquipo().isBlank()) {
            throw new IllegalArgumentException("El nombre del equipo es obligatorio");
        }
        if (datos.getFechaInicio() == null || datos.getFechaInicio().isBlank()) {
            throw new IllegalArgumentException("La fecha de inicio es obligatoria");
        }
        if (datos.getFechaFin() == null || datos.getFechaFin().isBlank()) {
            throw new IllegalArgumentException("La fecha de fin es obligatoria");
        }
    }
}
