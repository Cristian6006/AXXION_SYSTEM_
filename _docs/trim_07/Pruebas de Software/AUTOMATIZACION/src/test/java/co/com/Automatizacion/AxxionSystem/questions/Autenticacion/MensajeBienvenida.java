package co.com.Automatizacion.AxxionSystem.questions.Autenticacion;

import co.com.Automatizacion.AxxionSystem.userInterfaces.PrincipalUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Visibility;

public class MensajeBienvenida implements Question<Boolean> {

    @Override
    public Boolean answeredBy(Actor actor) {
        return Visibility.of(PrincipalUI.MENSAJE_BIENVENIDA).answeredBy(actor);
    }

    public static MensajeBienvenida esVisible() {
        return new MensajeBienvenida();
    }
}
