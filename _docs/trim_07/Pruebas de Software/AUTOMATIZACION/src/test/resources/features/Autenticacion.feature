# language: es

@autenticacion
Característica: Autenticación en la pagina de login
  Como usuario
  Quiero iniciar sesión con mis credenciales
  Para acceder a las funcionalidades de la aplicación

  Escenario: verificar la autenticación exitosa en la página de facebook
    Dado que el usuario se encuentra en la pagina de inicio de sesion
    Cuando Ingrese las credenciales correctas (usuario y contrasena)
      | usuario | clave       |
      | student | Password123 |
    Entonces se debe verificar que el usuario haya sido autenticado correctamente...
