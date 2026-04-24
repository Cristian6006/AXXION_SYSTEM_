Feature: Agregar Subcategorías
  Como usuario del sistema
  Quiero poder agregar nuevas subcategorías
  Para organizar mejor los productos o servicios dentro de una categoría

  @agregarSubcategoria

  Scenario Outline: Agregar una subcategoría válida
    Given que estoy en la página de gestión de categorías
    When selecciono la categoría "<categoria>"
    And hago clic en la opción "+ Añadir nueva"
    Then debería mostrarse un modal para registrar una nueva subcategoría

    When ingreso el nombre "<nombre_subcategoria>" en el campo de nombre de subcategoría
    And ingreso la descripción "<descripcion>" en el campo de descripción
    And presiono el botón "Guardar"
    Then debería ver un icono de carga visible
    And el botón "Guardar" debería quedar inhabilitado
    And debería mostrarse un mensaje indicando que la subcategoría se creó exitosamente
    And la subcategoría "<nombre_subcategoria>" debería aparecer en la lista de subcategorías

    Examples:
      | categoria     | nombre_subcategoria | descripcion                              |
      | Electrónicos  | Pantalla LED        | pantalla modular de gran resolución      |
      | Electrónicos  | Monitores Gaming    | monitores de alta tasa de refresco       |


  Scenario Outline: No permitir agregar una subcategoría sin nombre
    Given que estoy en la página de gestión de categorías
    And selecciono la categoría "<categoria>"
    When hago clic en la opción "+ Añadir nueva"
    And dejo vacío el campo de nombre de subcategoría
    And ingreso la descripción "<descripcion>" en el campo de descripción
    And presiono el botón "Guardar"
    Then debería ver un mensaje de error indicando que el nombre es requerido
    And no se debería crear ninguna subcategoría

    Examples:
      | categoria     | descripcion                     |
      | Electrónicos  | subcategoría sin nombre        |
      | Hogar         | prueba sin nombre              |


  Scenario Outline: No permitir agregar una subcategoría con nombre mayor a 100 caracteres
    Given que estoy en la página de gestión de categorías
    And selecciono la categoría "<categoria>"
    When hago clic en la opción "+ Añadir nueva"
    And ingreso el nombre "<nombre_subcategoria>" en el campo de nombre de subcategoría
    And ingreso la descripción "<descripcion>" en el campo de descripción
    And presiono el botón "Guardar"
    Then debería ver un mensaje de error indicando que el nombre excede el limite de caracteres permitido
    And no se debería crear ninguna subcategoría

    Examples:
      | categoria     | nombre_subcategoria                                                                 | descripcion                |
      | Electrónicos  | Subcategoria_con_nombre_excesivamente_largo_que_supera_los_100_caracteres_123456789 | descripción válida         |
      | Hogar         | Nombre_muy_largo_para_validar_el_limite_de_cien_caracteres_en_el_sistema_abcdefghi  | otra descripción válida    |