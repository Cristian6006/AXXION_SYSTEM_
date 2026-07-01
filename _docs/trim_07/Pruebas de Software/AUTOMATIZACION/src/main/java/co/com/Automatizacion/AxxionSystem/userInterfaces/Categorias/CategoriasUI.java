package co.com.Automatizacion.AxxionSystem.userInterfaces.Categorias;

import net.serenitybdd.screenplay.targets.Target;

public class CategoriasUI {
    // Comunes
    public static final Target MENSAJE_GUARDADO = Target.the("Mensaje de Guaradado").locatedBy("//h2[@id='swal2-title']");
    public static final Target INPUT_NOMBRE = Target.the("Campo Nombre Categoria").locatedBy("//label[contains(., 'Nombre')]/following-sibling::input");
    public static final Target INPUT_TIPO_CATEGORIA = Target.the("Campo Tipo Categoria").locatedBy("//label[contains(., 'Tipo de categoria')]/following-sibling::input");
    public static final Target INPUT_DESCRIPCION = Target.the("Campo Descripcion").locatedBy("//label[contains(., 'Descrip')]/following-sibling::textarea");
    public static final Target RESULTADO_CATEGORIAS = Target.the("Resultado de gestion de categorias").locatedBy("//h3[text()[normalize-space(.)='{0}']]");

    // Agregar Categoria
    public static final Target BOTON_AGREGAR_CATEGORIAS = Target.the("Boton Agregar Categoria").locatedBy("//span[contains(., 'Agregar Categoria')]");
    public static final Target BOTON_CREAR = Target.the("Boton Guardar").locatedBy("//button[contains(., 'Crear')]");

    // Borrar Categoria
    public static final Target BOTON_BORRAR_CATEGORIA = Target.the("Botón Borrar de la categoría {0}").locatedBy("//div[contains(@class, 'justify-between') and .//h3[contains(., '{0}')]]//button[2]");
    public static final Target BOTON_CONFIRMAR_BORRAR = Target.the("Boton Confirmacion Borrado").locatedBy("//button[contains(., 'Sí, Eliminar')]");

    // Editar Categoria
    public static final Target BOTON_EDITAR_CATEGORIA = Target.the("Botón Borrar de la categoría {0}")
            .locatedBy("//div[contains(@class, 'justify-between') and .//h3[contains(., '{0}')]]//button[1]");
    public static final Target BOTON_CONFIRMAR_EDITAR = Target.the("Boton Confirmacion Edicion").locatedBy("//button[contains(., 'Guardar Cambios')]");

    // Consultar Categoria
    public static final Target INPUT_BUSQUEDA_NOMBRE = Target.the("Buscador de categoria por nombre").locatedBy("//input[@placeholder='Buscar Categorias']");
    public static final Target INPUT_BUSQUEDA_FECHA = Target.the("Buscador de categoria por fecha").locatedBy("//div[descendant::input[@type='date']]");
    public static final Target CATEGORIA_BUSQUEDA = Target.the("categoria buscada").locatedBy("//div[contains(@class, 'bg-[#364153]') and contains(., '{0}')]");
}
