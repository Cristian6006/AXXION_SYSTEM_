# language: es

@inventario @cp03 @prioridad-media
Característica: Filtrado de equipos por estado en inventario
  Como usuario del sistema
  Quiero filtrar el listado por estado operativo
  Para revisar únicamente los equipos en una situación determinada

  Antecedentes:
    Dado que el usuario se encuentra en la pagina de inicio de sesion
    Cuando Ingrese las credenciales correctas (usuario y contrasena)
      | usuario | clave       |
      | student | Password123 |


  Escenario: El filtro por estado alquilado acota el listado a equipos rentados
    Cuando el usuario selecciona el estado "Alquilado" en el filtro del listado
    Entonces el listado muestra únicamente equipos en estado alquilado
