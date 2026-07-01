package co.com.Automatizacion.AxxionSystem.stepDefintions.Categorias;

import co.com.Automatizacion.AxxionSystem.models.Categorias.Categoria;
import co.com.Automatizacion.AxxionSystem.questions.Categorias.CategoriaExiste;
import co.com.Automatizacion.AxxionSystem.questions.Categorias.CategoriaFecha;
import co.com.Automatizacion.AxxionSystem.tasks.Categorias.ConsultarCategoriaFecha;
import co.com.Automatizacion.AxxionSystem.tasks.Categorias.ConsultarCategoriaNombre;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;

public class ConsultarCategoriaStepDefinition {
    @Cuando("ingrese el nombre de la categoria buscada")
    public void ingrese_el_nombre_de_la_categoria_buscada() {
        Actor actor = OnStage.theActorInTheSpotlight();
        Categoria categoriaActual = actor.recall("CATEGORIA");
        actor.attemptsTo(ConsultarCategoriaNombre.conNombre(categoriaActual));
    }
    @Entonces("debería ver unicamente resultados que contengan la categoria buscada")
    public void debería_ver_unicamente_resultados_que_contengan_la_categoria_buscada() {
        Actor actor = OnStage.theActorInTheSpotlight();
        Categoria categoriaActual = actor.recall("CATEGORIA");
        actor.should(
                seeThat(CategoriaExiste.enLaLista(categoriaActual))
        );
    }
}
