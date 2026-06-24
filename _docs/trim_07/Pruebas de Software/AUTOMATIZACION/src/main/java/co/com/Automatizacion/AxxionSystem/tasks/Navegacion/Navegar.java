package co.com.Automatizacion.AxxionSystem.tasks.Navegacion;

import co.com.Automatizacion.AxxionSystem.models.Navegacion.Pagina;
import co.com.Automatizacion.AxxionSystem.userInterfaces.Dashboard.DashboardUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.targets.Target;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class Navegar implements Task {
    private final Pagina pagina;

    public Navegar(Pagina pagina) {
        this.pagina = pagina;
    }

    @Override
    public <T extends Actor> void  performAs(T actor) {
        Target target = switch (pagina) {
            case USUARIOS -> DashboardUI.BOTON_NAV_USUARIOS;
            case CATEGORIAS -> DashboardUI.BOTON_NAV_CATEGORIAS;
            case INVENTARIO -> DashboardUI.BOTON_NAV_INVENTARIO;
            case SOLICITUDES -> DashboardUI.BOTON_NAV_SOLICITUDES;
            case ALERTAS ->  DashboardUI.BOTON_NAV_ALERTAS;
            case MANTENIMIENTOS -> DashboardUI.BOTON_NAV_MANTENIMIENTOS;
            case ALQUILER -> DashboardUI.BOTON_NAV_ALQUILER;
            default -> throw new IllegalArgumentException("Página no soportada");
        };
        actor.attemptsTo(
                Click.on(target)
        );
    }

public static Navegar a(Pagina pagina) {
        return instrumented(Navegar.class, pagina);
}
}
