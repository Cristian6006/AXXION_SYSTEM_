package com.example.axxionsystem.ui.home

/**
 * Pantalla principal (home) luego del login.
 *
 * Carga el perfil del usuario para mostrar saludo y rol, y permite cerrar
 * sesion (logout en backend + limpieza local + navegacion a Login).
 */
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionManager
import com.example.axxionsystem.R
import com.example.axxionsystem.data.api.RetrofitClient
import com.example.axxionsystem.data.repository.auth.AuthRepository
import com.example.axxionsystem.databinding.FragmentHomeBinding
import com.example.axxionsystem.util.SessionManager
import com.google.android.material.transition.MaterialContainerTransform

class HomeFragment: Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager

    private lateinit var homeViewModel: HomeViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        val apiService = RetrofitClient.getApiService(requireContext())
        val repository = AuthRepository(apiService)
        val factory = HomeViewModelFactory(repository)
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        homeViewModel.fetchUserProfile()
        setupMorphingMenu()
        setupNavigation()
    }

    private fun setupNavigation() {
        binding.cardAlquiler.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_alquilerFragment)
        }
    }

    private fun setupMorphingMenu() {
        // 1. Cuando el usuario toca el FAB Redondo (Abrir Menú)
        binding.fabMenu.setOnClickListener {
            // Preparamos la animación Morph
            val transform = MaterialContainerTransform().apply {
                startView = binding.fabMenu
                endView = binding.cardFloatingMenu
                addTarget(binding.cardFloatingMenu) // Hacia dónde vamos
                scrimColor = android.graphics.Color.TRANSPARENT // Sin fondo oscuro detrás
                duration = 350L // 350 milisegundos se ve muy fluido
            }

            // Iniciamos la transición en el contenedor principal
            TransitionManager.beginDelayedTransition(binding.root as ViewGroup, transform)

            // Intercambiamos la visibilidad (Esto dispara la animación automáticamente)
            binding.fabMenu.visibility = View.GONE
            binding.cardFloatingMenu.visibility = View.VISIBLE
        }

        // 2. Cuando el usuario toca la "X" dentro de la barra (Cerrar Menú)
        binding.btnCloseMenu.setOnClickListener {
            // Preparamos la animación Inversa
            val transform = MaterialContainerTransform().apply {
                startView = binding.cardFloatingMenu
                endView = binding.fabMenu
                addTarget(binding.fabMenu) // Hacia dónde volvemos
                scrimColor = android.graphics.Color.TRANSPARENT
                duration = 300L
            }

            // Iniciamos la transición
            TransitionManager.beginDelayedTransition(binding.root as ViewGroup, transform)

            // Intercambiamos la visibilidad al revés
            binding.cardFloatingMenu.visibility = View.GONE
            binding.fabMenu.visibility = View.VISIBLE
        }

        binding.btnOption1.setOnClickListener {
            // Hacer algo y luego cerrar el menú
            binding.btnCloseMenu.performClick()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}