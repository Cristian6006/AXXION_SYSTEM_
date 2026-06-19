# language: es
  # Author: Cristian

Característica: Editar Categorías
  Como usuario del sistema
  Quiero poder editar categorías existentes
  Para actualizar su información cuando sea necesario

  Antecedentes:
    Dado que el usuario se encuentra en la pagina de inicio de sesion de Axxion System
    Y inicie sesion con las credenciales (usuario y contraseña)
      | usuario || contraseña |
      | p@example.com || Us123456   |
    Y que se muestra la pagina de gestión de categorías

  @EditarCategoria

  Esquema del escenario: Editar una categoría correctamente
    Cuando el usuario actualiza el nombre de la categoría por el nuevo nombre
      | nombre || nuevoNombre |
      | TestAutomatizacion2 || TestAutomatizacion |
    Entonces debería ver un mensaje de éxito
    Y que la categoría "<nuevoNombre>" debería estar visible en la tabla
    Ejemplos:
    | nuevoNombre |
    | TestAutomatizacion |



