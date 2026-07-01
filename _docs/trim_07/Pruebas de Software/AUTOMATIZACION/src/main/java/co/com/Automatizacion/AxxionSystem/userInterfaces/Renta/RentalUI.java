package co.com.Automatizacion.AxxionSystem.userInterfaces.Renta;

import net.serenitybdd.screenplay.targets.Target;

public class RentalUI {
    public static final Target TITULO_MODULO = Target.the("Contenedor rentas")
            .locatedBy("//div[contains(@class,'rental-card')]");

    public static final Target TARJETA_RENTA_POR_ID = Target.the("Tarjeta renta {0}")
            .locatedBy("//h3[contains(.,'Renta #{0}')]");

    public static final Target ESTADO_RENTA_EN_TARJETA = Target.the("Estado renta {0}")
            .locatedBy("//h3[contains(.,'Renta #{0}')]/ancestor::div[contains(@class,'rental-card')]//span[contains(@class,'rounded-full')]");

    public static final Target CLIENTE_EN_TARJETA = Target.the("Cliente renta {0}")
            .locatedBy("//h3[contains(.,'Renta #{0}')]/ancestor::div[contains(@class,'rental-card')]//p[contains(@class,'text-white')]");

    public static final Target NOTAS_RENTA = Target.the("Notas renta")
            .locatedBy("//div[contains(@class,'rental-card')]");
}
