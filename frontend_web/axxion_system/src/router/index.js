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
    meta: {
      requiresAuth: true,
      title: 'Reportes'
    },
  },
  {
    path: '/ReportUsers',
    name: 'ReportUsers',
    component: ReportUsers,
    meta: {
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
router.beforeEach(async (to, from, next) => {
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
    return next({ name: 'Home' });
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
