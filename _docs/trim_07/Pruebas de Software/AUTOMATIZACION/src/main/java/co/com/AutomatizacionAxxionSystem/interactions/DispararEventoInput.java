package co.com.AutomatizacionAxxionSystem.interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

/**
 * Dispara eventos input/change en un campo para que frameworks reactivos (Vue) actualicen v-model.
 */
public class DispararEventoInput implements Interaction {

    private final Target target;

    public DispararEventoInput(Target target) {
        this.target = target;
    }

    public static Interaction en(Target target) {
        return new DispararEventoInput(target);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        WebElement element = target.resolveFor(actor);
        JavascriptExecutor js = (JavascriptExecutor) actor.usingAbilityTo(BrowseTheWeb.class).getDriver();
        js.executeScript(
                "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));"
                        + "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                element
        );
    }
}
