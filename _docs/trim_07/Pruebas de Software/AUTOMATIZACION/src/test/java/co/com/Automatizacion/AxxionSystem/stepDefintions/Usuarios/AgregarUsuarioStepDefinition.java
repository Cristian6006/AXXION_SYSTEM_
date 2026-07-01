package co.com.Automatizacion.AxxionSystem.stepDefintions.Usuarios;

import co.com.Automatizacion.AxxionSystem.models.Categorias.Categoria;
import co.com.Automatizacion.AxxionSystem.models.Usuarios.Usuario;
import co.com.Automatizacion.AxxionSystem.questions.Categorias.CategoriaExiste;
import co.com.Automatizacion.AxxionSystem.questions.Usuarios.UsuarioExiste;
import co.com.Automatizacion.AxxionSystem.stepDefintions.Usuarios.helper.UsuarioHelper;
import co.com.Automatizacion.AxxionSystem.tasks.Usuarios.AgregarUsuario;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;

public class AgregarUsuarioStepDefinition {
    @Cuando("el administrador crea un nuevo usuario")
    public void el_administrador_crea_un_nuevo_usuario() {
        Usuario usuarioActual = UsuarioHelper.crearUsuario();
        OnStage.theActorInTheSpotlight().attemptsTo(
                AgregarUsuario.con(usuarioActual)
        );
    }
    @Entonces("deberia ver el nuevo usuario en la lista")
    public void deberia_ver_el_nuevo_usuario_en_la_lista() {
        Actor actor = OnStage.theActorInTheSpotlight();
        Usuario usuarioActual = actor.recall("USUARIO");
        actor.should(
                seeThat(UsuarioExiste.enLaLista(usuarioActual))
        );
    }
}
