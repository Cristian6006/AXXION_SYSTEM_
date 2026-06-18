package co.com.AutomatizacionAxxionSystem.interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Espera a que un botón esté visible y habilitado (sin atributo disabled).
 */
public class EsperarBotonHabilitado implements Interaction {

    private final Target target;
    private final Duration timeout;

    public EsperarBotonHabilitado(Target target, Duration timeout) {
        this.target = target;
        this.timeout = timeout;
    }

    public static Interaction de(Target target) {
        return new EsperarBotonHabilitado(target, Duration.ofSeconds(15));
    }

    public static Interaction de(Target target, Duration timeout) {
        return new EsperarBotonHabilitado(target, timeout);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        var driver = actor.usingAbilityTo(BrowseTheWeb.class).getDriver();
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.elementToBeClickable(target.resolveFor(actor)));
    }
}
