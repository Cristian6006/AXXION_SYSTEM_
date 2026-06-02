package co.com.Automatizacion.utils.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import net.serenitybdd.screenplay.actors.OnlineCast;

import static net.serenitybdd.screenplay.actors.OnStage.drawTheCurtain;
import static net.serenitybdd.screenplay.actors.OnStage.setTheStage;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;

public class PreparacionEscenario {

    @Before
    public void setup() {
        setTheStage(new OnlineCast());
        theActorCalled("usuario");
    }

    @After
    public void tearDown() {
        drawTheCurtain();
    }
}
