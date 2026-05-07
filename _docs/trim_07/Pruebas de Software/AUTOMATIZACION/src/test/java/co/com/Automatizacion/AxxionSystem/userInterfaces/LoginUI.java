package co.com.Automatizacion.AxxionSystem.userInterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public class LoginUI {
    public static final Target INPUT_EMAIL = Target.the("Campo de correo").located(By.id("email"));
    public static final Target INPUT_PASSWORD = Target.the("Campo de contraseña").located(By.id("password"));
    public static final Target BOTON_ACCEDER = Target.the("Botón para acceder").locatedBy("//button[@type='submit']");
}
