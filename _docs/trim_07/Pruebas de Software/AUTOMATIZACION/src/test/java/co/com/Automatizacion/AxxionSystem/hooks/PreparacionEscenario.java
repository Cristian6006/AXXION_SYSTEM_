package co.com.Automatizacion.AxxionSystem.hooks;

import io.cucumber.java.Before;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;

public class PreparacionEscenario {

    @Before
    public void PreparacionEscenario() {
        OnStage.setTheStage(new OnlineCast());
    }
}
