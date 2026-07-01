package co.com.Automatizacion.AxxionSystem.questions.Inventario;

import co.com.Automatizacion.AxxionSystem.userInterfaces.Inventario.InventarioUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isNotVisible;

public class ModalRegistroEquipoVisible implements Question<Boolean> {
    private static final Logger logger = LoggerFactory.getLogger(ModalRegistroEquipoVisible.class);

    public static ModalRegistroEquipoVisible modalRegistro() {
        return new ModalRegistroEquipoVisible();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            WaitUntil.the(InventarioUI.INPUT_NOMBRE, isNotVisible()).forNoMoreThan(15).seconds();
            logger.info("Modal de registro ya no es visible");
            return true;
        } catch (Exception e) {
            logger.error("El modal de registro sigue visible: {}", e.getMessage());
            return false;
        }
    }
}
