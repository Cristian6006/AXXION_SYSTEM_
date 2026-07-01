package co.com.Automatizacion.AxxionSystem.userInterfaces.Cotizacion;

import net.serenitybdd.screenplay.targets.Target;

public class CarritoCotizacionUI {
    public static final Target TITULO_CARRITO = Target.the("Titulo carrito cotizacion")
            .locatedBy("//h2[contains(.,'Carrito de Cotización')]");

    public static final Target BOTON_VER_CARRITO = Target.the("Boton ver carrito")
            .locatedBy("//button[contains(.,'Ver Carrito')]");

    public static final Target SELECT_CLIENTE = Target.the("Select cliente cotizacion")
            .locatedBy("//select[@id='cliente']");

    public static final Target INPUT_FECHA_INICIO = Target.the("Fecha inicio cotizacion")
            .locatedBy("//input[@id='fecha_inicio']");

    public static final Target INPUT_FECHA_FIN = Target.the("Fecha fin cotizacion")
            .locatedBy("//input[@id='fecha_fin']");

    public static final Target BOTON_GENERAR_COTIZACION = Target.the("Boton generar cotizacion")
            .locatedBy("//button[contains(.,'Generar Cotización') and not(@disabled)]");

    public static final Target ITEM_EN_CARRITO = Target.the("Item {0} en carrito")
            .locatedBy("//h2[contains(.,'Carrito de Cotización')]/following::h3[contains(normalize-space(.), '{0}')]");
}
