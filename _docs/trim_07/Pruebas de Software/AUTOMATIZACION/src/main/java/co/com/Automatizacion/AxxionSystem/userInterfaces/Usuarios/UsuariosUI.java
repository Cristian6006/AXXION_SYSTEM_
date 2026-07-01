package co.com.Automatizacion.AxxionSystem.userInterfaces.Usuarios;

import net.serenitybdd.screenplay.targets.Target;
import net.serenitybdd.screenplay.targets.TargetBuilder;
import org.openqa.selenium.By;

public class UsuariosUI {
    //Agregar
    public static final Target INPUT_PRIMER_NOMBRE = Target.the("Campo de nombre").located(By.id("nombre"));
    public static final Target INPUT_SEGUNDO_NOMBRE = Target.the("Campo de nombre").located(By.id("nombre2"));
    public static final Target INPUT_PRIMER_APELLIDO = Target.the("Campo de apellido").located(By.id("apellido1"));
    public static final Target INPUT_SEGUNDO_APELLIDO = Target.the("Campo de apellido").located(By.id("apellido2"));
    public static final Target INPUT_NOMBRE_USUARIO = Target.the("Campo de nombre usuario").located(By.id("nombre_usuario"));
    public static final Target INPUT_EMAIL = Target.the("Campo de correo").located(By.id("email"));
    public static final Target INPUT_TELEFONO = Target.the("Campo de telefono").located(By.id("telefono"));
    public static final Target INPUT_DEPARTAMENTO = Target.the("Campo de departamento").located(By.id("departamento"));
    public static final Target INPUT_CLAVE = Target.the("Campo de password").located(By.id("password"));
    public static final Target INPUT_ESTADO = Target.the("Campo de correo").located(By.id("estado_update"));
    public static final Target INPUT_ROL = Target.the("Campo de rol").located(By.id("role-4"));
    public static final Target RESULTADO_USUARIO = Target.the("Resultado de gestion de categorias").locatedBy("//h1[text()[normalize-space(.)='{0}']]");
    public static final Target BOTON_AGREGAR_USUARIO = Target.the("Boton Agregar Categoria").locatedBy("//span[contains(., ' Agregar Usuario ')]");
    public static final Target BOTON_CREAR = Target.the("Boton Guardar").locatedBy("//button[@type='submit']");

    // Borrar
    public static final Target BTN_ELIMINAR_USUARIO =
            Target.the("Botón eliminar usuario")
                    .locatedBy(
                            "//div[contains(@class,'w-80')][.//h1[normalize-space()='{0}']]//button[@title='Eliminar usuario']"
                    );
    public static final Target BOTON_CONFIRMAR_BORRAR = Target.the("Boton Confirmacion Borrado").locatedBy("//div[contains(@class,'fixed')]//form//button[@type='submit']");
    public static final Target CHECKBOX_CONFIRMAR_ELIMINACION = Target.the("Checkbox de confirmación de eliminación").locatedBy("//label[.//span[normalize-space()='Confirmo que quiero eliminar este usuario permanentemente']]//input");

    // Editar
    public static final Target BTN_ACTUALIZAR_USUARIO =
            Target.the("Botón actualizar usuario")
                    .locatedBy(
                            "//div[contains(@class,'w-80')][.//p[normalize-space()='{0}']]//button[@title='Actualizar usuario']"
                    );
    public static final Target BOTON_CONFIRMAR_EDITAR = Target.the("Boton Confirmacion Edicion").locatedBy("//form//button[@type='submit'][.//span[normalize-space()='Actualizar Usuario']]");
    public static final Target INPUT_PRIMER_NOMBRE_EDITAR = Target.the("Campo de nombre").located(By.id("nombre_update"));

    // Consultar
    public static final Target INPUT_BUSQUEDA_NOMBRE = Target.the("Buscador de categoria por nombre").locatedBy("//input[@placeholder='Buscar por nombre o email...']");
    public static final Target USUARIO_BUSQUEDA = Target.the("categoria buscada").locatedBy("//div[contains(@class, 'bg-[#364153]') and contains(., '{0}')]");
}
