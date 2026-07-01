package co.com.Automatizacion.AxxionSystem.runners;

import co.com.Automatizacion.AxxionSystem.runners.Autenticacion.AutenticacionSuite;
import co.com.Automatizacion.AxxionSystem.runners.Categorias.AgregarCateforiaSuite;
import co.com.Automatizacion.AxxionSystem.runners.Categorias.BorrarCategoriaSuite;
import co.com.Automatizacion.AxxionSystem.runners.Categorias.ConsultarCategoriaSuite;
import co.com.Automatizacion.AxxionSystem.runners.Categorias.EditarCategoriaSuite;
import co.com.Automatizacion.AxxionSystem.runners.Cotizacion.GenerarCotizacionSuite;
import co.com.Automatizacion.AxxionSystem.runners.Inventario.AgregarEquipoSuite;
import co.com.Automatizacion.AxxionSystem.runners.Inventario.EliminarEquipoSuite;
import co.com.Automatizacion.AxxionSystem.runners.Renta.GenerarRentaSuite;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * Runner único: ejecuta todas las suites E2E del proyecto AXXION SYSTEM.
 * Orden recomendado: autenticación → categorías → inventario → cotización → renta.
 */
@Suite
@SelectClasses({
        AutenticacionSuite.class,
        AgregarCateforiaSuite.class,
        BorrarCategoriaSuite.class,
        ConsultarCategoriaSuite.class,
        EditarCategoriaSuite.class,
        AgregarEquipoSuite.class,
        EliminarEquipoSuite.class,
        GenerarCotizacionSuite.class,
        GenerarRentaSuite.class
})
public class AxxionSystemSuite {
}
