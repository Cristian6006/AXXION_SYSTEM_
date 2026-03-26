package com.example.axxionsystem.ui.auth.recovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.axxionsystem.data.api.RetrofitClient
import com.example.axxionsystem.databinding.FragmentRecoverPasswordBottomSheetBinding
import com.example.axxionsystem.data.repository.auth.AuthRepository
import com.example.axxionsystem.util.SessionManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class RecoverPasswordBottomSheetFragment: BottomSheetDialogFragment() {

    private var _binding: FragmentRecoverPasswordBottomSheetBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RecoverPasswordViewModel

    private lateinit var sessionManager: SessionManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRecoverPasswordBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        setupViewModel()
        setupListeners()
        observeUiState()
    }

    private fun setupViewModel() {
        val apiService = RetrofitClient.getApiService(requireContext())
        val repository = AuthRepository(apiService)
        val factory = RecoverPasswordViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[RecoverPasswordViewModel::class.java]
    }

    private fun setupListeners() {
        binding.btnSendEmail.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            viewModel.requestRecoveryToken(email)
        }

        binding.btnResetPassword.setOnClickListener {
            val token = binding.etToken.text.toString().trim()
            val newPass = binding.etNewPassword.text.toString()
            viewModel.executePasswordReset(token, newPass)
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    handleState(state)
                }
            }
        }
    }

    private fun handleState(state: RecoverPasswordUiState) {
        when (state) {
            is RecoverPasswordUiState.Idle -> {
                binding.progressBar.visibility = View.GONE
                enableButtons(true)
            }
            is RecoverPasswordUiState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                enableButtons(false)
            }
            is RecoverPasswordUiState.TokenSentSuccess -> {
                binding.progressBar.visibility = View.GONE
                enableButtons(true)

                binding.layoutStepEmail.visibility = View.GONE
                binding.layoutStepReset.visibility = View.VISIBLE
                binding.tvSubtitle.text = "Ingresa el código que enviamos a tu correo y tu nueva contraseña."
            }
            is RecoverPasswordUiState.ResetSuccess -> {
                binding.progressBar.visibility = View.GONE
                showSuccessDialogAndClose()
            }
            is RecoverPasswordUiState.Error -> {
                binding.progressBar.visibility = View.GONE
                enableButtons(true)
                Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun enableButtons(isEnabled: Boolean) {
        binding.btnSendEmail.isEnabled = isEnabled
        binding.btnResetPassword.isEnabled = isEnabled
    }

    private fun showSuccessDialogAndClose() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("¡Contraseña Actualizada!")
            .setMessage("Tu contraseña se ha restablecido exitosamente. Ahora puedes iniciar sesión.")
            .setPositiveButton("Entendido") { dialog, _ ->
                dialog.dismiss()
                this.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}