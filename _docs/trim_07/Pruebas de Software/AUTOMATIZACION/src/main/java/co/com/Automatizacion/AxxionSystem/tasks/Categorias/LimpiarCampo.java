package co.com.Automatizacion.AxxionSystem.tasks.Categorias;

import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.Keys;

public class LimpiarCampo {
    public static Performable con(Target campo, String valor) {
        String borrarTodo = Keys.chord(Keys.CONTROL, "a") + Keys.BACK_SPACE;
        return Task.where(
                Enter.theValue(borrarTodo).into(campo),
                Enter.theValue(valor).into(campo).thenHit(Keys.TAB)
        );
    }
}
