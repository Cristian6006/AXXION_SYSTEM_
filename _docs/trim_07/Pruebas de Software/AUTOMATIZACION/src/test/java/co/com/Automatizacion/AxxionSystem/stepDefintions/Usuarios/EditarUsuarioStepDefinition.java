package co.com.Automatizacion.AxxionSystem.stepDefintions.Usuarios;

import co.com.Automatizacion.AxxionSystem.factory.Categoria.CategoriaFactory;
import co.com.Automatizacion.AxxionSystem.factory.Usuario.UsuarioFactory;
import co.com.Automatizacion.AxxionSystem.models.Usuarios.Usuario;
import co.com.Automatizacion.AxxionSystem.questions.Categorias.CategoriaExiste;
import co.com.Automatizacion.AxxionSystem.questions.Usuarios.UsuarioExiste;
import co.com.Automatizacion.AxxionSystem.tasks.Categorias.EditarCategoria;
import co.com.Automatizacion.AxxionSystem.tasks.Usuarios.EditarUsuario;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;

public class EditarUsuarioStepDefinition {
    private Usuario updateUser;

    @Cuando("el administrador modifica la informacion de un usuario existente en el sistema.")
    public void el_administrador_modifica_la_informacion_de_un_usuario_existente_en_el_sistema() {
        Actor actor = OnStage.theActorInTheSpotlight();
        Usuario usuarioActual = actor.recall("USUARIO");
        updateUser = UsuarioFactory.updateUser(usuarioActual);
        actor.attemptsTo(EditarUsuario.con(updateUser));
    }
    @Entonces("deberia los datos del usuario actualizados")
    public void deberia_los_datos_del_usuario_actualizados() {
        Actor actor = OnStage.theActorInTheSpotlight();
        actor.should(seeThat(UsuarioExiste.enLaLista(updateUser)));
    }
}
