// router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import Home from '@/views/Home.vue'
import User from '@/views/User.vue'
import Login from '@/views/login.vue'
import Inventory from '@/views/Inventory.vue'
import Maintenace from '@/views/Maintenace.vue'
import Category from '@/views/Category.vue'
import SubCategory from '@/views/SubCategory.vue'
import Reports from '@/views/Reports.vue'
import ReportUsers from '@/views/ReportUsers.vue'
import ReportMaintenances from '@/views/ReportMaintenances.vue'
import ReportAlquiler from '@/views/ReportAlquiler.vue'
import Rental from '@/views/Rental.vue'
import Alerts from '@/views/Alerts.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login.vue'),
    meta: { 
      requiresAuth: false,
      title: 'Iniciar Sesión'
    },
  },
  {
    path: '/Home',
    name: 'Home',
    component: Home,
    meta: { 
      requiresAuth: true,
      title: 'Inicio'
    },
  },
  {
    path: '/User',
    name: 'usuarios',
    component: User,
    meta: { 
      requiresAuth: true,
      title: 'Gestión de Usuarios',
      
    },
  },
  {
    path: '/Inventory',
    name: 'Inventory',
    component: Inventory,
    meta: { 
      requiresAuth: true,
      title: 'Inventario'
    },
  },
  {
    path: '/Mantenace',
    name: 'Mantenimiento',
    component: Maintenace,
    meta: { 
      requiresAuth: true,
      title: 'Mantenimiento',
      
    },
  },
  {
    path: '/Category',
    name: 'Category',
    component: Category,
    meta: { 
      requiresAuth: true,
      title: 'Categorías'
    },
  },
  {
    path: '/SubCategory',
    name: 'SubCategory',
    component: SubCategory,
    meta: { 
      requiresAuth: true,
      title: 'Subcategorías'
    },
  },
  {
    path: '/Reports',
    name: 'Reports',
    component: Reports,
    meta:{ 
      requiresAuth: true,
      title: 'Reportes'
    },
  },
  {
    path: '/ReportUsers',
    name: 'ReportUsers',
    component: ReportUsers,
    meta:{ 
      requiresAuth: true,
      title: 'Gestión de Usuarios',
      
    },
  },
  {
    path: '/ReportMaintenance',
    name: 'ReportMaintenance',
    component: ReportMaintenances,
    meta: { 
      requiresAuth: true,
      title: 'Reporte de Mantenimientos',
      
    },
  },
  {
    path: '/ReportAlquiler',
    name: 'ReportAlquiler',
    component: ReportAlquiler,
    meta: { 
      requiresAuth: true,
      title: 'Reporte de Alquileres',
      
    },
  },
  {
    path: '/Rental',
    name: 'Rental',
    component: Rental,
    meta: { 
      requiresAuth: true,
      title: 'Reporte de Alquileres',
      
    },
  },
  {
    path: '/Alerts',
    name: 'Alerts',
    component: Alerts,
    meta: { 
      requiresAuth: true,
      title: 'Reporte de Alertas',
    },
  },
  {
    path: '/quotation/:id',
    name: 'QuotationDetails',
    component: () => import('@/views/QuotationDetails.vue'),
    meta: {
      requiresAuth: true,
      title: 'Detalle de Cotización'
    }
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

// Guard global de navegación
router.beforeEach(async (to, from, next) => { // <-- async
  const authStore = useAuthStore();

  if (!authStore.authReady) {
    await authStore.tryToRefresh();
  }

  const isAuthenticated = authStore.isAuthenticated;
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth);
  const isLoginPage = to.name === 'Login';

  if (isAuthenticated && isLoginPage) {
    return next({ name: 'Home' });
  }

  if (requiresAuth && !isAuthenticated) {
    return next({ name: 'Login', query: { redirect: to.fullPath } });
  }

  // Si el usuario ya está autenticado e intenta ir al Login
  if (isAuthenticated && to.name === 'Login') {
      return next({name: 'Home'});
  }
  console.log(`[Router Guard] ¿Usuario autenticado?`, isAuthenticated);

    if (to.name === 'Login') { 
    return next();
    }

  document.title = `${to.meta.title || 'AXXION SYSTEM'} | Teleperformance`

  // Si la ruta requiere autenticación
  if (to.meta.requiresAuth) {
    // Verificar si hay sesión activa
    const hasValidSession = await authStore.isAuthenticated

    if (!hasValidSession) {
      // Redirigir al login con la ruta de retorno
      console.log(`[Router Guard] ACCESO DENEGADO. Redirigiendo a Login.`);
      next({
        name: 'Login',
        query: { redirect: to.fullPath },
      })
      return
    }

    // Si hay roles específicos requeridos (opcional)
    if (to.meta.roles && !to.meta.roles.includes(authStore.userRole)) {
      // Usuario no tiene permiso
      next({ name: 'Home' })
      return
    }
  }

  // Si está autenticado e intenta ir al login, redirigir al dashboard
  if (to.name === 'Login' && authStore.isAuthenticated) {
    next({ name: 'Home' })
    return
  }
console.log(`[Router Guard] ACCESO PERMITIDO.`);
  next()
})

export default router




// const routes = [
//   // Ruta raíz - redirige al login si no está autenticado, al home si está autenticado
//   {
//     path: '/',
//     redirect: () => {
//       const authStore = useAuthStore();
//       return authStore.isAuthenticated ? '/Home' : '/login';
//     }
//   },
//   // Rutas públicas (no requieren autenticación)
//   {
//     path: '/login',
//     name: 'login',
//     component: Login,
//     meta: { 
//       requiresAuth: false,
//       hideForAuthenticated: true // Ocultar si ya está logueado
//     },
//   },
//   // Rutas protegidas (requieren autenticación)
//   {
//     path: '/Home',
//     name: 'Home',
//     component: Home,
//     meta: { 
//       requiresAuth: true,
//       title: 'Inicio'
//     },
//   },
//   {
//     path: '/User',
//     name: 'usuarios',
//     component: User,
//     meta: { 
//       requiresAuth: true,
//       title: 'Gestión de Usuarios',
//       roles: ['ADMIN'] // Solo administradores pueden gestionar usuarios
//     },
//   },
//   {
//     path: '/Inventory',
//     name: 'Inventory',
//     component: Inventory,
//     meta: { 
//       requiresAuth: true,
//       title: 'Inventario'
//     },
//   },
//   {
//     path: '/Mantenace',
//     name: 'Mantenimiento',
//     component: Maintenace,
//     meta: { 
//       requiresAuth: true,
//       title: 'Mantenimiento',
//       roles: ['ADMIN', 'TECNICO'] // Solo admin y técnicos
//     },
//   },
//   {
//     path: '/Category',
//     name: 'Category',
//     component: Category,
//     meta: { 
//       requiresAuth: true,
//       title: 'Categorías'
//     },
//   },
//   {
//     path: '/SubCategory',
//     name: 'SubCategory',
//     component: SubCategory,
//     meta: { 
//       requiresAuth: true,
//       title: 'Subcategorías'
//     },
//   },
//   {
//     path: '/Reports',
//     name: 'Reports',
//     component: Reports,
//     meta:{ 
//       requiresAuth: true,
//       title: 'Reportes'
//     },
//   },
//   {
//     path: '/ReportUsers',
//     name: 'ReportUsers',
//     component: ReportUsers,
//     meta:{ 
//       requiresAuth: true,
//       title: 'Gestión de Usuarios',
//       roles: ['ADMIN']
//     },
//   },
//   {
//     path: '/ReportMaintenance',
//     name: 'ReportMaintenance',
//     component: ReportMaintenances,
//     meta: { 
//       requiresAuth: true,
//       title: 'Reporte de Mantenimientos',
//       roles: ['ADMIN', 'TECNICO'] // Solo admin y técnicos
//     },
//   },
//   {
//     path: '/ReportAlquiler',
//     name: 'ReportAlquiler',
//     component: ReportAlquiler,
//     meta: { 
//       requiresAuth: true,
//       title: 'Reporte de Alquileres',
      
//     },
//   },
//   {
//     path: '/Rental',
//     name: 'Rental',
//     component: Rental,
//     meta: { 
//       requiresAuth: true,
//       title: 'Reporte de Alquileres',
      
//     },
//   },
//   {
//     path: '/Alerts',
//     name: 'Alerts',
//     component: Alerts,
//     meta: { 
//       requiresAuth: true,
//       title: 'Reporte de Alertas',
//     },
//   },
// ]

// const router = createRouter({
//   history: createWebHistory(),
//   routes,
// })

// // Guard de navegación global
// router.beforeEach(async (to, from, next) => {
//   const authStore = useAuthStore();
  
//   // Verificar autenticación desde localStorage al iniciar
//   authStore.checkAuth();
  
//   const requiresAuth = to.matched.some((record) => record.meta.requiresAuth);
//   const hideForAuthenticated = to.matched.some((record) => record.meta.hideForAuthenticated);
//   const requiredRoles = to.meta.roles;
  
//   console.log('Router Guard:', {
//     to: to.path,
//     requiresAuth,
//     isAuthenticated: authStore.isAuthenticated,
//     user: authStore.user,
//     requiredRoles
//   });

//   // Si la ruta debe ocultarse para usuarios autenticados (como login)
//   if (hideForAuthenticated && authStore.isAuthenticated) {
//     console.log('Usuario ya autenticado, redirigiendo al home');
//     return next('/Home');
//   }

//   // Si la ruta requiere autenticación
//   if (requiresAuth) {
//     if (!authStore.isAuthenticated) {
//       console.log('Usuario no autenticado, redirigiendo al login');
//       return next('/login');
//     }

//     // Verificar roles si están especificados
//     if (requiredRoles && requiredRoles.length > 0) {
//       const userRoles = authStore.user?.roles || [];
      
//       console.log('Verificando roles:', {
//         userRoles: userRoles,
//         requiredRoles: requiredRoles,
//         userRolesType: typeof userRoles[0]
//       });
      
//       // Usar el método hasAnyRole del store que ya maneja ambos formatos
//       const hasRequiredRole = authStore.hasAnyRole(requiredRoles);

//       if (!hasRequiredRole) {
//         console.log('Usuario sin permisos suficientes:', {
//           userRoles: userRoles,
//           requiredRoles: requiredRoles,
//           hasRequiredRole: hasRequiredRole
//         });
        
//         // Redirigir al home con mensaje de error
//         return next({
//           path: '/Home',
//           query: { 
//             error: 'No tienes permisos para acceder a esta sección' 
//           }
//         });
//       }
      
//       console.log('Permisos verificados correctamente, acceso permitido');
//     }
//   }

//   // Establecer título de la página
//   if (to.meta.title) {
//     document.title = `AXXION - ${to.meta.title}`;
//   }

//   next();
// });

// export default router

