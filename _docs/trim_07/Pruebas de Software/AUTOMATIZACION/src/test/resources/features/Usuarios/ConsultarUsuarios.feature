# language: es
  # Author: Cristian

Característica: Consultar Categorías
  Como usuario del sistema
  Quiero visualizar y buscar usuarios
  Para encontrar información específica de manera rápida

  Antecedentes:
    Dado el administrador inicie sesion con las credenciales correctas
    Y que se encuentre en la pagina de gestion gestion de usuarios

  @ConsultarUsuario
  Escenario: Buscar usuario por nombre
    Dado el administrador crea un nuevo usuario
    Cuando ingresa el nombre del usuario en el buscador
    Entonces deberia ver unicamente el usuario con su nombre