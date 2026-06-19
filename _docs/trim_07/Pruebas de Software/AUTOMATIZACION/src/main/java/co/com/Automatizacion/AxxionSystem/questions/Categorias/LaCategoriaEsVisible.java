package co.com.Automatizacion.AxxionSystem.questions.Categorias;

import co.com.Automatizacion.AxxionSystem.userInterfaces.CategoriasUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.questions.Visibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LaCategoriaEsVisible implements Question<Boolean> {
    private static final Logger logger =
            LoggerFactory.getLogger(LaCategoriaEsVisible.class);

    private final String categoriaBuscada;

    public LaCategoriaEsVisible(String categoriaBuscada) {
        this.categoriaBuscada = categoriaBuscada;
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            String texto = Text.of(CategoriasUI.RESULTADO_CATEGORIAS).answeredBy(actor);
            logger.info("Texto capturado: {}", texto);
            return Visibility.of(CategoriasUI.RESULTADO_CATEGORIAS.of(categoriaBuscada)).answeredBy(actor);
        } catch (Exception e) {
            logger.error("El mensaje de guardado no fue encontrado: {}", e.getMessage());
            return false;
        }
    }

    public static LaCategoriaEsVisible enLosResultados(String categoriaBuscada) {
        return new LaCategoriaEsVisible(categoriaBuscada);
    }
}
