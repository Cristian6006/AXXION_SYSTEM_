# language: es
  # Author: Cristian

Característica: Borrar Categorías
  Como usuario del sistema
  Quiero poder borrar categorías existentes
  Para reorganizar la información cuando sea necesario

  Antecedentes:
    Dado que se muestra la pagina de gestión de categorías
    Y que el usuario ha iniciado sesión en la aplicación correctamente

    @BorrarCategoria

    Escenario: Borrar una categoría correctamente
      Cuando el usuario elimina la categoría "Electrónica"
      Entonces debería ver un mensaje de éxito "Guardado"
      Y que la categoría "Electrónica" ya no debería existir en el sistema
