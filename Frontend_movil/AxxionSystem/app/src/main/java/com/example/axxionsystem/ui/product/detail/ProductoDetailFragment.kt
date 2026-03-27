package com.example.axxionsystem.ui.product.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.axxionsystem.R
import com.example.axxionsystem.data.api.RetrofitClient
import com.example.axxionsystem.data.local.AppDatabase
import com.example.axxionsystem.data.repository.product.ProductoRepository
import com.example.axxionsystem.databinding.FragmentProductoDetailBinding
import com.example.axxionsystem.ui.product.ProductoDetailUiState
import com.example.axxionsystem.ui.product.ProductoViewModelFactory
import com.example.axxionsystem.ui.product.UpdateActionState
import kotlinx.coroutines.launch
import android.widget.ArrayAdapter

class ProductoDetailFragment : Fragment(R.layout.fragment_producto_detail) {

    private var _binding: FragmentProductoDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProductoDetailViewModel
    private val args: ProductoDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProductoDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupSpinner()
        setupListeners()
        observeUiState()
        viewModel.getProductoById(args.productoId)

        binding.btnVolverDetail.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupViewModel() {
        val apiService = RetrofitClient.getApiService(requireContext())
        val database = AppDatabase.getDatabase(requireContext())
        val repository = ProductoRepository(apiService, database.productoDao())
        val factory = ProductoViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ProductoDetailViewModel::class.java]
    }

    private fun setupListeners() {
        binding.btnActualizar.setOnClickListener {
            val nuevoEstado = binding.spinnerEstado.selectedItem?.toString() ?: "DISPONIBLE"
            val notas = binding.etNotas.text.toString().trim()
            viewModel.updateEstado(args.productoId, nuevoEstado, notas)
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        handleDetailState(state)
                    }
                }
                launch {
                    viewModel.updateState.collect { state ->
                        handleUpdateActionState(state)
                    }
                }
            }
        }
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.estados_producto,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerEstado.adapter = adapter
    }

    private fun handleDetailState(state: ProductoDetailUiState) {
        when (state) {
            is ProductoDetailUiState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            is ProductoDetailUiState.Success -> {
                binding.progressBar.visibility = View.GONE
                val p = state.producto
                binding.tvNombre.text = p.nombre
                binding.tvMarcaModelo.text = p.marca
                binding.tvModelo.text = p.modelo
                binding.tvNumeroSerie.text = p.numeroSerie
                binding.tvValor.text = p.valorActual.toString()
                binding.etNotas.setText(p.notas)
            }
            is ProductoDetailUiState.Error -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleUpdateActionState(state: UpdateActionState) {
        when (state) {
            is UpdateActionState.Loading -> {
                binding.btnActualizar.isEnabled = false
                binding.btnActualizar.text = "Guardando..."
            }
            is UpdateActionState.Success -> {
                binding.btnActualizar.isEnabled = true
                binding.btnActualizar.text = "GUARDAR CAMBIOS"
                Toast.makeText(requireContext(), "¡Actualizado con éxito!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
            is UpdateActionState.Error -> {
                binding.btnActualizar.isEnabled = true
                binding.btnActualizar.text = "GUARDAR CAMBIOS"
                Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}