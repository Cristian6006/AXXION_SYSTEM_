package co.com.Automatizacion.AxxionSystem.userInterfaces.Inventario;

import net.serenitybdd.screenplay.targets.Target;

public class InventarioUI {
    public static final Target TITULO_INVENTARIO = Target.the("Titulo gestion inventario")
            .locatedBy("//h1[contains(.,'Gestión de Inventario')]");

    public static final Target BOTON_AGREGAR_EQUIPO = Target.the("Boton agregar equipo")
            .locatedBy("//main//button[contains(.,'Agregar Equipo')]");

    public static final Target INPUT_BUSQUEDA = Target.the("Campo busqueda equipos")
            .locatedBy("//input[@placeholder='Buscar por nombre, modelo o serie...']");

    public static final Target MODAL_FORMULARIO = Target.the("Modal formulario equipo")
            .locatedBy("//div[contains(@class,'fixed') and .//input[@placeholder='Ej: Laptop Dell Inspiron 15']]");

    public static final Target INPUT_NOMBRE = Target.the("Campo nombre equipo")
            .locatedBy("//div[contains(@class,'fixed')]//input[@placeholder='Ej: Laptop Dell Inspiron 15']");

    public static final Target INPUT_MARCA = Target.the("Campo marca equipo")
            .locatedBy("//div[contains(@class,'fixed')]//input[@placeholder='Ej: Dell, HP, Apple']");

    public static final Target INPUT_MODELO = Target.the("Campo modelo equipo")
            .locatedBy("//div[contains(@class,'fixed')]//input[@placeholder='Ej: Inspiron 15 3000']");

    public static final Target INPUT_SERIE = Target.the("Campo serie equipo")
            .locatedBy("//div[contains(@class,'fixed')]//input[@placeholder='Ej: DL001234567']");

    public static final Target SELECT_CATEGORIA = Target.the("Select categoria equipo")
            .locatedBy("//div[contains(@class,'fixed')]//select[.//option[contains(.,'Seleccionar categoría') or contains(.,'Video') or contains(.,'Sonido')]]");

    public static final Target SELECT_ESTADO = Target.the("Select estado equipo")
            .locatedBy("//div[contains(@class,'fixed')]//select[.//option[@value='disponible']]");

    public static final Target INPUT_TARIFA_DIARIA = Target.the("Campo tarifa diaria")
            .locatedBy("//div[contains(@class,'fixed')]//input[@placeholder='25.00']");

    public static final Target BOTON_GUARDAR_EQUIPO = Target.the("Boton guardar equipo en modal")
            .locatedBy("//div[contains(@class,'fixed')]//form//button[contains(.,'Agregar Equipo') or contains(.,'Actualizar Equipo')]");

    public static final Target NOMBRE_EQUIPO_EN_LISTADO = Target.the("Nombre equipo {0} en listado")
            .locatedBy("//h3[contains(normalize-space(.), '{0}')]");

    public static final Target BOTON_ELIMINAR_EN_TARJETA = Target.the("Boton eliminar equipo {0}")
            .locatedBy("//h3[contains(normalize-space(.), '{0}')]/ancestor::div[contains(@class,'equipment-card') or contains(@class,'p-6')]//button[contains(.,'Eliminar')]");

    public static final Target BOTON_ALQUILAR_EN_TARJETA = Target.the("Boton alquilar equipo {0}")
            .locatedBy("//h3[contains(normalize-space(.), '{0}')]/ancestor::div[contains(@class,'equipment-card') or contains(@class,'p-6')]//button[contains(.,'Alquilar')]");

    public static final Target MODAL_ELIMINAR = Target.the("Modal confirmacion eliminar")
            .locatedBy("//div[contains(@class,'fixed') and .//h3[contains(.,'eliminar este equipo')]]");

    public static final Target BOTON_CONFIRMAR_ELIMINAR = Target.the("Boton confirmar eliminacion")
            .locatedBy("//div[contains(@class,'fixed') and .//h3[contains(.,'eliminar este equipo')]]//button[contains(.,'Eliminar')]");

    public static final Target BOTON_LIMPIAR_FILTROS = Target.the("Boton limpiar filtros")
            .locatedBy("//button[contains(.,'Limpiar Filtros')]");

    public static final Target PRIMER_EQUIPO_LISTADO = Target.the("Primer equipo del listado")
            .locatedBy("(//div[contains(@class,'equipment-card')]//h3)[1]");
}
