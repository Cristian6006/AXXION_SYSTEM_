# language: es


Característica: Generación de cotización de equipos
  Como encargado de almacén
  Quiero crear cotizaciones para los clientes
  Para reservar equipos y proyectar ingresos

  Antecedentes:
    Dado que el usuario se encuentra en la pagina de inicio de sesion
    Cuando Ingrese las credenciales correctas (usuario y contrasena)
      | usuario | clave       |
      | student | Password123 |
  @generacion-cotizacion
  Escenario: Crear una cotización en borrador con un equipo disponible
    Cuando el usuario genera una cotizacion para el cliente "Carlos Sanchez" con el equipo "Monitor" entre las fechas "2026-04-28" y "2026-05-05"
    Entonces el sistema genera la cotización con estado "Borrador"