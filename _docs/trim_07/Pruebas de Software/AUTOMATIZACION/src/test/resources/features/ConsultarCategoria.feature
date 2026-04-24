Feature: Consultar Categorías
  Como usuario del sistema
  Quiero visualizar y buscar categorías
  Para encontrar información específica de manera rápida

  @consultarCategoria

  Scenario Outline: Consultar categorías con filtros y búsqueda
    Given que he ingresado correctamente a la aplicación con un usuario válido
    And estoy en la página de categorías
    When hago clic en la opción "Categorías"
    Then debería ver la lista de categorías

    When ingreso "<buscar_categoria>" en el campo de búsqueda
    Then debería visualizar la categoría específica "<buscar_categoria>"

    When selecciono la fecha "<fecha>" en el filtro de fechas
    Then debería visualizar la categoría específica filtrada por la fecha "<fecha>"

    Examples:
      | buscar_categoria | fecha       |
      | Iluminación      | 21/02/2026  |
      | Electrónicos     | 15/03/2026  |

