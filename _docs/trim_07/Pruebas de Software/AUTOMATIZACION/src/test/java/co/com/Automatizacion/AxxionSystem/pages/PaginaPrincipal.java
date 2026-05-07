package co.com.Automatizacion.AxxionSystem.pages;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.support.FindBy;

public class PaginaPrincipal extends PageObject {
    @FindBy(xpath = "//h1[contains(., 'Bienvenido')]")
    WebElementFacade mensajeBienvenida;

    public boolean mensajeBienvenidaEsVisible() {
        return mensajeBienvenida.isVisible();
    }

    public String obtenerUrlActual() {
        return getDriver().getCurrentUrl();
    }
}
