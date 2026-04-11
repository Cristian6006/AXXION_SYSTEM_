# Preguntas de Entrevista - Desarrollo Móvil Android con Kotlin

## Arquitectura y Diseño

### 1. Arquitectura MVVM
**Pregunta:** ¿Cuáles son las principales ventajas de usar el patrón MVVM en aplicaciones Android? ¿Cómo implementas la comunicación entre ViewModel y View?

**Respuesta esperada:** MVVM separa la lógica de negocio de la UI, facilita el testing, mejora la mantenibilidad. Se usa LiveData/Flow para comunicación reactiva.

### 2. Clean Architecture
**Pregunta:** ¿Cómo aplicas Clean Architecture en un proyecto Android? Explica las capas y sus responsabilidades.

**Respuesta esperada:** Capas: Presentation (UI/ViewModels), Domain (Use Cases), Data (Repositories/Apis). Cada capa tiene responsabilidades específicas.

## Kotlin Específico

### 3. Características Avanzadas de Kotlin
**Pregunta:** ¿Cuáles son las diferencias entre `val`, `var`, `const val` y `lateinit`? ¿Cuándo usarías cada uno?

**Respuesta esperada:**
- `val`: Inmutable, inicializada una vez
- `var`: Mutable
- `const val`: Constante en tiempo de compilación
- `lateinit`: Inicialización tardía para propiedades no-null

### 4. Null Safety
**Pregunta:** ¿Cómo maneja Kotlin la nulabilidad? Explica los operadores `?.`, `?:`, `!!`.

**Respuesta esperada:** Kotlin es null-safe por defecto. `?.` safe call, `?:` elvis operator, `!!` not-null assertion.

## Android Framework

### 5. Lifecycle Management
**Pregunta:** ¿Cómo manejas el ciclo de vida de los componentes Android? ¿Qué problemas comunes encuentras y cómo los solucionas?

**Respuesta esperada:** ViewModel sobrevive cambios de configuración, LiveData es lifecycle-aware, evitar memory leaks con disposables.

### 6. Jetpack Components
**Pregunta:** ¿Qué componentes de Android Jetpack has usado? Explica ViewModel, LiveData, Room, Navigation.

**Respuesta esperada:** ViewModel para lógica UI, LiveData para datos observables, Room para persistencia, Navigation para navegación.

## Concurrencia y Asincronía

### 7. Coroutines
**Pregunta:** ¿Cómo funcionan las coroutines en Kotlin? ¿Cuándo usarías `launch`, `async`, `withContext`?

**Respuesta esperada:** Coroutines para programación asíncrona. `launch` para fire-and-forget, `async` para resultados paralelos, `withContext` para cambiar dispatcher.

### 8. Flow vs LiveData
**Pregunta:** ¿Cuáles son las diferencias entre Flow y LiveData? ¿En qué casos usarías uno u otro?

**Respuesta esperada:** Flow es más flexible, soporta operadores Rx, LiveData es lifecycle-aware automáticamente.

## UI/UX

### 9. Material Design 3
**Pregunta:** ¿Cómo implementas Material Design 3 en una aplicación Android? ¿Qué componentes has usado?

**Respuesta esperada:** Tema dinámico, componentes como BottomNavigationView, Card, TextField, manejo de colores y tipografía.

### 10. RecyclerView Optimization
**Pregunta:** ¿Cómo optimizas un RecyclerView para listas grandes? ¿Qué técnicas de mejora de rendimiento aplicas?

**Respuesta esperada:** ViewHolder pattern, DiffUtil, pagination, image loading libraries (Glide/Coil), evitar nested scrolls.

## Persistencia de Datos

### 11. Room Database
**Pregunta:** ¿Cómo configuras Room en un proyecto Android? Explica Entities, DAOs y Database.

**Respuesta esperada:** Entity con @Entity, DAO con queries, Database con @Database y entities list.

### 12. Repository Pattern
**Pregunta:** ¿Por qué usar el patrón Repository? ¿Cómo lo implementas con Room y Retrofit?

**Respuesta esperada:** Abstrae fuente de datos, facilita testing, switching entre local/remote.

## Networking

### 13. Retrofit
**Pregunta:** ¿Cómo configuras Retrofit para llamadas a API? ¿Cómo manejas errores y respuestas?

**Respuesta esperada:** Interfaz con anotaciones, converter (Gson), interceptors para auth/logging, manejo de errores con try-catch.

### 14. API Security
**Pregunta:** ¿Cómo manejas la autenticación y seguridad en llamadas API? ¿Qué medidas de seguridad implementas?

**Respuesta esperada:** JWT tokens, refresh tokens, certificate pinning, API key management.

## Testing

### 15. Unit Testing
**Pregunta:** ¿Cómo escribes tests unitarios para ViewModels y Repositories? ¿Qué frameworks usas?

**Respuesta esperada:** JUnit, Mockito/KMock, test coroutines, in-memory database para Room.

### 16. UI Testing
**Pregunta:** ¿Cómo realizas pruebas de UI? ¿Qué herramientas usas para Espresso?

**Respuesta esperada:** Espresso para UI tests, Page Object pattern, Idling Resources.

## Performance

### 17. Memory Leaks
**Pregunta:** ¿Cómo identificas y previenes memory leaks en Android? ¿Qué herramientas usas?

**Respuesta esperada:** LeakCanary, evitar static references, proper lifecycle management, weak references.

### 18. App Bundle Size
**Pregunta:** ¿Cómo reduces el tamaño del APK/App Bundle? ¿Qué técnicas aplicas?

**Respuesta esperada:** Proguard/R8, remove unused resources, dynamic feature modules, WebP images.

## CI/CD

### 19. Build Process
**Pregunta:** ¿Cómo configuras el proceso de build con Gradle? ¿Qué optimizaciones aplicas?

**Respuesta esperada:** Build variants, product flavors, buildConfig fields, dependency management.

### 20. Code Quality
**Pregunta:** ¿Qué herramientas usas para mantener la calidad del código? (Lint, Detekt, SonarQube)

**Respuesta esperada:** Android Lint, Detekt para Kotlin, code coverage con Jacoco.

## Preguntas Situacionales

### 21. Troubleshooting
**Pregunta:** Describe un problema complejo que hayas resuelto en una app Android. ¿Qué pasos seguiste para diagnosticarlo?

**Respuesta esperada:** Análisis sistemático, uso de herramientas de debugging, revisión de logs, testing incremental.

### 22. Architecture Decision
**Pregunta:** ¿Cómo decides qué arquitectura usar para un nuevo proyecto? ¿Qué factores consideras?

**Respuesta esperada:** Complejidad del proyecto, tamaño del equipo, requerimientos de testing, tiempo de desarrollo.

### 23. Third-party Libraries
**Pregunta:** ¿Cómo decides qué librerías externas usar? ¿Qué criterios aplicas?

**Respuesta esperada:** Popularidad, mantenimiento activo, documentación, compatibilidad, tamaño, performance impact.

### 24. Legacy Code
**Pregunta:** ¿Cómo refactorizas código legacy? ¿Qué estrategias aplicas?

**Respuesta esperada:** Tests primero, cambios incrementales, identificar responsabilidades, aplicar patrones de diseño.

### 25. Team Collaboration
**Pregunta:** ¿Cómo trabajas en equipo en proyectos Android? ¿Qué prácticas de desarrollo aplicas?

**Respuesta esperada:** Code reviews, pair programming, CI/CD, documentación, stand-ups, agile methodologies.
