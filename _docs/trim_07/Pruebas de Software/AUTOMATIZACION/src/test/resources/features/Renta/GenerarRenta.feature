# language: es

Característica: Conversión de cotización a renta efectiva
  Como administrador del sistema
  Quiero convertir cotizaciones en rentas programadas
  Para formalizar el alquiler de equipos a los clientes

  Antecedentes:
    Dado que el administrador cuenta con sesión iniciada en AXXION SYSTEM
    Y existe una cotización en estado "Borrador" para el cliente "Carlos Sanchez"
    Y el administrador visualiza el detalle de esa cotización

  @generacion-renta
  Escenario: Registrar una renta a partir de una cotización existente
    Cuando el usuario convierte la cotización en renta
    Entonces el sistema registra una nueva renta con estado "Programada"
    Y la renta mantiene la referencia al número de cotización de origen
    Y los equipos de la cotización figuran vinculados al cronograma del cliente "Carlos Sanchez"
