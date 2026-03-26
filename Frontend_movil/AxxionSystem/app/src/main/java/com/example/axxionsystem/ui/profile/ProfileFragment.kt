package com.example.axxionsystem.ui.profile

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
import com.google.android.material.snackbar.Snackbar
import com.example.axxionsystem.data.api.RetrofitClient
import com.example.axxionsystem.data.local.AppDatabase
import com.example.axxionsystem.data.repository.user.UserRepository
import com.example.axxionsystem.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiService = RetrofitClient.getApiService(requireContext())
        val userDao = AppDatabase.getDatabase(requireContext()).userDao()
        val repository = UserRepository(apiService, userDao)

        val factory = ProfileViewModelFactory(repository)
        profileViewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                profileViewModel.uiState.collect {state ->
                    when (state) {
                        is ProfileUiState.Loading -> {
                            binding.progressBarProfile.visibility = View.VISIBLE
                            binding.cardProfileData.visibility = View.GONE
                        }

                        is ProfileUiState.Success -> {
                            binding.progressBarProfile.visibility = View.GONE
                            binding.cardProfileData.visibility = View.VISIBLE

                            val user = state.user
                            binding.tvNombreProfile.text = user.nombre
                            binding.tvEmailProfile.text = user.email
                            binding.chipRolProfile.text = user.rol
                        }

                        is ProfileUiState.Error -> {
                            binding.progressBarProfile.visibility = View.GONE
                            Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

        profileViewModel.syncProfile()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}