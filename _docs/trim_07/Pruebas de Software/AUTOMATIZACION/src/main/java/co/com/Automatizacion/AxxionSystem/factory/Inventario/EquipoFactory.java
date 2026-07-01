package co.com.Automatizacion.AxxionSystem.factory.Inventario;

import co.com.Automatizacion.AxxionSystem.models.Inventario.Equipo;
import net.datafaker.Faker;

import java.util.Locale;

public class EquipoFactory {
    private static final Faker faker = new Faker(new Locale("es"));

    private EquipoFactory() {}

    public static Equipo equipoParaEliminar() {
        String sufijo = String.valueOf(System.currentTimeMillis());
        return Equipo.builder()
                .nombre("Equipo QA Eliminar " + sufijo)
                .marca("Epson")
                .modelo("QA-MODEL-" + sufijo)
                .serie("SN-QA-" + sufijo)
                .categoria("Video")
                .tarifaDiaria("25.00")
                .build();
    }

    public static Equipo equipoParaCotizacion() {
        String sufijo = String.valueOf(System.currentTimeMillis());
        return Equipo.builder()
                .nombre("Equipo QA Cotizacion " + sufijo)
                .marca("Sony")
                .modelo("QA-COT-" + sufijo)
                .serie("SN-COT-" + sufijo)
                .categoria("Video")
                .tarifaDiaria("50.00")
                .build();
    }

    public static Equipo desdeParametros(String nombre, String marca, String serie, String tarifaDiaria) {
        String serieUnica = serie + "-" + System.currentTimeMillis();
        return Equipo.builder()
                .nombre(nombre)
                .marca(marca)
                .modelo(faker.commerce().productName())
                .serie(serieUnica)
                .categoria("Video")
                .tarifaDiaria(tarifaDiaria)
                .build();
    }
}
