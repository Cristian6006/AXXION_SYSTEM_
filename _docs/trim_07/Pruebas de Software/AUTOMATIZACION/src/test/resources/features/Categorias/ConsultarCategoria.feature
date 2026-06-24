# language: es
  # Author: Cristian

Característica: Consultar Categorías
  Como usuario del sistema
  Quiero visualizar y buscar categorías
  Para encontrar información específica de manera rápida

  Antecedentes:
    Dado el administrador inicie sesion con las credenciales correctas
    Y se encuentre en la pagina de gestion gestion de categorias

  @ConsultarCategoria @Busqueda

  Escenario: Buscar categoria por Nombre
    Dado este crea una nueva categoría
    Cuando ingrese el nombre de la categoria buscada
    Entonces debería ver unicamente resultados que contengan la categoria buscada


  @ConsultarCategoria @FiltroPorFecha

    Escenario: Filtrar las categorias por fechas de creacion
    Dado este crea una nueva categoría
    Cuando el usuario selecciona la fecha en el filtro de fechas
    Entonces debería ver solo las categorias creadas en la fecha seleccionada

