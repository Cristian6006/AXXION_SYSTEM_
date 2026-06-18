package co.com.AutomatizacionAxxionSystem.interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Espera explícita compatible con Serenity 4 (Targets + WebDriverWait).
 */
public class EsperarElemento implements Interaction {

    private final Target target;
    private final Duration timeout;
    private final boolean clickeable;

    public EsperarElemento(Target target, Duration timeout, boolean clickeable) {
        this.target = target;
        this.timeout = timeout;
        this.clickeable = clickeable;
    }

    public static Interaction seaVisible(Target target) {
        return new EsperarElemento(target, Duration.ofSeconds(15), false);
    }

    public static Interaction seaVisible(Target target, Duration timeout) {
        return new EsperarElemento(target, timeout, false);
    }

    public static Interaction seaClickeable(Target target) {
        return new EsperarElemento(target, Duration.ofSeconds(15), true);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        WebDriverWait wait = new WebDriverWait(
                actor.usingAbilityTo(BrowseTheWeb.class).getDriver(),
                timeout);
        if (clickeable) {
            wait.until(ExpectedConditions.elementToBeClickable(target.resolveFor(actor)));
        } else {
            wait.until(ExpectedConditions.visibilityOf(target.resolveFor(actor)));
        }
    }
}
