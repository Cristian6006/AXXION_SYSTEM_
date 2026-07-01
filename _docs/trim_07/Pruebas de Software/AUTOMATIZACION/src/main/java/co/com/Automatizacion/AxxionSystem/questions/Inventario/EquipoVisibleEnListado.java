package co.com.Automatizacion.AxxionSystem.questions.Inventario;

import co.com.Automatizacion.AxxionSystem.userInterfaces.Inventario.InventarioUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class EquipoVisibleEnListado implements Question<Boolean> {
    private static final Logger logger = LoggerFactory.getLogger(EquipoVisibleEnListado.class);
    private final String nombre;

    public EquipoVisibleEnListado(String nombre) {
        this.nombre = nombre;
    }

    public static EquipoVisibleEnListado conNombre(String nombre) {
        return new EquipoVisibleEnListado(nombre);
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            WaitUntil.the(InventarioUI.NOMBRE_EQUIPO_EN_LISTADO.of(nombre), isVisible()).forNoMoreThan(10).seconds();
            String encontrado = Text.of(InventarioUI.NOMBRE_EQUIPO_EN_LISTADO.of(nombre)).answeredBy(actor).trim();
            logger.info("Buscando equipo: {}, encontrado: {}", nombre, encontrado);
            return encontrado.toLowerCase().contains(nombre.toLowerCase());
        } catch (Exception e) {
            logger.error("Equipo no visible en listado: {}", e.getMessage());
            return false;
        }
    }
}
