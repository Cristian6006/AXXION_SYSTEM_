# language: es

Característica: Generación de cotización de equipos
  Como encargado de almacén
  Quiero crear cotizaciones para los clientes
  Para reservar equipos y proyectar ingresos

  Antecedentes:
    Dado que el administrador cuenta con sesión iniciada en AXXION SYSTEM
    Y el administrador está en el módulo de inventario
    Y el usuario registra un equipo disponible para cotización

  @generacion-cotizacion
  Escenario: Crear una cotización en borrador con un equipo disponible
    Cuando el usuario agrega el equipo registrado al carrito de cotización
    Y registra la solicitud para el cliente "Carlos Sanchez" entre las fechas "2026-07-01" y "2026-07-15"
    Entonces el sistema genera la cotización con estado "Borrador"
    Y el usuario visualiza un desglose de costos acorde al número de días del periodo seleccionado
