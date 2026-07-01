package co.com.Automatizacion.AxxionSystem.questions.Usuarios;

import co.com.Automatizacion.AxxionSystem.models.Usuarios.Usuario;
import co.com.Automatizacion.AxxionSystem.userInterfaces.Categorias.CategoriasUI;
import co.com.Automatizacion.AxxionSystem.userInterfaces.Usuarios.UsuariosUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class UsuarioExiste implements Question<Boolean> {
    private static final Logger logger =
            LoggerFactory.getLogger(UsuarioExiste.class);

    private final Usuario usuario;

    public UsuarioExiste(Usuario usuario) {
        this.usuario = usuario;
    }

    public static UsuarioExiste enLaLista(Usuario usuario) {
        return new UsuarioExiste(usuario);
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            String nombreEsperado = usuario.getFullName().trim();
            WaitUntil.the(UsuariosUI.RESULTADO_USUARIO.of(nombreEsperado), isVisible()).forNoMoreThan(5).seconds();
            String nombreEncontrado = Text.of(UsuariosUI.RESULTADO_USUARIO.of(nombreEsperado)).answeredBy(actor).trim();
            logger.info("Esperado: {}", nombreEsperado);
            logger.info("Encontrado: {}", nombreEncontrado);
            return nombreEsperado.equalsIgnoreCase(nombreEncontrado);
        } catch (Exception e) {
            logger.error("La categoria no fue encontrado: {}", e.getMessage());
            return false;
        }
    }
}
