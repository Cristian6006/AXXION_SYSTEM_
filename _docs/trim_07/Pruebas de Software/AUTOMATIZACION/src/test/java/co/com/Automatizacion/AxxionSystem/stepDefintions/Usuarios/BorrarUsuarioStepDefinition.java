package co.com.Automatizacion.AxxionSystem.stepDefintions.Usuarios;

import co.com.Automatizacion.AxxionSystem.models.Usuarios.Usuario;
import co.com.Automatizacion.AxxionSystem.questions.Usuarios.UsuarioExiste;
import co.com.Automatizacion.AxxionSystem.tasks.Usuarios.BorrarUsuario;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.is;

public class BorrarUsuarioStepDefinition {
    @Cuando("elimina a el usuario del sistema")
    public void elimina_a_el_usuario_del_sistema() {
        Actor actor = OnStage.theActorInTheSpotlight();
        Usuario usuarioActual = actor.recall("USUARIO");
        actor.attemptsTo(BorrarUsuario.con(usuarioActual));
    }
    @Entonces("deberia ver que el usuario no exista en la lista")
    public void deberia_ver_que_el_usuario_no_exista_en_la_lista() {
        Actor actor = OnStage.theActorInTheSpotlight();
        Usuario usuarioActual = actor.recall("USUARIO");
        actor.should(
                seeThat(
                        "La ausencia en la lista de: " + usuarioActual,
                        UsuarioExiste.enLaLista(usuarioActual),
                        is(false))
        );
    }
}
