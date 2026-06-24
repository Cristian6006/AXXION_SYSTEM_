package co.com.Automatizacion.AxxionSystem.stepDefintions.Categorias.helper;

import co.com.Automatizacion.AxxionSystem.factory.Categoria.CategoriaFactory;
import co.com.Automatizacion.AxxionSystem.models.Categorias.Categoria;
import net.serenitybdd.screenplay.actors.OnStage;

public class CategoriaHelper {
    private CategoriaHelper() {}

    public static Categoria crearCategoria() {
        Categoria categoria = CategoriaFactory.ramdomCategory();
        OnStage.theActorInTheSpotlight()
                .remember("CATEGORIA", categoria);
        return categoria;
    }
}
