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
import com.google.android.material.snackbar.Snackbar
import com.example.axxionsystem.R
import com.example.axxionsystem.data.api.RetrofitClient
import com.example.axxionsystem.data.repository.AuthRepository
import com.example.axxionsystem.databinding.FragmentHomeBinding
import com.example.axxionsystem.ui.auth.AuthViewModel
import com.example.axxionsystem.ui.auth.AuthViewModelFactory
import com.example.axxionsystem.util.SessionManager

class HomeFragment: Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var authViewModel: AuthViewModel
    private lateinit var sessionManager: SessionManager

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
        val factory = AuthViewModelFactory(repository)
        authViewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]


        authViewModel.isLoading.observe(viewLifecycleOwner) { cargando ->
            binding.progressBarHome.visibility = if (cargando) View.VISIBLE else View.GONE
            binding.tvWelcome.visibility = if (cargando) View.GONE else View.VISIBLE
            binding.chipRole.visibility = if (cargando) View.GONE else View.VISIBLE
        }



        authViewModel.perfilResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { perfil ->
                binding.tvWelcome.text = "Hola, ${perfil.nombre}"

                val rolPrincipal = perfil.roles.firstOrNull()?.replace("ROLE_", "") ?: "USUARIO"
                binding.chipRole.text = "Rol: $rolPrincipal"

                sessionManager.saveUserRole(rolPrincipal)
            }.onFailure { error ->
                Snackbar.make(binding.root, "Error al cargar perfil: ${error.message}", Snackbar.LENGTH_LONG).show()
            }
        }

        binding.btnLogout.setOnClickListener {
            authViewModel.logoutBackend()
            sessionManager.clearSession()
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }

        authViewModel.fetchUserProfile()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
