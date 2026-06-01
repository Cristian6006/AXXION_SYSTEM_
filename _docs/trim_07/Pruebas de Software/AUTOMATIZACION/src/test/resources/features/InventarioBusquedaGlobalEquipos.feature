# language: es

@inventario @cp02 @prioridad-media
Característica: Búsqueda global de equipos en inventario
  Como usuario del sistema
  Quiero acotar el listado mediante un criterio de texto
  Para localizar equipos por nombre, marca o número de serie

  Antecedentes:
    Dado que el usuario se encuentra en la pagina de inicio de sesion
    Cuando Ingrese las credenciales correctas (usuario y contrasena)
      | usuario | clave       |
      | student | Password123 |


  Escenario: La búsqueda filtra el listado por coincidencia en nombre, marca o serie
    Cuando el usuario escribe "Dell" en el campo de búsqueda de equipos
    Entonces el listado muestra únicamente equipos cuyo nombre, marca o número de serie contiene el criterio buscado
    Y el listado refleja el filtro de forma inmediata
