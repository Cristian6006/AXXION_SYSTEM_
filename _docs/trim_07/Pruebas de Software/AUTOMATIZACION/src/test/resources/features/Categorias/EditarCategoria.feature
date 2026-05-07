# language: es
  # Author: Cristian

Característica: Editar Categorías
  Como administrador del sistema
  Quiero poder editar categorías existentes
  Para actualizar su información cuando sea necesario

  Antecedentes:
    Dado que el administrador ha iniciado sesión en la aplicación
      | usuario       | clave        |
      | p@example.com | contraseña12 |

  @editarCategoria

  Esquema del escenario: Editar una categoría correctamente
    Dado que estoy en la sección de gestión de categorías
    Cuando selecciono la categoría "<categoria>" y hago clic en el icono de edición
    Y ingreso los detalles de la categoría editada
      | nombre         | Tipo Categoria   | descripción   |
      | <nuevo_nombre> | <tipo_categoria> | <descripcion> |
    Y confirmo la edición de la categoría
    Entonces debería ver un mensaje de éxito "Guardado"
    Y la categoría "<nuevo_nombre>" debería reflejar los cambios en la lista

    Ejemplos:
      | categoria     | nuevo_nombre | tipo_categoria | descripcion                              |
      | Electrónicos  | Video        | Pantallas      | pantalla modular de gran resolución      |
      | Hogar         | Muebles      | Oficina        | muebles ergonómicos de oficina           |

