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

  Esquema del escenario: Editar una categoría correctamente
    Cuando el usuario selecciona la categoría "<categoria>" y hace click en el icono de edición
    Y ingresa los detalles de la categoría editada
      | nombre         | Tipo Categoria   | descripción   |
      | <nuevo_nombre> | <tipo_categoria> | <descripcion> |
    Y confirma la edición de la categoría
    Entonces debería ver un mensaje de éxito "Guardado"
    Y la categoría "<nuevo_nombre>" debería evidenciar cambios en la lista

    Ejemplos:
      | categoria     | nuevo_nombre | tipo_categoria | descripcion                              |
      | Electrónicos  | Video        | Pantallas      | pantalla modular de gran resolución      |
      | Hogar         | Muebles      | Oficina        | muebles ergonómicos de oficina           |

