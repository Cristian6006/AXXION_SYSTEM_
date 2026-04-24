Feature: Filtros y Busqueda de Inventario
  Como administrador del sistema
  Quiero poder filtrar y buscar equipos en el inventario
  Para localizar activos especificos de forma rapida

  Background:
    Given que he ingresado a la aplicacion correctamente con un usuario valido
    When ingrese las credenciales correctas (usuario y contraseña)
      | usuario       | clave      |
      | c@example.com | Su12345678 |
    And estoy en la pagina de gestion de inventario

  @CP02 @busquedaGlobal
  Scenario Outline: Buscar equipo por nombre o serie
    When ingreso el criterio de busqueda "<criterio>"
    Then la lista de equipos debería mostrar solo aquellos que coincidan con "<criterio>"

    Examples:
      | criterio      |
      | Dell          |
      | LG-MON-998877 |
      | SM58          |

  @CP03 @filtradoEstado
  Scenario Outline: Filtrar equipos por estado
    When selecciono el estado "<estado>" en el filtro
    Then la lista de equipos debería mostrar solo equipos con estado "<resultado>"

    Examples:
      | estado           | resultado        |
      | Disponible       | Disponible       |
      | Alquilado        | Rentado          |
      | En Mantenimiento | EnMantenimiento  |

  @CP09 @paginacion
  Scenario: Navegar por la paginacion del inventario
    Given que existen mas de 12 equipos registrados
    When presiono el boton de la pagina "2"
    Then el sistema debería cargar el siguiente set de equipos
    And la vista no debe recargarse completamente
