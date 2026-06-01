# language: es

Característica: Gestión de inventario
  Como administrador del sistema
  Quiero poder gestionar los equipos del inventario

  Antecedentes:
    Dado que el usuario se encuentra en la pagina de inicio de sesion
    Cuando Ingrese las credenciales correctas (usuario y contrasena)
      | usuario | clave       |
      | student | Password123 |

  Esquema del escenario: Agregar un equipo valido al inventario
    Dado que estoy en el modulo de gestion de inventario
    Cuando el usuario agrega un equipo valido al inventario con nombre "<nombre>", marca "<marca>", modelo "<modelo>", serie "<serie>", categoria "<categoria>" y tarifa diaria "<tarifa>"
    Entonces el equipo "<nombre>" deberia aparecer en la lista de inventario con un mensaje de exito "Equipo Agregado"

    Ejemplos:
      | nombre               | marca | modelo   | serie          | categoria | tarifa |
      | Monitor LG Ultra Wide | LG    | 29WL500  | LG-MON-998877  | Video     | 45000  |
      | Microfono Shure SM58 | Shure | SM58     | SH-MIC-112233  | Sonido    | 15000  |
