package co.com.Automatizacion.AxxionSystem.stepDefintions.Categorias;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;

public class AgregarCategoriasStepDefinitions {
    @Cuando("el usuario crea una nueva categoría")
    public void elUsuarioCreaUnaNuevaCategoría(io.cucumber.datatable.DataTable dataTable) {
        // Write code here that turns the phrase above into concrete actions
        // For automatic transformation, change DataTable to one of
        // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
        // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
        // Double, Byte, Short, Long, BigInteger or BigDecimal.
        //
        // For other transformations you can register a DataTableType.
        throw new io.cucumber.java.PendingException();
    }
    @Entonces("la categoría {string} debería estar visible en la lista de categorías")
    public void laCategoríaDeberíaEstarVisibleEnLaListaDeCategorías(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
}
