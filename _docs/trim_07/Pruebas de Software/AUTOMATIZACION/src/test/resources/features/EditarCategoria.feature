Feature: Editar Categorías
  Como usuario del sistema
  Quiero poder editar categorías existentes
  Para actualizar su información cuando sea necesario

  @editarCategoria

  Scenario Outline: Editar una categoría correctamente
    Given que estoy en la página de gestión de categorías
    When selecciono la categoría "<categoria>" y hago clic en el icono de edición
    Then debería abrirse un modal con los datos actuales de la categoría

    When edito el nombre a "<nuevo_nombre>"
    And edito el tipo de categoría a "<tipo_categoria>"
    And edito la descripción a "<descripcion>"
    And presiono el botón "Guardar"
    Then debería ver un mensaje de éxito indicando que la categoría fue editada correctamente
    And el modal debería cerrarse
    And la categoría "<nuevo_nombre>" debería reflejar los cambios en la lista

    Examples:
      | categoria     | nuevo_nombre | tipo_categoria | descripcion                              |
      | Electrónicos  | Video        | Pantallas      | pantalla modular de gran resolución      |
      | Hogar         | Muebles      | Oficina        | muebles ergonómicos de oficina           |

  Scenario Outline: No permitir editar una categoría sin nombre
    Given que estoy en la página de gestión de categorías
    And selecciono la categoría "<categoria>" y hago clic en el icono de edición
    When dejo vacío el campo de nombre
    And presiono el botón "Guardar"
    Then debería ver un mensaje de error indicando que el nombre es requerido
    And no se deberían guardar los cambios

    Examples:
      | categoria     |
      | Electrónicos  |
      | Hogar         |

  Scenario Outline: No permitir editar una categoría con nombre mayor a 100 caracteres
    Given que estoy en la página de gestión de categorías
    And selecciono la categoría "<categoria>" y hago clic en el icono de edición
    When ingreso el nombre "<nuevo_nombre>"
    And presiono el botón "Guardar"
    Then debería ver un mensaje de error indicando que el nombre excede el limite de caracteres permitido
    And no se deberían guardar los cambios

    Examples:
      | categoria     | nuevo_nombre                                                                 |
      | Electrónicos  | Nombre_excesivamente_largo_para_validar_el_limite_de_100_caracteres_123456789 |
      | Hogar         | Otro_nombre_demasiado_largo_para_probar_validacion_de_longitud_abcdefghi      |