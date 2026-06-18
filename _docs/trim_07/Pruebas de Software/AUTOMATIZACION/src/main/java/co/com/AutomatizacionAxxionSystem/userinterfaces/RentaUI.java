package co.com.AutomatizacionAxxionSystem.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

/**
 * Localizadores de la vista de alquileres/rentas.
 */
public class RentaUI {

    /** Tarjetas de renta visibles en el módulo /Rental. */
    public static final Target TARJETAS_RENTA =
            Target.the("Tarjetas de renta")
                    .located(By.xpath("//div[contains(@class,'rental-card')]"));

    /** Tarjeta de renta que conserva estado y referencia a la cotización origen. */
    public static final Target RENTA_CON_ESTADO_Y_REFERENCIA =
            Target.the("Renta con estado '{0}' y cotización origen '{1}'")
                    .locatedBy("//div[contains(@class,'rental-card') "
                            + "and .//*[normalize-space(.)='{0}'] "
                            + "and .//*[contains(normalize-space(.), 'Generado desde Cotización #{1}')]]");

    /** Nota de referencia a la cotización origen dentro de una renta. */
    public static final Target REFERENCIA_COTIZACION =
            Target.the("Referencia a cotización '{0}'")
                    .locatedBy("//*[contains(normalize-space(.), 'Generado desde Cotización #{0}')]");

    private RentaUI() {
    }
}
