package co.com.Automatizacion.AxxionSystem.stepDefintions.Categorias;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
public class EditarCategoriaStepDefinition {
    @Cuando("el usuario selecciona la categoría {string} y hace click en el icono de edición")
    public void elUsuarioSeleccionaLaCategoríaYHaceClickEnElIconoDeEdición(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    @Cuando("ingresa los detalles de la categoría editada")
    public void ingresaLosDetallesDeLaCategoríaEditada(io.cucumber.datatable.DataTable dataTable) {
        // Write code here that turns the phrase above into concrete actions
        // For automatic transformation, change DataTable to one of
        // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
        // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
        // Double, Byte, Short, Long, BigInteger or BigDecimal.
        //
        // For other transformations you can register a DataTableType.
        throw new io.cucumber.java.PendingException();
    }
    @Cuando("confirma la edición de la categoría")
    public void confirmaLaEdiciónDeLaCategoría() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    @Entonces("la categoría {string} debería evidenciar cambios en la lista")
    public void laCategoríaDeberíaEvidenciarCambiosEnLaLista(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
}
