package com.example.axxionsystem.ui.auth

/**
 * Pantalla de login.
 *
 * Renderiza el formulario, valida campos, llama a [AuthViewModel.login] y:
 * - guarda el access token en [SessionManager]
 * - navega a Home al autenticar
 * - muestra feedback (loading/snackbar) en caso de error
 */
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.axxionsystem.R
import com.example.axxionsystem.data.api.RetrofitClient
import com.example.axxionsystem.data.repository.AuthRepository
import com.example.axxionsystem.databinding.FragmentLoginBinding
import com.example.axxionsystem.util.SessionManager
import com.google.android.material.snackbar.Snackbar

class LoginFragment: Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var authViewModel: AuthViewModel
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
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
            if (cargando) {
                binding.btnLogin.text = ""
                binding.btnLogin.isEnabled = false
                binding.progressLogin.visibility = View.VISIBLE
            } else {
                binding.btnLogin.text = "Iniciar Sesión"
                binding.btnLogin.isEnabled = true
                binding.progressLogin.visibility = View.GONE
            }
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isNotBlank() && password.isNotBlank()) {
                authViewModel.login(email, password)
            } else {
                mostrarSnackbar("Por favor, llena todos los campos")
            }
        }

        authViewModel.loginResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { authResponse ->
                sessionManager.saveAuthToken(authResponse.accessToken)
                mostrarSnackbar("Bienvenido")
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }.onFailure { error ->
                mostrarSnackbar(error.message ?: "Error de conexion")
            }
        }

        binding.btnRecoverPassword.setOnClickListener {
            Toast.makeText(requireContext(), "Ir a pantalla recuperar contraseña", Toast.LENGTH_SHORT).show()
            // findNavController().navigate(R.id.action_loginFragment_to_recoverFragment)
        }
    }

    private fun mostrarSnackbar(mensaje: String) {
        Snackbar.make(binding.root, mensaje, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
