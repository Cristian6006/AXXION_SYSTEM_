# language: es

Característica: Consultar Categorías
  Como administrador del sistema
  Quiero visualizar y buscar categorías
  Para encontrar información específica de manera rápida

  Antecedentes:
    Dado que el administrador ha iniciado sesión en la aplicación
      | usuario       | clave        |
      | p@example.com | contraseña12 |

  @consultarCategoria

  Esquema del escenario: Consultar categorías con filtros y búsqueda
    Dado que estoy en la sección de gestión de categorías
    Cuando ingreso "<buscar_categoria>" en el campo de búsqueda
    Entonces debería visualizar la categoría específica "<buscar_categoria>" en la lista de categorías

    Cuando selecciono la fecha "<fecha>" en el filtro de fechas
    Entonces debería visualizar la categoría específica filtrada por la fecha "<fecha>"

    Ejemplos:
      | buscar_categoria | fecha       |
      | Iluminación      | 21/02/2026  |
      | Electrónicos     | 15/03/2026  |
