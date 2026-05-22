# language: es

@inventario @cp01 @prioridad-alta
Característica: Carga inicial de métricas KPI en inventario
  Como usuario del sistema
  Quiero ver indicadores consolidados del parque de equipos
  Para contrastar el estado del inventario con los datos reales del sistema

  Antecedentes:
    Dado que el usuario se encuentra en la pagina de inicio de sesion
    Cuando Ingrese las credenciales correctas (usuario y contrasena)
      | usuario | clave       |
      | student | Password123 |

  Escenario: Los paneles KPI reflejan las sumatorias del catálogo y se mantienen alineados
    Cuando el usuario ingresa al módulo de inventario
    Entonces los paneles de métricas muestran los totales de equipos disponibles, alquilados, en mantenimiento y el monto de ingresos acorde a la sumatoria del catálogo vigente
    Y los indicadores se actualizan cuando cambia la información subyacente del inventario
