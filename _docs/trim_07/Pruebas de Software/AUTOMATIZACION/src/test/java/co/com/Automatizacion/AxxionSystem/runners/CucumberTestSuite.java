package co.com.Automatizacion.AxxionSystem.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features/Categorias",
        glue = "co/com/Automatizacion/AxxionSystem/stepDefintions",  
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class CucumberTestSuite {}

