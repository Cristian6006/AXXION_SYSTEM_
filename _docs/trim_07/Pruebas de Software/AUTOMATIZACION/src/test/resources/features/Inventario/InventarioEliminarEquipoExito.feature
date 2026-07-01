# language: es

Característica: Eliminación exitosa de un equipo del inventario
  Como usuario con permisos de gestión de inventario
  Quiero eliminar equipos disponibles del catálogo
  Para mantener el inventario actualizado y consistente

  Antecedentes:
    Dado que el usuario cuenta con sesión iniciada en AXXION SYSTEM
    Y el usuario está en el módulo de inventario
    Y el usuario registra un equipo disponible para pruebas de eliminación

  @inventario @eliminar-equipo @cp05
  Escenario: Eliminar un equipo disponible confirma la acción y lo retira del listado
    Cuando el usuario elimina el equipo de prueba desde el listado
    Entonces el sistema muestra una alerta de éxito de eliminación
    Y el equipo de prueba ya no aparece en el listado
