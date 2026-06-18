package co.com.AutomatizacionAxxionSystem.userinterfaces;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public class autenticacion extends PageObject {

    public static final Target INPUT_USUARIO =
            Target.the("Ingreso del Usuario")
                    .located(By.xpath("//*[@id='email' or @id='username']"));

    public static final Target INPUT_CLAVE =
            Target.the("Ingreso del password")
                    .located(By.id("password"));

    public static final Target BTN_INICIOSESION =
            Target.the("Click Boton inicio sesion")
                    .located(By.xpath("//button[@type='submit' and contains(@class,'login-button')]"));

    /** Indicador de sesión iniciada: enlace al módulo de inventario en el sidebar */
    public static final Target MENU_AUTENTICADO =
            Target.the("Menú lateral con Inventario")
                    .located(By.xpath("//a[contains(@href,'Inventory') or contains(.,'Inventario')]"));

    public static final Target MENSAJE_LOGIN =
            Target.the("Mensaje login exitoso")
                    .located(By.xpath("//h1[@class='post-title'] | //h1[contains(., 'Gestión de Inventario')] | //div[contains(., 'Gestión de Inventario')]"));

    private autenticacion() {
    }
}
