Feature: Gestion de Inventario
  Como administrador del sistema
  Quiero poder gestionar los equipos del inventario
  Para mantener un control actualizado de los activos de la empresa

  Background:
    Given que he ingresado a la aplicacion correctamente con un usuario valido
    When ingrese las credenciales correctas (usuario y contraseña)
      | usuario       | clave      |
      | c@example.com | Su12345678 |

  @CP04 @agregarEquipo
  Scenario Outline: Agregar un equipo valido al inventario
    Given que estoy en la pagina de gestion de inventario
    And presiono el boton "Agregar Equipo"
    When ingreso el nombre del equipo "<nombre>"
    And ingreso la marca "<marca>"
    And ingreso el modelo "<modelo>"
    And ingreso el numero de serie "<serie>"
    And selecciono la categoria "<categoria>"
    And ingreso la tarifa diaria "<tarifa>"
    And presiono el boton "Confirmar Agregar"
    Then debería ver un mensaje de exito "Equipo Agregado"
    And el equipo "<nombre>" debería aparecer en la lista de inventario

    Examples:
      | nombre               | marca | modelo   | serie          | categoria | tarifa |
      | Monitor LG UltraWide | LG    | 29WL500  | LG-MON-998877  | Video     | 45000  |
      | Microfono Shure SM58 | Shure | SM58     | SH-MIC-112233  | Sonido    | 15000  |

  @CP05 @validacionCampos
  Scenario: Intentar agregar un equipo sin campos obligatorios
    Given que estoy en la pagina de gestion de inventario
    And presiono el boton "Agregar Equipo"
    When dejo los campos obligatorios vacios
    And presiono el boton "Confirmar Agregar"
    Then debería ver indicadores de error en los campos requeridos
    And el sistema no debe permitir el registro del equipo

  @CP07 @restriccionEliminacion
  Scenario: No permitir la eliminacion de un equipo rentado
    Given que estoy en la pagina de gestion de inventario
    And existe un equipo con estado "Alquilado" en la lista
    When presiono el boton "Eliminar" de dicho equipo
    Then debería ver un mensaje de advertencia "No se puede eliminar porque tiene una renta en curso"
    And el equipo no debe ser eliminado de la lista
