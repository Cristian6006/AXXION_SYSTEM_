package co.com.Automatizacion.AxxionSystem.questions.Categorias;

import co.com.Automatizacion.AxxionSystem.questions.Autenticacion.MensajeBienvenida;
import co.com.Automatizacion.AxxionSystem.userInterfaces.CategoriasUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static co.com.Automatizacion.AxxionSystem.userInterfaces.CategoriasUI.MENSAJE_GUARDADO;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class MensajeGuardado implements Question<Boolean> {

    private static final Logger logger =
            LoggerFactory.getLogger(MensajeGuardado.class);

    private static final String MENSAJE_ESPERADO = "¡Guardado!";

    public static MensajeGuardado mensajeGuardado() {
        return new MensajeGuardado();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            WaitUntil.the(CategoriasUI.MENSAJE_GUARDADO, isVisible()).forNoMoreThan(5).seconds();
            String texto = Text.of(MENSAJE_GUARDADO).answeredBy(actor).trim();
            logger.info("Texto capturado: {}", texto);
            return texto.equalsIgnoreCase(MENSAJE_ESPERADO);
        } catch (Exception e){
            logger.error("El mensaje de guardado no fue encontrado: {}", e.getMessage());
            return false;
        }
    }
}
