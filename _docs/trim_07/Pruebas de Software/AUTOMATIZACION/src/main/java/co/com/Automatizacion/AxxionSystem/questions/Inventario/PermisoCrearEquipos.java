package co.com.Automatizacion.AxxionSystem.questions.Inventario;

import co.com.Automatizacion.AxxionSystem.userInterfaces.Inventario.InventarioUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class PermisoCrearEquipos implements Question<Boolean> {
    private static final Logger logger = LoggerFactory.getLogger(PermisoCrearEquipos.class);

    public static PermisoCrearEquipos disponible() {
        return new PermisoCrearEquipos();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            WaitUntil.the(InventarioUI.BOTON_AGREGAR_EQUIPO, isVisible()).forNoMoreThan(10).seconds();
            return true;
        } catch (Exception e) {
            logger.error("El usuario no tiene visible el boton de agregar equipo: {}", e.getMessage());
            return false;
        }
    }
}
