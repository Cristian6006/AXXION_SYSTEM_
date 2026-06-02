package co.com.Automatizacion.userinterfaces;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public class autenticacion extends PageObject {

    public static final Target INPUT_USUARIO =
            Target.the("Ingreso del Usuario")
                    .located(By.xpath("//*[@id=\"username\"]"));

    public static final Target INPUT_CLAVE =
            Target.the("Ingreso del password")
                    .located(By.id("password"));

    public static final Target BTN_INICIOSESION =
            Target.the("Click Boton inicio sesion")
                    .located(By.id("submit"));

    public static final Target MENSAJE_LOGIN =
            Target.the("Mensaje login exitoso")
                    .located(By.xpath("//h1[@class='post-title']"));

    private autenticacion() {
    }
}
