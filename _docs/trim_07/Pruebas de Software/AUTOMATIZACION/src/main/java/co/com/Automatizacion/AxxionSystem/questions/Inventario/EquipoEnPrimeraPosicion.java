package co.com.Automatizacion.AxxionSystem.questions.Inventario;

import co.com.Automatizacion.AxxionSystem.userInterfaces.Inventario.InventarioUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class EquipoEnPrimeraPosicion implements Question<Boolean> {
    private static final Logger logger = LoggerFactory.getLogger(EquipoEnPrimeraPosicion.class);
    private final String nombre;

    public EquipoEnPrimeraPosicion(String nombre) {
        this.nombre = nombre;
    }

    public static EquipoEnPrimeraPosicion conNombre(String nombre) {
        return new EquipoEnPrimeraPosicion(nombre);
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            WaitUntil.the(InventarioUI.PRIMER_EQUIPO_LISTADO, isVisible()).forNoMoreThan(10).seconds();
            String primerEquipo = Text.of(InventarioUI.PRIMER_EQUIPO_LISTADO).answeredBy(actor).trim();
            logger.info("Primer equipo esperado: {}, encontrado: {}", nombre, primerEquipo);
            return primerEquipo.toLowerCase().contains(nombre.toLowerCase());
        } catch (Exception e) {
            logger.error("No se pudo validar la primera posicion del listado: {}", e.getMessage());
            return false;
        }
    }
}
