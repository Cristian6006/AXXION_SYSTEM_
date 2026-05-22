package co.com.Automatizacion.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static co.com.Automatizacion.userinterfaces.autenticacion.MENSAJE_LOGIN;

public class ValidacionLogin implements Question<Boolean> {

    private static final Logger logger =
            LoggerFactory.getLogger(ValidacionLogin.class);

    private static final String MENSAJE_ESPERADO = "Logged In Successfully";

    public static ValidacionLogin validacionLogin() {
        return new ValidacionLogin();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            String texto =
                    Text.of(MENSAJE_LOGIN).answeredBy(actor).trim();

            logger.info("Texto encontrado: " + texto);
            return MENSAJE_ESPERADO.equalsIgnoreCase(texto);
        } catch (Exception e) {
            logger.error("No encontrado: " + e.getMessage());
            return false;
        }
    }
}
