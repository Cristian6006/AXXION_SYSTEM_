package co.com.AutomatizacionAxxionSystem.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

/**
 * Clase que contiene todos los localizadores (Targets) del flujo
 * de Cotización de Axxion System.
 * Incluye el CartDrawer y la vista de detalles de cotización.
 */
public class CotizacionUI {

    // ─── Carrito de Cotización (CartDrawer) ───────────────────────────────────

    /** Botón "Alquilar" en una tarjeta de equipo disponible */
    public static final Target BTN_ALQUILAR_EQUIPO =
            Target.the("Botón Alquilar en tarjeta de equipo")
                    .located(By.xpath("//button[contains(., 'Alquilar')]"));

    /** Botón "Ver Carrito" que aparece cuando hay items en el carrito */
    public static final Target BTN_VER_CARRITO =
            Target.the("Botón Ver Carrito")
                    .located(By.xpath("//button[contains(., 'Ver Carrito')]"));

    /** Título del panel lateral del carrito */
    public static final Target TITULO_CARRITO =
            Target.the("Título Carrito de Cotización")
                    .located(By.xpath("//h2[contains(., 'Carrito de Cotización')]"));

    /** Select de cliente en el CartDrawer */
    public static final Target SELECT_CLIENTE =
            Target.the("Select Cliente en carrito")
                    .located(By.id("cliente"));

    /** Input datetime-local de Fecha Inicio */
    public static final Target INPUT_FECHA_INICIO =
            Target.the("Input Fecha Inicio en carrito")
                    .located(By.id("fecha_inicio"));

    /** Input datetime-local de Fecha Fin */
    public static final Target INPUT_FECHA_FIN =
            Target.the("Input Fecha Fin en carrito")
                    .located(By.id("fecha_fin"));

    /** Botón "Generar Cotización" (checkout) */
    public static final Target BTN_GENERAR_COTIZACION =
            Target.the("Botón Generar Cotización")
                    .located(By.xpath("//button[contains(., 'Generar Cotización')]"));

    /** Alerta de éxito que aparece en el carrito al generar la cotización */
    public static final Target ALERTA_COTIZACION_CREADA =
            Target.the("Alerta Cotización creada exitosamente")
                    .located(By.xpath("//*[contains(., 'Cotización creada exitosamente')]"));

    // ─── Vista de detalles de Cotización (/quotation/:id) ─────────────────────

    /**
     * Badge de estado de la cotización (ej: "Borrador", "Aprobada", etc.).
     * El texto renderizado viene de cotizacion.estado_cotizacion.
     */
    public static final Target ESTADO_COTIZACION =
            Target.the("Badge de estado de la cotización")
                    .located(By.xpath("//span[contains(@class,'rounded-full') and (contains(., 'Borrador') or contains(., 'Aprobada') or contains(., 'Rechazada') or contains(., 'Enviada'))]"));

    /**
     * Localizador dinámico: verifica que la cotización tiene el estado esperado.
     * Uso: CotizacionUI.ESTADO_COTIZACION_ESPECIFICO.of("Borrador")
     */
    public static final Target ESTADO_COTIZACION_ESPECIFICO =
            Target.the("Estado de cotización '{0}'")
                    .locatedBy("//span[contains(@class,'rounded-full') and normalize-space(.)='{0}']");

    /** Encabezado de la vista de detalles con el número de cotización */
    public static final Target TITULO_DETALLE_COTIZACION =
            Target.the("Título de detalle de cotización")
                    .located(By.xpath("//h1[contains(., 'Cotización #')]"));

    /** Botón que convierte la cotización abierta en una renta */
    public static final Target BTN_CONVERTIR_A_RENTA =
            Target.the("Botón Convertir a Renta")
                    .located(By.xpath("//button[contains(., 'Convertir a Renta')]"));

    /** Alerta de éxito al convertir una cotización en renta */
    public static final Target ALERTA_RENTA_CREADA =
            Target.the("Alerta Renta creada exitosamente")
                    .located(By.xpath("//*[contains(., 'Renta creada exitosamente')]"));

    /** Select de cliente en el carrito */
    public static final Target SELECT_CLIENTE_CARRITO =
            Target.the("Select de cliente en carrito")
                    .located(By.id("cliente"));

    private CotizacionUI() {
    }
}
