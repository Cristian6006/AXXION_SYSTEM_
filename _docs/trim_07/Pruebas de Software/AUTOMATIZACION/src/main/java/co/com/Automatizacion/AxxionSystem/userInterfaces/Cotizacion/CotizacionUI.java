package co.com.Automatizacion.AxxionSystem.userInterfaces.Cotizacion;

import net.serenitybdd.screenplay.targets.Target;

public class CotizacionUI {
    public static final Target TITULO_COTIZACION = Target.the("Titulo cotizacion")
            .locatedBy("//h1[contains(.,'Cotización #')]");

    public static final Target BADGE_ESTADO = Target.the("Badge estado cotizacion")
            .locatedBy("//h1[contains(.,'Cotización #')]/ancestor::div[contains(@class,'flex')]//span[contains(@class,'rounded-full')]");

    public static final Target TABLA_ITEMS = Target.the("Tabla items cotizados")
            .locatedBy("//h3[contains(.,'Items Cotizados')]/following::table");

    public static final Target FILA_ITEM_POR_NOMBRE = Target.the("Fila item {0}")
            .locatedBy("//table//td[contains(normalize-space(.), '{0}')]");

    public static final Target TOTAL_COTIZACION = Target.the("Total cotizacion")
            .locatedBy("//tfoot//td[last()]");

    public static final Target BOTON_CONVERTIR_RENTA = Target.the("Boton convertir a renta")
            .locatedBy("//button[contains(.,'Convertir a Renta')]");

    public static final Target ALERTA_COTIZACION = Target.the("Alerta cotizacion")
            .locatedBy("//div[contains(@class,'alert') or contains(@role,'alert')]");
}
