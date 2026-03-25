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
        setupLogout()
    }

    private fun setupLogout() {
        binding.btnLogout.setOnClickListener {
            homeViewModel.logoutBackend()
            sessionManager.clearSession()
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        setupNavigation()
    }

    private fun setupNavigation() {
        binding.cardAlquiler.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_alquilerFragment)
        }
    }

    private fun setupMorphingMenu() {
        binding.fabMenu.setOnClickListener {
            val transform = MaterialContainerTransform().apply {
                startView = binding.fabMenu
                endView = binding.cardFloatingMenu
                addTarget(binding.cardFloatingMenu)
                scrimColor = android.graphics.Color.TRANSPARENT
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
                scrimColor = android.graphics.Color.TRANSPARENT
                duration = 300L
            }

            TransitionManager.beginDelayedTransition(binding.root as ViewGroup, transform)

            binding.cardFloatingMenu.visibility = View.GONE
            binding.fabMenu.visibility = View.VISIBLE
        }

        binding.btnOption1.setOnClickListener {
            binding.btnCloseMenu.performClick()
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        binding.cardInventory.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_productoList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}