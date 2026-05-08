# language: es
  # Author: Cristian

Característica: Gestión de Categorías
  Como usuario del sistema
  Quiero poder agregar nuevas categorías
  Para organizar los productos del inventario

  Antecedentes:
    Dado que se muestra la pagina de gestión de categorías
    Y que el usuario ha iniciado sesión en la aplicación correctamente


  @AgregarCategorias

  Esquema del escenario: Agregar una categoría válida con éxito
    Cuando el usuario de click a la opcion "Agregar Categoria"
    Y ingrese los detalles de la nueva categoría
      | nombre   | Tipo Categoria | descripción   |
      | <nombre> | <tipoCategoria> | <descripcion> |
    Y confirme la creación de la categoría
    Entonces debería ver un mensaje de éxito "Guardado"
    Y la categoría "<nombre>" debería estar visible en la lista de categorías

    Ejemplos:
      | nombre       | tipoCategoria | descripcion                             |
      | Electronicos | Pantalla      | Dispositivos electronicos y gadgets     |
      | Video        | Equipos       | Pantalla protectores y equipos de video |

