package co.com.Automatizacion.AxxionSystem.runners.Categorias;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "classpath:features",
        glue = "co.com.Automatizacion.AxxionSystem",
        tags = "@EditarCategoria",
        snippets = CucumberOptions.SnippetType.CAMELCASE
)

public class EditarCategoriaSuite {}
