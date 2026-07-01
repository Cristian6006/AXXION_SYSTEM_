package co.com.Automatizacion.AxxionSystem.tasks.Usuarios;

import co.com.Automatizacion.AxxionSystem.models.Usuarios.Usuario;
import co.com.Automatizacion.AxxionSystem.questions.Usuarios.UsuarioExiste;
import co.com.Automatizacion.AxxionSystem.userInterfaces.Usuarios.UsuariosUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class BorrarUsuario implements Task {
    private static final Logger logger =
            LoggerFactory.getLogger(BorrarUsuario.class);

    private final Usuario usuario;

    public BorrarUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public static BorrarUsuario con(Usuario usuario) {
        return instrumented(BorrarUsuario.class, usuario);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(UsuariosUI.BTN_ELIMINAR_USUARIO.of(usuario.getFullName())),
                WaitUntil.the(UsuariosUI.CHECKBOX_CONFIRMAR_ELIMINACION,
                                WebElementStateMatchers.isVisible())
                        .forNoMoreThan(5)
                        .seconds(),
                Click.on(UsuariosUI.CHECKBOX_CONFIRMAR_ELIMINACION),
                WaitUntil.the(
                        UsuariosUI.BOTON_CONFIRMAR_BORRAR,
                        WebElementStateMatchers.isClickable()
                ).forNoMoreThan(5).seconds(),
                Click.on(UsuariosUI.BOTON_CONFIRMAR_BORRAR)
        );
        logger.info(
                "Botones encontrados: {}",
                UsuariosUI.BOTON_CONFIRMAR_BORRAR.resolveAllFor(actor).size()
        );

    }
}
