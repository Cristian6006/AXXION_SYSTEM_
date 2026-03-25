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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.axxionsystem.R
import com.example.axxionsystem.data.api.RetrofitClient
import com.example.axxionsystem.data.repository.auth.AuthRepository
import com.example.axxionsystem.databinding.FragmentLoginBinding
import com.example.axxionsystem.util.SessionManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

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


        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            authViewModel.login(email, password)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.uiState.collect { state ->
                    handleUiState(state)
                }
            }
        }

        binding.btnRecoverPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_recoverPasswordBottomSheet)
        }
    }

    private fun handleUiState(state: LoginUiState) {
        when (state) {
            is LoginUiState.Idle -> {
                showLoading(false)
            }

            is LoginUiState.Loading -> {
                showLoading(true)
                clearErrors()
            }

            is LoginUiState.ValidationError -> {
                showLoading(false)
                binding.tilEmail.error = state.emailError
                binding.tilPassword.error = state.passwordError
                authViewModel.resetState()
            }

            is LoginUiState.Success -> {
                showLoading(false)
                sessionManager.saveAuthToken(state.token)
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                authViewModel.resetState()
            }

            is LoginUiState.Error -> {
                showLoading(false)
                mostrarSnackbarError(state.message, state.isNetworkError)
                authViewModel.resetState()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.btnLogin.text = if (isLoading) "" else "Iniciar Sesion"
        binding.btnLogin.isEnabled = !isLoading
        binding.progressLogin.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun clearErrors() {
        binding.tilEmail.error = null
        binding.tilPassword.error = null
    }

    private fun mostrarSnackbarError(mensaje: String, isNetworkError: Boolean) {
        val snackbar = Snackbar.make(binding.root, mensaje, Snackbar.LENGTH_LONG)

        if (isNetworkError) {
            snackbar.setAction("Reintentar") {
                binding.btnLogin.performClick()
            }
        }

        snackbar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
