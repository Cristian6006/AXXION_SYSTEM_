package co.com.AutomatizacionAxxionSystem.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;

/**
 * Verifica que el filtro se aplicó sin navegar fuera de inventario.
 */
public class ValidacionFiltroInmediato implements Question<Boolean> {

    public static ValidacionFiltroInmediato sinRecargarPagina() {
        return new ValidacionFiltroInmediato();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        String url = actor.usingAbilityTo(BrowseTheWeb.class).getDriver().getCurrentUrl();
        return url != null && url.contains("/Inventory");
    }
}
