# language: es

Característica: Borrar Categorías
  Como administrador del sistema
  Quiero poder borrar categorías existentes
  Para reorganizar la información cuando sea necesario

  Antecedentes:
    Dado que el administrador ha iniciado sesión en la aplicación
      | usuario       | clave        |
      | p@example.com | contraseña12 |

    @borrarCategoria

    Esquema del escenario: Borrar una categoría correctamente
      Dado que estoy en la sección de gestión de categorías
      Cuando selecciono la categoría "<categoria>" y hago clic en el icono de borrado
      Y confirmo el borrado de la categoría
      Entonces debería ver un mensaje de éxito "Guardado"
      Y la categoría "<categoria>" debería reflejar los cambios en la lista

      Ejemplos:
        | categoria     |
        | Electrónicos  |
        | Hogar         |