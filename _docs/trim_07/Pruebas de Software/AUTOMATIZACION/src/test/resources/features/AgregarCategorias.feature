Feature: Agregar Categorias
  Como usuario del sistema
  Quiero poder agregar nuevas categorias
  Para organizar los productos o servicios

  Background:
    Given que he ingresado a la aplicacion correctamente con un usuario valido
    When ingrese las credenciales correctas (usuario y contraseña)
    |    usuario    | |    clave     |
    | p@example.com | | contraseña12 |

  @agregarCategoria

  Scenario Outline : Agregar una categoria valida
    Given que estoy en la pagina de gestion de categorias
    When ingreso el nombre "<nombre>"
    And ingreso la descripcion "<descripcion>"
    And presiono el boton "Agregar Categoria"
    Then debería ver un mensaje de exito
    And la categoria "<nombre>" debería aparecer en la lista

    Examples:
      | nombre       | descripcion                         |
      | Electronicos | Dispositivos electronicos y gadgets |


  Scenario Outline: Agregar una categoria sin nombre
    Given que estoy en la pagina de gestion de categorias
    When dejo vacio el campo de nombre de categoria
    And ingreso la descripción "<descripcion>" en el campo de descripcion
    And presiono el boton "Agregar Categoria"
    Then debería ver un mensaje de error indicando que el nombre es requerido
    And no se debería crear ninguna categoria

    Examples:
      | descripcion           |
      | Categoria sin nombre  |

  Scenario Outline: Agregar una categoria con nombre muy largo
    Given que estoy en la pagina de gestion de categorias
    When ingreso un nombre de categoria con más de 100 caracteres en el campo de nombre
    And ingreso una descripción "<descripcion>" en el campo de descripcion
    And presiono el boton "Agregar Categoria"
    Then debería ver un mensaje de error indicando que el nombre excede el limite de caracteres permitido

    Examples:
      | descripcion              |
      | Descripción valida       |