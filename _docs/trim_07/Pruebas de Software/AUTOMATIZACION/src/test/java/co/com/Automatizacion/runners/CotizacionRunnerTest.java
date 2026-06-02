package co.com.Automatizacion.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features/GenerarCotizacion.feature",
        glue = {"co.com.Automatizacion.stepdefinitions", "co.com.Automatizacion.utils.hooks"},
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class CotizacionRunnerTest {
}
