package co.com.Automatizacion.AxxionSystem.questions.Categorias;

import co.com.Automatizacion.AxxionSystem.models.Categorias.Categoria;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static co.com.Automatizacion.AxxionSystem.userInterfaces.Categorias.CategoriasUI.CATEGORIA_BUSQUEDA;

public class CategoriaFecha implements Question<Boolean> {
    private static final Logger logger =
            LoggerFactory.getLogger(CategoriaFecha.class);

    private final Categoria categoria;

    public CategoriaFecha(Categoria categoria) {
        this.categoria = categoria;
    }

    public static CategoriaFecha conFecha(Categoria categoria) {
        return new CategoriaFecha(categoria);
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            String fechaEsperada = categoria.getFecha();
            String fechaEncontrada = Text.of(CATEGORIA_BUSQUEDA.of(fechaEsperada)).answeredBy(actor).trim();
            logger.info("Esperado: {}", fechaEsperada);
            logger.info("Encontrado: {}", fechaEncontrada);
            return fechaEncontrada.equalsIgnoreCase(fechaEsperada);
        } catch (Exception e) {
            logger.error("la fecha no fue encontrada: {}", e.getMessage());
            return false;
        }
    }
}
