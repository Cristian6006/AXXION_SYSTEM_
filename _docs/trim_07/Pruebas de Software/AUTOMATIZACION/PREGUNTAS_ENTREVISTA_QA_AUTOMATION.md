# Preguntas de entrevista — QA Automation (Serenity BDD + Java + Gradle)

Documento de referencia para entrevistas técnicas alineadas con el proyecto **AXXION SYSTEM** y automatización E2E con Screenplay.

---

## 1. Fundamentos de automatización

1. ¿Cuál es la diferencia entre pruebas unitarias, de integración y end-to-end?
2. ¿Qué ventajas y desventajas tiene automatizar pruebas en la UI frente a pruebas de API?
3. ¿Qué criterios usarías para decidir qué casos automatizar primero?
4. ¿Cómo defines un caso de prueba mantenible y estable en el tiempo?
5. ¿Qué es el patrón Page Object y qué problemas resuelve?

---

## 2. Serenity BDD y Screenplay

6. Explica el patrón **Screenplay**: Actor, Task, Question y Ability.
7. ¿Por qué Serenity separa **Tasks** de **Questions**?
8. ¿Qué es `OnStage.theActorInTheSpotlight()` y cuándo lo usarías?
9. ¿Para qué sirve `actor.remember()` y `actor.recall()` en escenarios con varios pasos?
10. ¿Cómo se integran Serenity Reports con Cucumber?
11. ¿Qué diferencia hay entre `Target`, `PageObject` y `@DefaultUrl` en Serenity?
12. ¿Cuándo usarías `WaitUntil` en lugar de `Thread.sleep()`?

---

## 3. Cucumber y Gherkin

13. ¿Qué es Gherkin y por qué se escribe en lenguaje de negocio?
14. ¿Cuál es la diferencia entre `Antecedentes`, `Escenario` y `Esquema del escenario`?
15. ¿Cómo mapeas un paso en español con `@Cuando("el usuario agrega el equipo {string}...")`?
16. ¿Qué es `UndefinedStepException` y cómo lo resuelves?
17. ¿Cómo compartes pasos entre varios archivos `.feature` sin duplicar código?
18. ¿Qué son los `DataTable` en Cucumber y un caso de uso en inventario?

---

## 4. Java, Gradle y ejecución

19. ¿Cómo ejecutas un solo escenario con tag `@cp04` usando Gradle?
20. ¿Qué hace `useJUnitPlatform()` en `build.gradle.kts`?
21. ¿Para qué sirve la tarea `aggregate` de Serenity?
22. ¿Cómo configuras credenciales y URL base sin hardcodearlas en el código?
23. ¿Qué es el glue code en Cucumber y qué valor tiene en este proyecto?

---

## 5. Selenium y estabilidad

24. ¿Qué es una espera implícita vs explícita vs fluida (FluentWait)?
25. ¿Cómo localizarías el botón **Convertir a Renta** de forma resiliente?
26. ¿Qué harías ante un `NoSuchElementException` intermitente?
27. ¿Por qué conviene usar `contains(normalize-space(.), 'texto')` en XPath?
28. ¿Cómo manejarías modales, drawers y elementos fuera del viewport?

---

## 6. Diseño de framework (caso AXXION)

29. Describe el flujo **Inventario → Carrito → Cotización → Renta** en pasos de negocio.
30. ¿Cómo modelarías un `Equipo` y una `SolicitudCotizacion` como objetos de dominio de prueba?
31. ¿Dónde ubicarías los selectores: ¿en Step Definitions, Tasks o UI classes?
32. ¿Cómo evitarías que un test de renta dependa manualmente de otro test de cotización?
33. ¿Qué datos de prueba generarías con **DataFaker** y cuáles dejarías fijos?
34. ¿Cómo validarías que una cotización quedó en estado **Borrador**?

---

## 7. CI/CD y reportes

35. ¿Qué información revisas primero en un reporte Serenity fallido?
36. ¿Cómo integrarías estas pruebas en GitHub Actions o Jenkins?
37. ¿Qué estrategia usarías para ejecutar pruebas headless en Linux?
38. ¿Cómo aislarías fallos por entorno (Render cold start, datos compartidos)?

---

## 8. Preguntas situacionales (con respuesta esperada)

39. **El login pasa pero el inventario no carga equipos.** ¿Qué investigas?
   - Red/API, token, timeout, selectores, estado del backend Render.

40. **El paso de cotización falla porque el botón está disabled.** ¿Causa probable?
   - Cliente o fechas no diligenciados; validar `datetime-local` y eventos Vue.

41. **Un test pasa localmente y falla en CI.** ¿Qué revisas?
   - Headless, resolución, timeouts, credenciales, datos compartidos, versión de ChromeDriver.

42. **¿Cómo refactorizarías Step Definitions que crecen demasiado?**
   - Extraer Tasks, Helpers, Questions; un paso = una intención de negocio.

43. **¿Cómo probarías la eliminación de un equipo sin afectar datos productivos?**
   - Crear equipo único en antecedentes, eliminarlo al final, usar entorno de pruebas.

44. **¿Qué es el principio de responsabilidad única aplicado a `AgregarEquipo` vs `GenerarCotizacionStepDefinition`?**
   - Task ejecuta interacción; Step Definition orquesta; Question valida.

---

## 9. Ejercicio práctico sugerido en entrevista

> Dado el escenario: *"El usuario agrega example 1 al carrito y genera cotización para Carlos Sanchez"*, escribe:
> 1. Un paso Gherkin.
> 2. La firma del método Step Definition.
> 3. Una Task Screenplay.
> 4. Una Question de verificación.

---

## 10. Glosario rápido del proyecto

| Término | Significado en AXXION |
|--------|------------------------|
| Equipo | Ítem del inventario rentable |
| Carrito de cotización | Drawer para armar solicitud |
| Cotización | Documento en estado Borrador con items y total |
| Renta | Contrato programado derivado de la cotización |
| Actor | Usuario automatizado (`administrador`) |

---

*Generado para el módulo de automatización E2E — AXXION SYSTEM.*
