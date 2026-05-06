package co.com.Automatizacion.AxxionSystem.runners.Autenticacion;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features/Autenticacion",
        glue = "co/com/Automatizacion/AxxionSystem/stepDefintions/Autenticacion",
        snippets = CucumberOptions.SnippetType.CAMELCASE
)

public class AutenticacionSuite {}
