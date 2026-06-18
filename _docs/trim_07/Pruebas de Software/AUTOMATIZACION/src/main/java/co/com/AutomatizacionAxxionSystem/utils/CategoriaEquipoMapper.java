package co.com.AutomatizacionAxxionSystem.utils;

/**
 * Mapea valores de categoría del Gherkin a opciones visibles del select del formulario.
 */
public final class CategoriaEquipoMapper {

    private CategoriaEquipoMapper() {
    }

    /**
     * Normaliza el nombre de categoría para selección en el DOM.
     * El formulario usa nombres como "Equipos de Sonido", "Video", "Iluminación".
     */
    public static String aOpcionSelect(String categoriaGherkin) {
        if (categoriaGherkin == null || categoriaGherkin.isBlank()) {
            throw new IllegalArgumentException("La categoría no puede estar vacía");
        }
        String categoriaNormalizada = categoriaGherkin.trim().toLowerCase();
        switch (categoriaNormalizada) {
            case "sonido":
            case "audio":
            case "equipos de sonido":
                return "Equipos de Sonido";
            case "video":
                return "Video";
            case "iluminación":
            case "iluminacion":
                return "Iluminación";
            case "mobiliario":
                return "Mobiliario";
            case "estructuras":
                return "Estructuras";
            default:
                return categoriaGherkin.trim();
        }
    }
}
