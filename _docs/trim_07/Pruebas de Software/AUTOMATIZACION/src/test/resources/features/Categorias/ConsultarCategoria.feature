# language: es
  # Author: Cristian

Característica: Consultar Categorías
  Como usuario del sistema
  Quiero visualizar y buscar categorías
  Para encontrar información específica de manera rápida

  Antecedentes:
    Dado que se muestra la pagina de gestión de categorías
    Y que el usuario ha iniciado sesión en la aplicación correctamente

  @ConsultarCategoria @Busqueda

  Esquema del escenario: Buscar categoria por Nombre
    Cuando el usuario ingresa "<buscar_categoria>" en el campo de búsqueda
    Entonces debería ver unicamente resultados que contengan "<buscar_categoria>" en la lista
    Ejemplos:
    | buscar_categoria |
    | Iluminación |
    | Electrónicos |


  @ConsultarCategoria @FiltroPorFecha

    Esquema del escenario: Filtrar las categorias por fechas de creacion
    Cuando el usuario selecciona la fecha "<fecha>" en el filtro de fechas
    Entonces debería ver solo las categorias creadas en "<fecha>"
    Ejemplos:
      | fecha       |
      | 21/02/2026  |
      | 15/03/2026  |
