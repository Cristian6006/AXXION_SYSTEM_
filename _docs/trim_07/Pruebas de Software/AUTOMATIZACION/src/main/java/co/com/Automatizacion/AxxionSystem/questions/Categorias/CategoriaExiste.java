package co.com.Automatizacion.AxxionSystem.questions.Categorias;

import co.com.Automatizacion.AxxionSystem.models.Categorias.Categoria;
import co.com.Automatizacion.AxxionSystem.userInterfaces.Categorias.CategoriasUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class CategoriaExiste implements Question<Boolean> {
    private static final Logger logger =
            LoggerFactory.getLogger(CategoriaExiste.class);

    private final Categoria categoria;

    public CategoriaExiste(Categoria categoria) {
        this.categoria = categoria;
    }

    public static CategoriaExiste enLaLista(Categoria categoria) {
        return new CategoriaExiste(categoria);
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            String categoriaNombre = categoria.getNombre().trim();
            String categoriaEsperada = categoria.getFullCategory().trim();
            WaitUntil.the(CategoriasUI.RESULTADO_CATEGORIAS, isVisible()).forNoMoreThan(5).seconds();
            String categoriaEncontrada = Text.of(CategoriasUI.RESULTADO_CATEGORIAS.of(categoriaNombre)).answeredBy(actor).replaceAll("\\s+", " ").trim();;
            logger.info("Esperado: {}", categoriaEsperada);
            logger.info("Encontrado: {}", categoriaEncontrada);
            return categoriaEsperada.equalsIgnoreCase(categoriaEncontrada);
        } catch (Exception e) {
            logger.error("La categoria no fue encontrado: {}", e.getMessage());
            return false;
        }
    }
}
