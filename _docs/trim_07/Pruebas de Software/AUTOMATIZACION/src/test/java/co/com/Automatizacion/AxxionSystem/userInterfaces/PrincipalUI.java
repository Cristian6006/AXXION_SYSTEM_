package co.com.Automatizacion.AxxionSystem.userInterfaces;

import net.serenitybdd.screenplay.targets.Target;

public class PrincipalUI {
    public static final Target MENSAJE_BIENVENIDA = Target.the("Mensaje de bienvenida")
            .locatedBy("//h1[contains(., 'Bienvenido')]");
}
