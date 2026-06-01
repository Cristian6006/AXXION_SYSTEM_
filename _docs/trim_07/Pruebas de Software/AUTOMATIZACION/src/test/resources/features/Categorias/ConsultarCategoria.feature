# language: es
  # Author: Cristian

Característica: Consultar Categorías
  Como usuario del sistema
  Quiero visualizar y buscar categorías
  Para encontrar información específica de manera rápida

  Antecedentes:
    Dado que el usuario se encuentra en la pagina de inicio de sesion de Axxion System
    Y inicie sesion con las credenciales (usuario y contraseña)
      | usuario || contraseña |
      | p@example.com || Us123456   |
    Y que se muestra la pagina de gestión de categorías

  @ConsultarCategoria @Busqueda

  Esquema del escenario: Buscar categoria por Nombre
    Cuando el usuario ingresa el nombre de la categoria "<palabra_buscada>"
      | nombre |
      | Iluminación |
    Entonces debería ver unicamente resultados que contengan "<palabra_buscada>"

    Ejemplos:
      | palabra_buscada |
      | Iluminación     |



  @ConsultarCategoria @FiltroPorFecha

    Escenario: Filtrar las categorias por fechas de creacion
    Cuando el usuario selecciona la fecha en el filtro de fechas
      | fecha       |
      | 21/02/2026  |
    Entonces debería ver solo las categorias creadas en la fecha seleccionada

