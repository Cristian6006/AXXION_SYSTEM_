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

    Esquema del escenario: Borrar una categoría correctamente
      Cuando el usuario selecciona la categoría "<categoria>" y hace click en el icono de borrado
      Y confirma el borrado de la categoría
      Entonces debería ver un mensaje de éxito "Guardado"
      Y la categoría "<categoria>" debería haber desaparecido de la lista

      Ejemplos:
        | categoria     |
        | Electrónicos  |
        | Hogar         |