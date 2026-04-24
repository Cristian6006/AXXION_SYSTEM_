Feature: Mantenimiento de Inventario
  Como administrador del sistema
  Quiero poder programar mantenimientos para los equipos
  Para asegurar el buen estado operativo de los activos

  Background:
    Given que he ingresado a la aplicacion correctamente con un usuario valido
    When ingrese las credenciales correctas (usuario y contraseña)
      | usuario       | clave      |
      | c@example.com | Su12345678 |
    And estoy en la pagina de gestion de inventario

  @CP08 @programarMantenimiento
  Scenario Outline: Programar un mantenimiento preventivo exitoso
    Given que selecciono un equipo con estado "Disponible"
    And presiono el boton "Mantener"
    When selecciono el tipo de mantenimiento "<tipo>"
    And ingreso la descripcion del mantenimiento "<descripcion>"
    And selecciono la fecha de inicio "<fecha_inicio>"
    And selecciono el tecnico asignado "<tecnico>"
    And presiono el boton "Programar Mantenimiento"
    Then debería ver un mensaje de exito
    And el estado del equipo debe cambiar a "En Mantenimiento"

    Examples:
      | tipo       | descripcion           | fecha_inicio | tecnico        |
      | Preventivo | Limpieza de lentes    | 2026-05-01   | Juan Perez     |
      | Correctivo | Reparacion de fuente  | 2026-05-05   | Carlos Ruiz    |
