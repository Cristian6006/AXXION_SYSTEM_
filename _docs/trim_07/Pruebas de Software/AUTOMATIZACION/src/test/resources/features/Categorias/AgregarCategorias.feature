# language: es
  # Author: Cristian

Característica: Gestión de Categorías
  Como usuario del sistema
  Quiero poder agregar nuevas categorías
  Para organizar los productos del inventario

  Antecedentes:
    Dado el administrador inicie sesion con las credenciales correctas
    Y se encuentre en la pagina de gestion gestion de categorias

  @AgregarCategorias

  Escenario: Agregar una categoría válida con éxito
    Cuando este crea una nueva categoría
    Entonces debería ver un mensaje de éxito
    Y la categoría debería estar visible en la lista de categorías




