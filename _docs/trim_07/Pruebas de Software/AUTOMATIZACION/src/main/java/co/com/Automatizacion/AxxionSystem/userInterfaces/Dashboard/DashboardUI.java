package co.com.Automatizacion.AxxionSystem.userInterfaces.Dashboard;

import net.serenitybdd.screenplay.targets.Target;

public class DashboardUI {
    public static final Target TITULO_DASHBOARD = Target.the("Mensaje de bienvenida").locatedBy("//h1[contains(., 'AXXION SYSTEM')]");
    public static final Target BOTON_NAV_USUARIOS = Target.the("Bonton Navegacion Usuarios").locatedBy("//a[contains(@href,'/User')]");
    public static final Target BOTON_NAV_CATEGORIAS = Target.the("Bonton Navegacion Usuarios").locatedBy("//a[contains(@href,'/Category')]");
    public static final Target BOTON_NAV_INVENTARIO = Target.the("Bonton Navegacion Usuarios").locatedBy("//a[contains(@href,'/Inventory')]");
    public static final Target BOTON_NAV_SOLICITUDES = Target.the("Bonton Navegacion Usuarios").locatedBy("//a[contains(@href,'/Solicitudes')]");
    public static final Target BOTON_NAV_ALERTAS = Target.the("Bonton Navegacion Usuarios").locatedBy("//a[contains(@href,'/Alerts')]");
    public static final Target BOTON_NAV_MANTENIMIENTOS = Target.the("Bonton Navegacion Usuarios").locatedBy("//a[contains(@href,'/Mantenace')]");
    public static final Target BOTON_NAV_ALQUILER = Target.the("Bonton Navegacion Usuarios").locatedBy("//a[contains(@href,'/Rental')]");

}
