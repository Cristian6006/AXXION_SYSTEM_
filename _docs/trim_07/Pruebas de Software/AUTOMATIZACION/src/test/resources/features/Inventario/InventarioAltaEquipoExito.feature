# language: es

Característica: Registro exitoso de un equipo en inventario
  Como usuario con permisos de gestión de inventario
  Quiero registrar equipos nuevos con sus datos obligatorios
  Para ampliar el catálogo con información consistente

  Antecedentes:
    Dado que el usuario cuenta con sesión iniciada en AXXION SYSTEM
    Y el usuario está en el módulo de inventario
    Y el usuario cuenta con permisos para crear equipos

  @inventario @cp04 @prioridad-alta
  Escenario: El alta con datos mínimos obligatorios confirma el registro y prioriza el nuevo ítem en el listado
    Cuando el usuario registra un equipo nuevo desde el formulario con nombre "Proyector portátil", marca "Epson", serie "SN-EPS-2026-001" y tarifa diaria "18.50"
    Entonces el sistema muestra una alerta de éxito
    Y el modal de registro deja de estar visible
    Y el nuevo equipo aparece en la primera posición del listado
