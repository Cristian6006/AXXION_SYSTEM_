# language: es

Característica: Gestión de Categorías
  Como administrador del sistema
  Quiero poder agregar nuevas categorías
  Para organizar los productos del inventario

  Antecedentes:
    Dado que el administrador ha iniciado sesión en la aplicación
      | usuario       | clave        |
      | p@example.com | contraseña12 |

  @AgregarCategoriaExitoso

  Esquema del escenario: Agregar una categoría válida con éxito
    Dado que estoy en la sección de gestión de categorías
    Cuando hago click a la opcion "Agregar Categoria"
    Y ingreso los detalles de la nueva categoría
      | nombre   | Tipo Categoria | descripción   |
      | <nombre> | <tipoCategoria> | <descripcion> |
    Y confirmo la creación de la categoría
    Entonces debería ver un mensaje de éxito "Guardado"
    Y la categoría "<nombre>" debería estar visible en la lista de categorías

    Ejemplos:
      | nombre       | tipoCategoria | descripcion |
      | Electronicos | Pantalla | Dispositivos electronicos y gadgets |
      | Video         | Equipos | Pantalla protectores y equipos de video |

