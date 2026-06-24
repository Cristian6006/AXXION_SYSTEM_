package co.com.Automatizacion.AxxionSystem.questions.Categorias;

import co.com.Automatizacion.AxxionSystem.userInterfaces.Categorias.CategoriasUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static co.com.Automatizacion.AxxionSystem.userInterfaces.Categorias.CategoriasUI.MENSAJE_GUARDADO;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class MensajeExito implements Question<Boolean> {
    private static final Logger logger =
            LoggerFactory.getLogger(MensajeExito.class);

    private static final String MENSAJE_ESPERADO = "¡Guardado!";

    public static MensajeExito mensajeExito() {
        return new MensajeExito();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            WaitUntil.the(MENSAJE_GUARDADO, isVisible()).forNoMoreThan(5).seconds();
            String texto = Text.of(MENSAJE_GUARDADO).answeredBy(actor).trim();
            logger.info("Esperado: {}", MENSAJE_ESPERADO);
            logger.info("Encontrado: {}", texto);
            return texto.equalsIgnoreCase(MENSAJE_ESPERADO);
        } catch (Exception e){
            logger.error("El mensaje de guardado no fue encontrado: {}", e.getMessage());
            return false;
        }
    }
}
