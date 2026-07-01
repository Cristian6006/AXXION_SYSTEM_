package co.com.Automatizacion.AxxionSystem.factory.Usuario;

import co.com.Automatizacion.AxxionSystem.models.Usuarios.Usuario;
import net.datafaker.Faker;

import java.util.Locale;

public class UsuarioFactory {
    private static final Faker faker = new Faker(new Locale("es"));

    public static Usuario ramdomUser() {
        return Usuario.builder()
                .primerNombre(faker.name().firstName())
                .segundoNombre(faker.name().firstName())
                .primerApellido(faker.name().firstName())
                .segundoApellido(faker.name().lastName())
                .nombreUsuario(faker.internet().username())
                .email(faker.internet().emailAddress())
                .telefono(faker.phoneNumber().cellPhone())
                .clave(faker.internet().password(8, 16, true, true, true))
                .build();
    }

    public static Usuario updateUser(Usuario original) {
        return Usuario.builder()
                .primerNombre(faker.name().firstName())
                .segundoNombre(original.getSegundoNombre())
                .primerApellido(original.getPrimerApellido())
                .segundoApellido(original.getSegundoApellido())
                .nombreUsuario(original.getNombreUsuario())
                .build();
    }
}
