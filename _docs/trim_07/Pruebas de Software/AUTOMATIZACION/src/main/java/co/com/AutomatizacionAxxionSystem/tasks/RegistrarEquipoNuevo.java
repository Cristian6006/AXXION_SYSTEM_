package co.com.AutomatizacionAxxionSystem.tasks;

import co.com.AutomatizacionAxxionSystem.models.DatosEquipo;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;

/**
 * Task: registra un equipo con los campos del escenario InventarioAgregarEquipoExito.
 * Delega en AgregarEquipoAlInventario tras construir el modelo de datos.
 */
public class RegistrarEquipoNuevo implements Task {

    private final String nombre;
    private final String marca;
    private final String serie;
    private final String tarifaDiaria;

    public RegistrarEquipoNuevo(String nombre, String marca, String serie, String tarifaDiaria) {
        this.nombre = nombre;
        this.marca = marca;
        this.serie = serie;
        this.tarifaDiaria = tarifaDiaria;
    }

    public static RegistrarEquipoNuevo con(String nombre, String marca, String serie, String tarifaDiaria) {
        return Tasks.instrumented(RegistrarEquipoNuevo.class, nombre, marca, serie, tarifaDiaria);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        DatosEquipo datos = new DatosEquipo(nombre, marca, serie, tarifaDiaria);
        actor.attemptsTo(
                NavegarAInventario.elModulo(),
                AgregarEquipoAlInventario.con(datos)
        );
    }
}
