package co.com.AutomatizacionAxxionSystem.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utilidad para formatear fechas del Gherkin al formato datetime-local del navegador.
 */
public final class FechaHoraUtil {

    private static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter ISO_DATE_TIME = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private FechaHoraUtil() {
    }

    /**
     * Convierte una fecha ISO (yyyy-MM-dd) a datetime-local con hora por defecto.
     *
     * @param fechaIso fecha en formato yyyy-MM-dd
     * @param horaPorDefecto hora en formato HH:mm (ej. 08:00)
     * @return valor para input type="datetime-local"
     */
    public static String aDateTimeLocal(String fechaIso, String horaPorDefecto) {
        validarFechaIso(fechaIso);
        if (fechaIso.contains("T")) {
            return fechaIso.length() == 16 ? fechaIso : fechaIso.substring(0, 16);
        }
        return fechaIso + "T" + horaPorDefecto;
    }

    /**
     * Fecha de inicio con hora 08:00.
     */
    public static String fechaInicioCotizacion(String fechaIso) {
        return aDateTimeLocal(fechaIso, "08:00");
    }

    /**
     * Fecha de fin con hora 18:00.
     */
    public static String fechaFinCotizacion(String fechaIso) {
        return aDateTimeLocal(fechaIso, "18:00");
    }

    /**
     * Valida que la fecha fin sea posterior a la fecha inicio.
     */
    public static void validarRangoFechas(String fechaInicioIso, String fechaFinIso) {
        LocalDate inicio = LocalDate.parse(fechaInicioIso.substring(0, 10), ISO_DATE);
        LocalDate fin = LocalDate.parse(fechaFinIso.substring(0, 10), ISO_DATE);
        if (!fin.isAfter(inicio) && !fin.isEqual(inicio)) {
            throw new IllegalArgumentException(
                    "La fecha fin debe ser igual o posterior a la fecha inicio: "
                            + fechaInicioIso + " -> " + fechaFinIso);
        }
    }

    private static void validarFechaIso(String fechaIso) {
        if (fechaIso == null || fechaIso.isBlank()) {
            throw new IllegalArgumentException("La fecha no puede estar vacía");
        }
        try {
            if (fechaIso.contains("T")) {
                LocalDateTime.parse(fechaIso.length() >= 16 ? fechaIso.substring(0, 16) : fechaIso, ISO_DATE_TIME);
            } else {
                LocalDate.parse(fechaIso, ISO_DATE);
            }
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Formato de fecha inválido: " + fechaIso, ex);
        }
    }
}
