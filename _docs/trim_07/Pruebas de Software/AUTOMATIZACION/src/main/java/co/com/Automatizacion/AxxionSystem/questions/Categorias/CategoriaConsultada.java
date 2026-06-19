package co.com.Automatizacion.AxxionSystem.questions.Categorias;

import co.com.Automatizacion.AxxionSystem.userInterfaces.CategoriasUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static co.com.Automatizacion.AxxionSystem.userInterfaces.CategoriasUI.CATEGORIA_BUSQUEDA;
// import static co.com.Automatizacion.AxxionSystem.userInterfaces.CategoriasUI.MENSAJE_GUARDADO;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class CategoriaConsultada implements Question<String> {
    private static final Logger logger =
            LoggerFactory.getLogger(CategoriaConsultada.class);

    public static CategoriaConsultada categoriaConsultada() {
        return new CategoriaConsultada();
    }

    @Override
    public String  answeredBy(Actor actor) {
        try {
            WaitUntil.the(CategoriasUI.CATEGORIA_BUSQUEDA, isVisible()).forNoMoreThan(5).seconds();
            String texto = Text.of(CATEGORIA_BUSQUEDA).answeredBy(actor);
            logger.info("Texto capturado: {}", texto);
            return Text.of(CategoriasUI.CATEGORIA_BUSQUEDA).answeredBy(actor).trim();
        } catch (Exception e){
            logger.error("El mensaje de guardado no fue encontrado: {}", e.getMessage());
            return e.getMessage();
        }
    }
}
