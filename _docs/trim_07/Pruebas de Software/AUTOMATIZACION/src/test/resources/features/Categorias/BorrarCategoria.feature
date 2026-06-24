# language: es
  # Author: Cristian

Característica: Borrar Categorías
  Como usuario del sistema
  Quiero poder borrar categorías existentes
  Para reorganizar la información cuando sea necesario

  Antecedentes:
    Dado el administrador inicie sesion con las credenciales correctas
    Y se encuentre en la pagina de gestion gestion de categorias

    @BorrarCategoria

    Escenario: Borrar una categoría correctamente
      Dado este crea una nueva categoría
      Cuando el usuario elimina la categoría por su nombre
      Entonces debería ver un mensaje de éxito
      Y que la categoría ya no debería existir en el sistema
