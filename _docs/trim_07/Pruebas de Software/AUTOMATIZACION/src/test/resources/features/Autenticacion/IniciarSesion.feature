# language: es
  # Author: Cristian

  Característica: Autenticacion en la pagina Axxion System
    Como usuario del sistema
    Quiero poder realizar el inicio de sesion correctamente
    Para gestionar, procesar y acceder a datos de forma eficiente a través de internet a su información cuando sea necesario


  @IniciarSesion

  Escenario: Iniciar sesion correctamente
    Dado que el usuario se encuentra en la pagina de inicio de sesion de Axxion System
    Cuando ingrese las credenciales correctas "<usuario>" y "<contraseña>"
    Entonces se deberia verificar que el usuario haya sido autenticado correctamente
    Y redirigido a la pagina prinncipal de Axxion System
