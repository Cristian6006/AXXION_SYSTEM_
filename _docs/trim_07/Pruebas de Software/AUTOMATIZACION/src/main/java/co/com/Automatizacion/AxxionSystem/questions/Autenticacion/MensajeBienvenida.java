package co.com.Automatizacion.AxxionSystem.questions.Autenticacion;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import static co.com.Automatizacion.AxxionSystem.userInterfaces.PrincipalUI.MENSAJE_BIENVENIDA;

public class MensajeBienvenida implements Question<Boolean> {

    private static final Logger logger =
            LoggerFactory.getLogger(MensajeBienvenida.class);

    private static final String MENSAJE_ESPERADO = "¡Bienvenido a AXION SYSTEM!";

    public static MensajeBienvenida mensajeBienvenida() {
        return new MensajeBienvenida();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            String texto = Text.of(MENSAJE_BIENVENIDA).answeredBy(actor).trim();
            logger.info("Texto capturado: {}", texto);
            return texto.equalsIgnoreCase(MENSAJE_ESPERADO);
        } catch (Exception e){
            logger.error("El mensaje de bienvenida no fue encontrado: {}", e.getMessage());
            return false;
        }
    }

}
