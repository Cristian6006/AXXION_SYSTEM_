# language: es
  # Author: Cristian

Característica: Editar Categorías
  Como usuario del sistema
  Quiero poder editar categorías existentes
  Para actualizar su información cuando sea necesario

  Antecedentes:
    Dado el administrador inicie sesion con las credenciales correctas
    Y se encuentre en la pagina de gestion gestion de categorias

  @EditarCategoria

  Escenario: Editar una categoría correctamente
    Dado este crea una nueva categoría
    Cuando el usuario actualiza el nombre de la categoría
    Entonces debería ver un mensaje de éxito
    Y que la categoría debería estar visible en la tabla




