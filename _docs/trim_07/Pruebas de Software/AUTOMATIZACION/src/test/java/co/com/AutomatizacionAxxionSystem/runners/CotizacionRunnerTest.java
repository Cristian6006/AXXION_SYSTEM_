package co.com.AutomatizacionAxxionSystem.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features/GenerarCotizacion.feature",
        glue = {"co.com.AutomatizacionAxxionSystem.stepdefinitions", "co.com.AutomatizacionAxxionSystem.utils.hooks"},
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class CotizacionRunnerTest {
}
