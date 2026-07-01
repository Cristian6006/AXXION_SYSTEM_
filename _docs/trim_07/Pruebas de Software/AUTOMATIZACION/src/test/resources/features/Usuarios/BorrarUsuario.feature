# language: es
  # Author: Cristian
Característica: Agregar Usuario
  Como administrador del sistema
  Quiero eliminar usuarios
  Para mantener actualizado el registro del sistema.

  Antecedentes:
    Dado el administrador inicie sesion con las credenciales correctas
    Y que se encuentre en la pagina de gestion gestion de usuarios

  @EliminarUsuario

  Escenario: Eliminar un usuario
    Dado el administrador crea un nuevo usuario
    Cuando elimina a el usuario del sistema
    Entonces deberia ver que el usuario no exista en la lista