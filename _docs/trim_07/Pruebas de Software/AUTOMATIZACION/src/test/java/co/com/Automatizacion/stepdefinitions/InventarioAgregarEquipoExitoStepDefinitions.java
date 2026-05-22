package co.com.Automatizacion.stepdefinitions;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;

public class InventarioAgregarEquipoExitoStepDefinitions {

    @Cuando("el usuario agrega un equipo valido al inventario con nombre {string}, marca {string}, modelo {string}, serie {string}, categoria {string} y tarifa diaria {string}")
    public void elUsuarioAgregaUnEquipoValidoAlInventarioConNombreMarcaModeloSerieCategoriaYTarifaDiaria(String string, String string2, String string3, String string4, String string5, String string6) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    @Entonces("el equipo {string} deberia aparecer en la lista de inventario con un mensaje de exito {string}")
    public void elEquipoDeberiaAparecerEnLaListaDeInventarioConUnMensajeDeExito(String string, String string2) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

}