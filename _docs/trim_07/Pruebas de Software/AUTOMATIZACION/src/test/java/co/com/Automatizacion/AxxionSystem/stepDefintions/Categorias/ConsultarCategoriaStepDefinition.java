package co.com.Automatizacion.AxxionSystem.stepDefintions.Categorias;

import co.com.Automatizacion.AxxionSystem.models.Categorias.Categoria;
import co.com.Automatizacion.AxxionSystem.questions.Categorias.CategoriaExiste;
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
    @Cuando("el usuario selecciona la fecha en el filtro de fechas")
    public void el_usuario_selecciona_la_fecha_en_el_filtro_de_fechas() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    @Entonces("debería ver solo las categorias creadas en la fecha seleccionada")
    public void debería_ver_solo_las_categorias_creadas_en_la_fecha_seleccionada() {
        Actor actor = OnStage.theActorInTheSpotlight();
        Categoria categoriaActual = actor.recall("CATEGORIA");
        actor.should(
                seeThat(CategoriaExiste.enLaLista(categoriaActual))
        );
    }
}
