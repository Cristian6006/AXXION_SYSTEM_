package co.com.Automatizacion.AxxionSystem.stepDefintions.Cotizacion.helper;

import co.com.Automatizacion.AxxionSystem.factory.Inventario.EquipoFactory;
import co.com.Automatizacion.AxxionSystem.models.Cotizacion.SolicitudCotizacion;
import co.com.Automatizacion.AxxionSystem.models.Inventario.Equipo;
import co.com.Automatizacion.AxxionSystem.models.Navegacion.Pagina;
import co.com.Automatizacion.AxxionSystem.tasks.Cotizacion.AgregarEquipoAlCarrito;
import co.com.Automatizacion.AxxionSystem.tasks.Cotizacion.RegistrarSolicitudCotizacion;
import co.com.Automatizacion.AxxionSystem.tasks.Inventario.AgregarEquipo;
import co.com.Automatizacion.AxxionSystem.tasks.Navegacion.Navegar;
import net.serenitybdd.screenplay.actors.OnStage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CotizacionHelper {
    private CotizacionHelper() {}

    public static void generarCotizacionBorrador(String cliente, String equipoIgnorado) {
        Equipo equipo = EquipoFactory.equipoParaCotizacion();
        String nombreEquipo = equipo.getNombre();
        String fechaInicio = LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
        String fechaFin = LocalDate.now().plusDays(15).format(DateTimeFormatter.ISO_LOCAL_DATE);

        SolicitudCotizacion solicitud = SolicitudCotizacion.builder()
                .cliente(cliente)
                .equipo(nombreEquipo)
                .fechaInicio(fechaInicio)
                .fechaFin(fechaFin)
                .build();

        OnStage.theActorInTheSpotlight().attemptsTo(
                Navegar.a(Pagina.INVENTARIO),
                AgregarEquipo.con(equipo),
                AgregarEquipoAlCarrito.conNombre(nombreEquipo),
                RegistrarSolicitudCotizacion.con(solicitud)
        );
    }
}
