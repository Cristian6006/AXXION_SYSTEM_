package co.com.Automatizacion.AxxionSystem.tasks.Usuarios;

import co.com.Automatizacion.AxxionSystem.models.Usuarios.Usuario;
import co.com.Automatizacion.AxxionSystem.tasks.Categorias.LimpiarCampo;
import co.com.Automatizacion.AxxionSystem.userInterfaces.Usuarios.UsuariosUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.SelectFromOptions;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class EditarUsuario implements Task {
    private final Usuario usuario;

    public EditarUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public static EditarUsuario con(Usuario usuario) {
        return instrumented(EditarUsuario.class, usuario);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(UsuariosUI.BTN_ACTUALIZAR_USUARIO.of('@' + usuario.getNombreUsuario())),
                LimpiarCampo.con(
                        UsuariosUI.INPUT_PRIMER_NOMBRE_EDITAR,
                        usuario.getPrimerNombre()
                ),
                SelectFromOptions.byVisibleText("Activo").from(UsuariosUI.INPUT_ESTADO),
                Click.on(UsuariosUI.BOTON_CONFIRMAR_EDITAR)
        );
    }
}
