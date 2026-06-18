package co.com.AutomatizacionAxxionSystem.interactions;

import co.com.AutomatizacionAxxionSystem.utils.CategoriaEquipoMapper;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Selecciona la categoría en el formulario del modal de agregar equipo.
 */
public class SeleccionarCategoriaEquipo implements Interaction {

    private static final By SELECT_CATEGORIA_MODAL =
            By.xpath("//select[.//option[normalize-space()='Seleccionar categoría']]");

    private final String categoria;

    public SeleccionarCategoriaEquipo(String categoria) {
        this.categoria = categoria;
    }

    public static Interaction conValor(String categoria) {
        return new SeleccionarCategoriaEquipo(categoria);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String opcion = CategoriaEquipoMapper.aOpcionSelect(categoria);
        var driver = actor.usingAbilityTo(BrowseTheWeb.class).getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        WebElement selectElement = wait.until(ExpectedConditions.presenceOfElementLocated(SELECT_CATEGORIA_MODAL));

        // Esperar a que las categorías del API estén cargadas (más de la opción placeholder)
        wait.until(d -> {
            Select select = new Select(selectElement);
            return select.getOptions().stream()
                    .map(WebElement::getText)
                    .filter(text -> text != null && !text.isBlank())
                    .filter(text -> !text.equalsIgnoreCase("Seleccionar categoría"))
                    .filter(text -> !text.toLowerCase().contains("cargando"))
                    .count() >= 1;
        });

        boolean opcionExiste = new Select(selectElement).getOptions().stream()
                .anyMatch(opt -> opcion.equalsIgnoreCase(opt.getText().trim()));

        if (!opcionExiste) {
            throw new IllegalStateException(
                    "La categoría '" + opcion + "' no está disponible en el select del formulario");
        }

        new Select(selectElement).selectByVisibleText(opcion);
    }
}
