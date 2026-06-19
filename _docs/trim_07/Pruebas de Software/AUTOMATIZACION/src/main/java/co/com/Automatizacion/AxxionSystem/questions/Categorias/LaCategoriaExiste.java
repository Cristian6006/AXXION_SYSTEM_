package co.com.Automatizacion.AxxionSystem.questions.Categorias;

import co.com.Automatizacion.AxxionSystem.userInterfaces.CategoriasUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LaCategoriaExiste implements Question<Boolean> {
    private static final Logger logger =
            LoggerFactory.getLogger(LaCategoriaExiste.class);

    private final String nombreCategoria;

    public LaCategoriaExiste(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            String texto = Text.of(CategoriasUI.RESULTADO_CATEGORIAS).answeredBy(actor);
            logger.info("Texto capturado: {}", texto);
            return CategoriasUI.RESULTADO_CATEGORIAS.of(nombreCategoria).resolveFor(actor).isVisible();
        } catch (Exception e) {
            logger.error("El mensaje de guardado no fue encontrado: {}", e.getMessage());
            return false;
        }
    }

    public static LaCategoriaExiste enLaLista(String nombre) {
        return new LaCategoriaExiste(nombre);
    }
}
