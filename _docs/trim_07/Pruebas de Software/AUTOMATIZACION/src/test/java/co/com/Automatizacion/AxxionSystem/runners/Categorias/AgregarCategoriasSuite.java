package co.com.Automatizacion.AxxionSystem.runners.Categorias;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features/Categorias/BorrarCategoria.feature",
        glue = "co/com/Automatizacion/AxxionSystem/stepDefintions/Categorias",
        snippets = CucumberOptions.SnippetType.CAMELCASE
)

public class AgregarCategoriasSuite {}
