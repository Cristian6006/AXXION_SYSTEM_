# language: es


Característica: Registro exitoso de un equipo en inventario
  Como usuario con permisos de gestión de inventario
  Quiero registrar equipos nuevos con sus datos obligatorios
  Para ampliar el catálogo con información consistente

  Antecedentes:
    Dado que el usuario se encuentra en la pagina de inicio de sesion
    Cuando Ingrese las credenciales correctas (usuario y contrasena)
      | usuario | clave       |
      | student | Password123 |

@AgregarEquipo
  Escenario: Registrar equipo nuevo exitosamente

    Cuando el usuario registra un equipo nuevo con nombre "Proyector portátil", marca "Epson", serie "SN-EPS-2026-001" y tarifa diaria "18.50"
    Entonces el sistema confirma el registro del equipo "Proyector portátil" con una alerta de exito, cierra el modal y lo muestra en la primera posicion del listado
