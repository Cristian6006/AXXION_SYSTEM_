package co.com.Automatizacion.AxxionSystem.tasks.Usuarios;

import co.com.Automatizacion.AxxionSystem.models.Usuarios.Usuario;
import co.com.Automatizacion.AxxionSystem.userInterfaces.Usuarios.UsuariosUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.SelectFromOptions;
import net.serenitybdd.screenplay.questions.*;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class AgregarUsuario implements Task {
    private final Usuario usuario;

    public AgregarUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public static AgregarUsuario con(Usuario usuario) {
        return instrumented(AgregarUsuario.class, usuario);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(UsuariosUI.BOTON_AGREGAR_USUARIO),
                Enter.theValue(usuario.getPrimerNombre()).into(UsuariosUI.INPUT_PRIMER_NOMBRE),
                Enter.theValue(usuario.getSegundoNombre()).into(UsuariosUI.INPUT_SEGUNDO_NOMBRE),
                Enter.theValue(usuario.getPrimerApellido()).into(UsuariosUI.INPUT_PRIMER_APELLIDO),
                Enter.theValue(usuario.getSegundoApellido()).into(UsuariosUI.INPUT_SEGUNDO_APELLIDO),
                Enter.theValue(usuario.getNombreUsuario()).into(UsuariosUI.INPUT_NOMBRE_USUARIO),
                Enter.theValue(usuario.getEmail()).into(UsuariosUI.INPUT_EMAIL),
                Enter.theValue(usuario.getTelefono()).into(UsuariosUI.INPUT_TELEFONO),
                SelectFromOptions.byVisibleText("Ventas").from(UsuariosUI.INPUT_DEPARTAMENTO),
                Enter.theValue(usuario.getClave()).into(UsuariosUI.INPUT_CLAVE),
                Click.on(UsuariosUI.INPUT_ROL),
                Click.on(UsuariosUI.BOTON_CREAR)
        );
    }
}
