package co.com.Automatizacion.AxxionSystem.pages;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.support.FindBy;

@DefaultUrl("http://localhost:5173/login")
public class LoginPage extends PageObject {
    @FindBy(id = "email")
    WebElementFacade inputEmail;

    @FindBy(id = "password")
    WebElementFacade inputPassword;

    @FindBy(xpath = "//button[@type='submit']")
    WebElementFacade botonAcceder;

    public void iniciarSesion(String correo, String clave) {
        inputEmail.type(correo);
        inputPassword.type(clave);
        botonAcceder.click();
    }
}
