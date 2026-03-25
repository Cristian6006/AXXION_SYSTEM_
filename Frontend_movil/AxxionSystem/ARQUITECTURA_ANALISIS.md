# Análisis de Arquitectura y Flujo de Datos

## 1. Arquitectura Detectada: MVVM + Clean Architecture

El proyecto utiliza una **arquitectura MVVM** (Model-View-ViewModel) con principios de **Clean Architecture**. A continuación se detallan las capas identificadas:

---

## 2. Capas de la Arquitectura

### 📱 Capa de Presentación (UI Layer)
| Componente | Descripción |
|------------|-------------|
| [`MainActivity.kt`](app/src/main/java/com/example/axxionsystem/MainActivity.kt:12) | Activity principal con SplashScreen |
| [`LoginFragment.kt`](app/src/main/java/com/example/axxionsystem/ui/auth/LoginFragment.kt:26) | Fragment de autenticación |
| [`HomeFragment.kt`](app/src/main/java/com/example/axxionsystem/ui/home/HomeFragment.kt:25) | Fragment principal post-login |

### 🧠 Capa de ViewModel (Lógica de Negocio)
| Componente | Descripción |
|------------|-------------|
| [`AuthViewModel.kt`](app/src/main/java/com/example/axxionsystem/ui/auth/AuthViewModel.kt:23) | Gestiona estado de autenticación con LiveData |
| [`AuthViewModelFactory.kt`](app/src/main/java/com/example/axxionsystem/ui/auth/AuthViewModelFactory.kt:13) | Factory para inyección manual de dependencias |

### 📦 Capa de Datos (Data Layer)
| Componente | Descripción |
|------------|-------------|
| [`AuthRepository.kt`](app/src/main/java/com/example/axxionsystem/data/repository/AuthRepository.kt:14) | Abstracción del acceso a datos |
| [`ApiService.kt`](app/src/main/java/com/example/axxionsystem/data/api/ApiService.kt:18) | Contrato de endpoints Retrofit |
| [`RetrofitClient.kt`](app/src/main/java/com/example/axxionsystem/data/api/RetrofitClient.kt:25) | Configuración de red singleton |
| [`AuthInterceptor.kt`](app/src/main/java/com/example/axxionsystem/data/api/AuthInterceptor.kt:13) | Adjunta JWT a cada request |
| [`TokenAuthenticator.kt`](app/src/main/java/com/example/axxionsystem/data/api/TokenAuthenticator.kt:18) | Renueva token automáticamente |

### 🔐 Capa de Utilidades
| Componente | Descripción |
|------------|-------------|
| [`SessionManager.kt`](app/src/main/java/com/example/axxionsystem/util/SessionManager.kt:17) | Gestiona sesión con EncryptedSharedPreferences |
| [`AuthModels.kt`](app/src/main/java/com/example/axxionsystem/data/model/AuthModels.kt:10) | DTOs de autenticación |

---

## 3. Flujo de Datos (Data Flow)

### 🔄 Flujo de Autenticación (Login)

```
┌─────────────────┐     ┌──────────────────┐     ┌───────────────────┐
│   LoginFragment │────▶│  AuthViewModel   │────▶│  AuthRepository   │
│   (UI - View)   │     │  (ViewModel)     │     │   (Repository)    │
└─────────────────┘     └──────────────────┘     └───────────────────┘
                                                          │
                                                          ▼
                                                 ┌───────────────────┐
                                                 │    ApiService     │
                                                 │   (Retrofit)      │
                                                 └───────────────────┘
                                                          │
                                                          ▼
                                                 ┌───────────────────┐
                                                 │  Backend Server   │
                                                 │  (10.0.2.2:8080)  │
                                                 └───────────────────┘
```

### 📝 Paso a Paso del Login:

1. **Usuario** ingresa email y password en [`LoginFragment`](app/src/main/java/com/example/axxionsystem/ui/auth/LoginFragment.kt:64)
2. **LoginFragment** llama a [`authViewModel.login(email, password)`](app/src/main/java/com/example/axxionsystem/ui/auth/AuthViewModel.kt:34)
3. **AuthViewModel**:
   - Valida que los campos no estén vacíos
   - Crea [`LoginRequest`](app/src/main/java/com/example/axxionsystem/data/model/AuthModels.kt:10)
   - Llama a [`repository.login(request)`](app/src/main/java/com/example/axxionsystem/data/repository/AuthRepository.kt:16)
4. **AuthRepository** delega a [`apiService.login()`](app/src/main/java/com/example/axxionsystem/data/api/ApiService.kt:21)
5. **Retrofit** ejecuta la petición HTTP POST a `/api/auth/login`
6. **Respuesta**:
   - ✅ Éxito: [`AuthResponse`](app/src/main/java/com/example/axxionsystem/data/model/AuthModels.kt:16) con `accessToken`
   - ❌ Error: Excepción capturada
7. **AuthViewModel** actualiza [`loginResult` LiveData](app/src/main/java/com/example/axxionsystem/ui/auth/AuthViewModel.kt:25)
8. **LoginFragment** observa el resultado y:
   - Guarda token: [`sessionManager.saveAuthToken()`](app/src/main/java/com/example/axxionsystem/util/SessionManager.kt:33)
   - Navega a Home: [`findNavController().navigate()`](app/src/main/java/com/example/axxionsystem/ui/auth/LoginFragment.kt:79)

### 🔄 Flujo de Requests Autenticados

```
┌─────────────────┐     ┌──────────────────┐     ┌───────────────────┐
│   HomeFragment  │────▶│  AuthViewModel   │────▶│  AuthRepository   │
└─────────────────┘     └──────────────────┘     └───────────────────┘
                                                         │
                                                         ▼
                                                ┌───────────────────┐
                                                │  RetrofitClient   │
                                                └───────────────────┘
                                                         │
                                ┌──────────────────────────┼──────────────────────────┐
                                ▼                          ▼                          ▼
                     ┌──────────────────┐    ┌──────────────────┐    ┌──────────────────┐
                     │  AuthInterceptor │    │  TokenAuthentic. │    │    CookieJar     │
                     │  (Adjunta Bearer) │    │  (Renueva token) │    │  (Sesión cookies)│
                     └──────────────────┘    └──────────────────┘    └──────────────────┘
                                │                          │                          │
                                └──────────────────────────┼──────────────────────────┘
                                                         ▼
                                                ┌───────────────────┐
                                                │  Backend Server   │
                                                └───────────────────┘
```

### 📋 Detalle del AuthInterceptor:
- Lee el token de [`SessionManager`](app/src/main/java/com/example/axxionsystem/util/SessionManager.kt:46)
- Añade header: `Authorization: Bearer <token>`

### 🔄 Detalle del TokenAuthenticator:
- Se activa cuando el servidor retorna **401 Unauthorized**
- Llama al endpoint `/api/auth/refresh` de forma síncrona
- Si obtiene nuevo token, lo guarda y reintenta el request
- Si falla, limpia la sesión

---

## 4. Gestión de Sesión Segura

```
┌─────────────────────────────────────────────────────────────┐
│                    SessionManager                           │
│  (EncryptedSharedPreferences - AES256)                    │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐         │
│  │ JWT_TOKEN   │  │ USER_ROLE   │  │ DEVICE_ID   │         │
│  │ (Encriptado)│  │ (Encriptado)│  │ (Encriptado)│         │
│  └─────────────┘  └─────────────┘  └─────────────┘         │
└─────────────────────────────────────────────────────────────┘
```

---

## 5. Navegación (Navigation Component)

```
┌──────────────┐      action_loginFragment_to_homeFragment      ┌──────────────┐
│ loginFragment│ ──────────────────────────────────────────────▶│ homeFragment │
│  (Start)     │                                                │              │
└──────────────┘      action_homeFragment_to_loginFragment      └──────────────┘
       ▲                                                                 │
       └─────────────────────────────────────────────────────────────────┘
```

---

## 6. Stack Tecnológico

| Categoría | Tecnología |
|-----------|------------|
| **Lenguaje** | Kotlin |
| **UI** | Material Design 3, ViewBinding |
| **Arquitectura** | MVVM + Clean Architecture |
| **Networking** | Retrofit 2 + OkHttp 4 |
| **Serialización** | Gson |
| **Navegación** | Jetpack Navigation Component |
| **Ciclo de Vida** | ViewModel + LiveData |
| **Seguridad** | AndroidX Security Crypto (EncryptedSharedPreferences) |
| **Biometría** | AndroidX Biometric |
| **Splash** | AndroidX SplashScreen |

---

## 7. Diagrama de Capas (Clean Architecture)

```
┌────────────────────────────────────────────────────────────────┐
│                      PRESENTATION LAYER                        │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │ LoginFragment│  │HomeFragment │  │   AuthViewModel     │  │
│  └─────────────┘  └─────────────┘  └─────────────────────┘  │
└────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌────────────────────────────────────────────────────────────────┐
│                       DOMAIN LAYER                             │
│  (Lógica de negocio - en ViewModel)                           │
│  ┌─────────────────────────────────────────────────────────┐  │
│  │ - Validación de inputs                                  │  │
│  │ - Manejo de errores                                     │  │
│  │ - Transformación de datos                               │  │
│  └─────────────────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌────────────────────────────────────────────────────────────────┐
│                        DATA LAYER                              │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │ AuthRepository│ │  ApiService │ │  RetrofitClient     │  │
│  └─────────────┘  └─────────────┘  └─────────────────────┘  │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │AuthInterceptor│ │TokenAuthent.│ │   SessionManager    │  │
│  └─────────────┘  └─────────────┘  └─────────────────────┘  │
└────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌────────────────────────────────────────────────────────────────┐
│                     EXTERNAL LAYER                             │
│  ┌─────────────────────┐  ┌─────────────────────────────┐   │
│  │   Backend API       │  │  EncryptedSharedPreferences │   │
│  │  (10.0.2.2:8080)     │  │  (Almacenamiento local)     │   │
│  └─────────────────────┘  └─────────────────────────────┘   │
└────────────────────────────────────────────────────────────────┘
```

---

## 8. Resumen

| Aspecto | Implementación |
|---------|----------------|
| **Patrón UI** | MVVM con ViewBinding |
| **Inyección de Dependencias** | Manual (ViewModelFactory) |
| **Patrón Repository** | ✅ Implementado |
| **Manejo de Estado** | LiveData + StateFlow (en ViewModel) |
| **Navegación** | Navigation Component con acciones |
| **Seguridad Token** | EncryptedSharedPreferences + JWT Bearer |
| **Refresh Token** | TokenAuthenticator automático |
| **Validación** | En ViewModel (campo vacío) |
| **Manejo de Errores** | Try-Catch con Result<*> |
