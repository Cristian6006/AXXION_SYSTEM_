# Paso a Paso para Pruebas de QA Automatizadas (Serenity BDD + Cucumber)

Este documento detalla los pasos manuales para ejecutar las pruebas de las funcionalidades de la aplicación AXXION SYSTEM, utilizando el Monitoreo de Chrome DevTools (MCP) para la captura de evidencias.

**URL de la Aplicación:** http://localhost:5173/
**Credenciales de Login:**
*   **Email:** c@example.com
*   **Contraseña:** Su12345678

---

## 1. Login en AXXION SYSTEM (Pre-requisito para todas las Features)

**Objetivo:** Iniciar sesión como administrador en la aplicación.

**Pasos:**

1.  **Abrir el navegador y navegar a la URL:**
    *   Abre una nueva pestaña en Chrome.
    *   Ingresa la URL: `http://localhost:5173/`
    *   **Evidencia (MCP):** `mcp__chrome_devtools__take_screenshot(filePath='evidencias_mcp/login_01_pagina_login.png')`
2.  **Ingresar credenciales:**
    *   En el campo de "Email", ingresa: `c@example.com`
    *   En el campo de "Contraseña", ingresa: `Su12345678`
    *   **Evidencia (MCP):** `mcp__chrome_devtools__take_screenshot(filePath='evidencias_mcp/login_02_credenciales_ingresadas.png')`
3.  **Hacer clic en el botón de Login:**
    *   Localiza y haz clic en el botón "Iniciar Sesión" o similar.
    *   **Evidencia (MCP):** `mcp__chrome_devtools__take_screenshot(filePath='evidencias_mcp/login_03_sesion_iniciada.png')`
4.  **Verificar inicio de sesión exitoso:**
    *   Asegúrate de que la página de inicio o el dashboard de la aplicación se muestre correctamente.

---

## 2. Feature: Conversión de cotización a renta efectiva (GenerarRenta.feature)

**Objetivo:** Transformar una cotización aprobada en una renta programada.

**Antecedentes:**

*   El administrador ya tiene sesión iniciada.
*   Existe una cotización en estado "Borrador" para el cliente "Carlos Sanchez".

**Pasos:**

1.  **Navegar a la sección de Cotizaciones:**
    *   En el menú principal o barra de navegación, busca y haz clic en la opción "Cotizaciones" o "Gestión de Cotizaciones".
    *   **Evidencia (MCP):** `mcp__chrome_devtools__take_screenshot(filePath='evidencias_mcp/generar_renta_01_seccion_cotizaciones.png')`
2.  **Buscar la cotización de "Carlos Sanchez":**
    *   Utiliza el campo de búsqueda o los filtros para encontrar la cotización asociada al cliente "Carlos Sanchez" y que esté en estado "Borrador". (Asumimos que hay un filtro por cliente y estado).
    *   **Evidencia (MCP):** `mcp__chrome_devtools__take_screenshot(filePath='evidencias_mcp/generar_renta_02_cotizacion_encontrada.png')`
3.  **Visualizar el detalle de la cotización:**
    *   Haz clic en la cotización encontrada para ver sus detalles.
    *   **Evidencia (MCP):** `mcp__chrome_devtools__take_screenshot(filePath='evidencias_mcp/generar_renta_03_detalle_cotizacion.png')`
4.  **Convertir la cotización en renta:**
    *   Dentro de la vista de detalles de la cotización, busca un botón o acción que diga "Convertir a Renta", "Generar Renta" o similar. Haz clic en él.
    *   **Evidencia (MCP):** `mcp__chrome_devtools__take_screenshot(filePath='evidencias_mcp/generar_renta_04_convertir_a_renta.png')`
5.  **Verificar el estado de la nueva renta:**
    *   Después de la conversión, el sistema debería redirigirte a la vista de la nueva renta o mostrar una confirmación.
    *   Verifica que el estado de la renta sea "Programada".
    *   **Evidencia (MCP):** `mcp__chrome_devtools__take_screenshot(filePath='evidencias_mcp/generar_renta_05_renta_programada.png')`
6.  **Verificar la referencia a la cotización original:**
    *   Asegúrate de que la nueva renta muestre una referencia o enlace a la cotización de origen.
    *   **Evidencia (MCP):** `mcp__chrome_devtools__take_screenshot(filePath='evidencias_mcp/generar_renta_06_referencia_cotizacion.png')`
7.  **Verificar vinculación de equipos al cronograma del cliente:**
    *   Navega a la sección de "Cronograma" o "Calendario" del cliente "Carlos Sanchez" (o a la sección de detalles de la renta si muestra los equipos).
    *   Confirma que los equipos de la cotización original estén listados y vinculados a este cliente.
    *   **Evidencia (MCP):** `mcp__chrome_devtools__take_screenshot(filePath='evidencias_mcp/generar_renta_07_equipos_vinculados.png')`
