# language: es

Característica: Gestión de inventario
Como administrador del sistema
Quiero poder gestionar los equipos del inventario
  Antecedentes:
    Dado que he ingresado a la aplicacion correctamente con un usuario valido
    Cuando ingrese las credenciales correctas (usuario y contraseña)
      | usuario       | clave      |
      | c@example.com | Su12345678 |

    Esquema del escenario: Agregar un equipo valido al inventario
    Entonces que estoy en el modulo  de gestion de inventario
    Entonces presiono el boton "Agregar Equipo"
    Cuando ingreso el nombre del equipo "<nombre>"
    Entonces ingreso la marca "<marca>"
    Entonces ingreso el modelo "<modelo>"
    Entonces ingreso el numero de serie "<serie>"
    Entonces selecciono la categoria "<categoria>"
    Entonces ingreso la tarifa diaria "<tarifa>"
    Entonces presiono el boton "Confirmar Agregar"
    Entonces debería ver un mensaje de exito "Equipo Agregado"
    Entonces el  equipo "<nombre>" debería aparecer en la lista de inventario

    Ejemplos:
      | nombre               | marca | modelo   | serie          | categoria | tarifa |
      | Monitor LG Ultra Wide | LG    | 29WL500  | LG-MON-998877  | Video     | 45000  |
      | Microfono Shure SM58 | Shure | SM58     | SH-MIC-112233  | Sonido    | 15000  |
