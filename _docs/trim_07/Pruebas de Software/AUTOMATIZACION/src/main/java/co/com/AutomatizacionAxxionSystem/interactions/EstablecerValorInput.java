package co.com.AutomatizacionAxxionSystem.interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

/**
 * Establece el valor de un input (especialmente datetime-local) y dispara eventos
 * para que Vue actualice el v-model correctamente.
 */
public class EstablecerValorInput implements Interaction {

    private final Target target;
    private final String valor;

    public EstablecerValorInput(Target target, String valor) {
        this.target = target;
        this.valor = valor;
    }

    public static Interaction en(Target target, String valor) {
        return new EstablecerValorInput(target, valor);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El valor del input no puede estar vacío");
        }

        WebElement element = target.resolveFor(actor);
        JavascriptExecutor js = (JavascriptExecutor) actor.usingAbilityTo(BrowseTheWeb.class).getDriver();
        js.executeScript(
                "const el = arguments[0]; const val = arguments[1];"
                        + "el.value = val;"
                        + "el.dispatchEvent(new Event('input', { bubbles: true }));"
                        + "el.dispatchEvent(new Event('change', { bubbles: true }));",
                element,
                valor
        );
    }
}
