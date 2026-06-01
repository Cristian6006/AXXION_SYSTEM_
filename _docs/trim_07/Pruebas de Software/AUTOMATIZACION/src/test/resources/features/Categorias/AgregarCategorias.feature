# language: es
  # Author: Cristian

Característica: Gestión de Categorías
  Como usuario del sistema
  Quiero poder agregar nuevas categorías
  Para organizar los productos del inventario

  Antecedentes:
    Dado que el usuario se encuentra en la pagina de inicio de sesion de Axxion System
    Y inicie sesion con las credenciales (usuario y contraseña)
      | usuario || contraseña |
      | p@example.com || Us123456   |
    Y que se muestra la pagina de gestión de categorías


  @AgregarCategorias

  Escenario: Agregar una categoría válida con éxito
    Cuando el usuario crea una nueva categoría
      | nombre   | tipoCategoria | descripcion   |
      | Electronicos | Equipos    | Dispositivos electronicos y gadgets    |
    Entonces debería ver un mensaje de éxito "Guardado"
    Y la categoría "<nombre>" debería estar visible en la lista de categorías



