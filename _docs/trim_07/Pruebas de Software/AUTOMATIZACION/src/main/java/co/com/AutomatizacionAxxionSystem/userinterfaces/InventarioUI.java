package co.com.AutomatizacionAxxionSystem.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

/**
 * Clase que contiene todos los localizadores (Targets) de la pantalla
 * de Gestión de Inventario de Axxion System.
 * Sigue el patrón Page Object dentro del modelo Screenplay.
 */
public class InventarioUI {

    // ─── Módulo de Inventario ─────────────────────────────────────────────────

    /** Enlace del sidebar para ir al módulo de Inventario */
    public static final Target LINK_INVENTARIO =
            Target.the("Enlace de Inventario en sidebar")
                    .located(By.xpath("//a[contains(@href, 'Inventory') or contains(@href, 'inventory')]"));

    /** Título H1 de la página de Gestión de Inventario */
    public static final Target TITULO_INVENTARIO =
            Target.the("Título Gestión de Inventario")
                    .located(By.xpath("//h1[contains(., 'Gestión de Inventario')]"));

    /** Botón principal "Agregar Equipo" */
    public static final Target BTN_AGREGAR_EQUIPO =
            Target.the("Botón Agregar Equipo")
                    .located(By.xpath("//button[contains(., 'Agregar Equipo') and not(@type='submit')]"));

    /** Campo de búsqueda global de equipos */
    public static final Target INPUT_BUSQUEDA =
            Target.the("Campo de búsqueda de equipos")
                    .located(By.xpath("//input[@placeholder='Buscar por nombre, modelo o serie...']"));

    /** Botón "Actualizar Vista" para forzar recarga de datos del API */
    public static final Target BTN_ACTUALIZAR_VISTA =
            Target.the("Botón Actualizar Vista")
                    .located(By.xpath("//button[contains(., 'Actualizar Vista')]"));

    /** Overlay de carga mientras se obtienen los equipos */
    public static final Target OVERLAY_CARGA =
            Target.the("Overlay de carga de inventario")
                    .located(By.cssSelector("main .overlay"));

    // ─── Modal de Agregar / Editar Equipo ─────────────────────────────────────

    /** Encabezado del modal "Agregar Nuevo Equipo" */
    public static final Target MODAL_TITULO =
            Target.the("Título del modal de agregar equipo")
                    .located(By.xpath("//div[contains(@class,'font-semibold') and contains(., 'Agregar Nuevo Equipo')]"));

    /** Input: Nombre del Equipo */
    public static final Target INPUT_NOMBRE_EQUIPO =
            Target.the("Input Nombre del Equipo")
                    .located(By.xpath("//form[contains(@class,'space-y-6')]"
                            + "//label[contains(normalize-space(.), 'Nombre del Equipo')]/following::input[1]"));

    /** Input: Marca */
    public static final Target INPUT_MARCA =
            Target.the("Input Marca del equipo")
                    .located(By.xpath("//form[contains(@class,'space-y-6')]"
                            + "//label[contains(normalize-space(.), 'Marca') and not(contains(., 'Tarjeta'))]"
                            + "/following::input[1]"));

    /** Input: Modelo */
    public static final Target INPUT_MODELO =
            Target.the("Input Modelo del equipo")
                    .located(By.xpath("//form[contains(@class,'space-y-6')]"
                            + "//label[contains(normalize-space(.), 'Modelo')]/following::input[1]"));

    /** Input: Número de Serie */
    public static final Target INPUT_SERIE =
            Target.the("Input Número de Serie")
                    .located(By.xpath("//form[contains(@class,'space-y-6')]"
                            + "//label[contains(normalize-space(.), 'Número de Serie')]/following::input[1]"));

    /**
     * Select de Categoría (el primero de los tres selects del modal).
     * Orden en el DOM: Categoría → Estado → Condición.
     */
    public static final Target SELECT_CATEGORIA =
            Target.the("Select Categoría del equipo")
                    .located(By.xpath("//form[contains(@class,'space-y-6')]//select[1]"));

    /** Input: Tarifa Diaria (bloque financiero del formulario) */
    public static final Target INPUT_TARIFA_DIARIA =
            Target.the("Input Tarifa Diaria")
                    .located(By.xpath("//form[contains(@class,'space-y-6')]"
                            + "//label[contains(normalize-space(.), 'Tarifa Diaria')]/following::input[@type='number'][1]"));

    /** Botón "Agregar Equipo" dentro del modal (submit) */
    public static final Target BTN_SUBMIT_MODAL =
            Target.the("Botón Agregar Equipo en modal")
                    .located(By.xpath("//form[contains(@class,'space-y-6')]//button[@type='submit']"));

    // ─── Validaciones post-acción ──────────────────────────────────────────────

    /**
     * Alerta de éxito que aparece en la vista de Inventario al agregar un equipo.
     * Busca el badge/alerta con texto "Equipo Agregado".
     */
    public static final Target ALERTA_EXITO_EQUIPO_AGREGADO =
            Target.the("Alerta de éxito 'Equipo Agregado'")
                    .located(By.xpath("//*[contains(., 'Equipo Agregado')]"));

    /**
     * Primer heading h3 en la grilla de equipos (primera tarjeta del listado).
     * Usado para verificar que el equipo recién agregado aparece al tope.
     */
    public static final Target PRIMER_EQUIPO_EN_LISTA =
            Target.the("Primer equipo en la lista")
                    .located(By.xpath("(//div[contains(@class,'equipment-card')]//h3[contains(@class,'text-lg')])[1]"));

    /**
     * Localizador dinámico: encuentra un equipo en la grilla por su nombre exacto.
     * Uso: InventarioUI.EQUIPO_POR_NOMBRE.of("Proyector portátil")
     */
    public static final Target EQUIPO_POR_NOMBRE =
            Target.the("Equipo '{0}' en la lista")
                    .locatedBy("//h3[normalize-space(.)='{0}']");

    /**
     * Localizador dinámico: verifica que el criterio de búsqueda aparece en al menos
     * un heading h3 de la grilla (nombre del equipo).
     */
    public static final Target EQUIPO_CON_TEXTO =
            Target.the("Equipo que contiene el texto '{0}'")
                    .locatedBy("//h3[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '{0}')]");

    /**
     * Botón Alquilar dentro de la tarjeta cuyo título contiene el nombre indicado.
     */
    public static final Target BTN_ALQUILAR_EN_TARJETA =
            Target.the("Botón Alquilar del equipo '{0}'")
                    .locatedBy("//h3[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), "
                            + "translate('{0}', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'))]"
                            + "/ancestor::div[contains(@class,'equipment-card') or contains(@class,'p-6')][1]"
                            + "//button[contains(., 'Alquilar')]");

    /** Todos los nombres de equipo visibles en la grilla */
    public static final Target TARJETAS_EQUIPO_VISIBLES =
            Target.the("Tarjetas de equipos visibles")
                    .located(By.xpath("//main//h3[contains(@class,'text-lg')]"));

    /** Contenedor de tarjeta por nombre parcial (para validar marca/serie en búsqueda) */
    public static final Target TARJETA_EQUIPO_POR_NOMBRE =
            Target.the("Tarjeta del equipo '{0}'")
                    .locatedBy("//h3[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), "
                            + "translate('{0}', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'))]"
                            + "/ancestor::div[contains(@class,'equipment-card') or contains(@class,'p-6')][1]");

    private InventarioUI() {
    }
}
