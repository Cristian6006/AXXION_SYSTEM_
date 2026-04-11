package com.example.axxionsystem.ui.home

/**
 * Pantalla principal (home) luego del login.
 *
 * Carga el perfil del usuario para mostrar saludo y rol, y permite cerrar
 * sesion (logout en backend + limpieza local + navegacion a Login).
 */
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionManager
import com.example.axxionsystem.R
import com.example.axxionsystem.data.api.RetrofitClient
import com.example.axxionsystem.data.model.resumen.ResumenResponse
import com.example.axxionsystem.data.repository.auth.AuthRepository
import com.example.axxionsystem.data.repository.home.DashboardRepository
import com.example.axxionsystem.databinding.FragmentHomeBinding
import com.example.axxionsystem.ui.home.summary.KpiUiState
import com.example.axxionsystem.util.SessionManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.launch

class HomeFragment: Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager

    private lateinit var homeViewModel: HomeViewModel
    private var lastErrorMessage: String? = null
    private var lastKpiErrorMessage: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        val apiService = RetrofitClient.getApiService(requireContext())
        val repositoryAuth = AuthRepository(apiService)
        val repositoryDashboard = DashboardRepository(apiService)
        
        val factory = HomeViewModelFactory(repositoryAuth, repositoryDashboard)
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        setupProfileObserver()
        setupKpiObserver()
        
        homeViewModel.fetchUserProfile()
        setupModuleCards()
        setupMorphingMenu()
        setupLogout()
    }

    private fun setupProfileObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.uiState.collect { state ->
                    when (state) {
                        is HomeUiState.Loading -> {
                            binding.tvSubtitle.text = getString(R.string.loading_data)
                        }

                        is HomeUiState.Success -> {
                            val perfil = state.perfil
                            val rol = perfil.roles.firstOrNull()?.replace("ROLE_", "") ?: "USUARIO"
                            binding.tvSubtitle.text = "Hola, ${perfil.nombre} ($rol)"
                            lastErrorMessage = null
                        }

                        is HomeUiState.Error -> {
                            binding.tvSubtitle.text = state.message
                            if (lastErrorMessage != state.message) {
                                Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                                lastErrorMessage = state.message
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupKpiObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.uiStateKpi.collect { state ->
                    when (state) {
                        is KpiUiState.Loading -> showSkeletonLoading()
                        is KpiUiState.Success -> {
                            showKpiData(state.data)
                            lastKpiErrorMessage = null
                        }
                        is KpiUiState.Error -> {
                            handleKpiError(state.message)
                        }
                    }
                }
            }
        }
    }

    private fun handleKpiError(message: String) {
        binding.shimmerKpi.stopShimmer()
        binding.shimmerKpi.hideShimmer()

        binding.tvTotalProductos.text = "-"
        binding.tvTotalAlquileres.text = "-"
        binding.tvTotalMantenimientos.text = "-"

        if (lastKpiErrorMessage != message) {
            Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
            lastKpiErrorMessage = message
        }
    }

    private fun showSkeletonLoading() {
        binding.shimmerKpi.startShimmer()
        binding.tvTotalProductos.text = ""
        binding.tvTotalAlquileres.text = ""
        binding.tvTotalMantenimientos.text = ""
    }

    private fun showKpiData(data: ResumenResponse) {
        binding.shimmerKpi.stopShimmer()
        binding.shimmerKpi.hideShimmer()

        binding.tvTotalProductos.text = data.totalProductos.toString()
        binding.tvTotalAlquileres.text = data.totalAlquileres.toString()
        binding.tvTotalMantenimientos.text = data.totalMantenimientos.toString()
    }

    private fun setupLogout() {
        binding.btnLogout.setOnClickListener {
            homeViewModel.logoutBackend()
            sessionManager.clearSession()
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }
    }

    /**
     * Configura la navegación para cada tarjeta de módulo en el Home.
     * Se utilizan las acciones definidas en nav_graph.xml.
     */
    private fun setupModuleCards() {
        // Módulo de Inventario / Productos
        binding.cardInventory.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_productoList)
        }

        // Módulo de Alquileres
        binding.cardAlquiler.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_alquilerFragment)
        }

        // Módulo de Mantenimiento
        binding.cardMaintenance.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_mantenimientoFragment)
        }
    }

    /**
     * Implementación de Morphing Animation para el menú flotante usando Material Design 3.
     */
    private fun setupMorphingMenu() {
        binding.fabMenu.setOnClickListener {
            val transform = MaterialContainerTransform().apply {
                startView = binding.fabMenu
                endView = binding.cardFloatingMenu
                addTarget(binding.cardFloatingMenu)
                scrimColor = Color.TRANSPARENT
                duration = 350L
            }

            TransitionManager.beginDelayedTransition(binding.root as ViewGroup, transform)

            binding.fabMenu.visibility = View.GONE
            binding.cardFloatingMenu.visibility = View.VISIBLE
        }

        binding.btnCloseMenu.setOnClickListener {
            val transform = MaterialContainerTransform().apply {
                startView = binding.cardFloatingMenu
                endView = binding.fabMenu
                addTarget(binding.fabMenu)
                scrimColor = Color.TRANSPARENT
                duration = 300L
            }

            TransitionManager.beginDelayedTransition(binding.root as ViewGroup, transform)

            binding.cardFloatingMenu.visibility = View.GONE
            binding.fabMenu.visibility = View.VISIBLE
        }

        // Opción 1: Navegar al perfil de usuario
        binding.btnOption1.setOnClickListener {
            binding.btnCloseMenu.performClick()
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        // Opción 2: Placeholder para futuras funcionalidades (ej: Ajustes o Notificaciones)
        binding.btnOption2.setOnClickListener {
            binding.btnCloseMenu.performClick()
            Snackbar.make(binding.root, "Funcionalidad en desarrollo", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
