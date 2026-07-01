package co.com.Automatizacion.AxxionSystem.stepDefintions.Usuarios;

import co.com.Automatizacion.AxxionSystem.models.Usuarios.Usuario;
import co.com.Automatizacion.AxxionSystem.questions.Usuarios.UsuarioExiste;
import co.com.Automatizacion.AxxionSystem.tasks.Usuarios.ConsultarUsuario;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;

public class ConsultarUsuarioStepDefinition {
    @Cuando("ingresa el nombre del usuario en el buscador")
    public void ingresa_el_nombre_del_usuario_en_el_buscador() {
        Actor actor = OnStage.theActorInTheSpotlight();
        Usuario usuarioActual = actor.recall("USUARIO");
        actor.attemptsTo(ConsultarUsuario.conNombre(usuarioActual));
    }
    @Entonces("deberia ver unicamente el usuario con su nombre")
    public void deberia_ver_unicamente_el_usuario_con_su_nombre() {
        Actor actor = OnStage.theActorInTheSpotlight();
        Usuario usuarioActual = actor.recall("USUARIO");
        actor.should(
                seeThat(UsuarioExiste.enLaLista(usuarioActual))
        );
    }
}
