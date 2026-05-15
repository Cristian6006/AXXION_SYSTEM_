# language: es
  # Author: Cristian

Característica: Editar Categorías
  Como usuario del sistema
  Quiero poder editar categorías existentes
  Para actualizar su información cuando sea necesario

  Antecedentes:
    Dado que se muestra la pagina de gestión de categorías
    Y que el usuario ha iniciado sesión en la aplicación correctamente

  @EditarCategoria

  Escenario: Editar una categoría correctamente
    Cuando el usuario actualiza la categoría "Tecnología" al nuevo nombre "Electrónica"
    Entonces debería ver un mensaje de éxito "Guardado"
    Y que la categoría "Electrónica" debería estar visible en la tabla



