package co.com.Automatizacion.AxxionSystem.factory.Categoria;

import co.com.Automatizacion.AxxionSystem.models.Categorias.Categoria;
import net.datafaker.Faker;
import java.util.Locale;

public class CategoriaFactory {
    private static final Faker faker = new Faker(new Locale("es"));

    public static Categoria ramdomCategory() {
        return Categoria.builder()
                .nombre(faker.commerce().department())
                .tipoCategoria(faker.company().industry())
                .descripcion(faker.lorem().sentence())
                .build();
    }
    public static Categoria updateCategory(Categoria original) {
        return Categoria.builder()
                .nombre(original.getNombre())
                .tipoCategoria(faker.company().industry())
                .descripcion(faker.lorem().sentence())
                .build();
    }
}
