# language: es
  # Author: Cristian

Característica: Borrar Categorías
  Como usuario del sistema
  Quiero poder borrar categorías existentes
  Para reorganizar la información cuando sea necesario

  Antecedentes:
    Dado que el usuario se encuentra en la pagina de inicio de sesion de Axxion System
    Y inicie sesion con las credenciales (usuario y contraseña)
      | usuario || contraseña |
      | p@example.com || Us123456   |
    Y que se muestra la pagina de gestión de categorías

    @BorrarCategoria

    Escenario: Borrar una categoría correctamente
      Cuando el usuario elimina la categoría por su nombre
        | nombre |
        | Electronicos |
      Entonces debería ver un mensaje de éxito
      Y que la categoría "Electronicos" ya no debería existir en el sistema
