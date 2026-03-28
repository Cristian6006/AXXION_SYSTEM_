package com.example.axxionsystem.ui.product.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.axxionsystem.R
import com.example.axxionsystem.data.api.RetrofitClient
import com.example.axxionsystem.data.local.AppDatabase
import com.example.axxionsystem.data.repository.product.ProductoRepository
import com.example.axxionsystem.databinding.FragmentProductoListBinding
import com.example.axxionsystem.ui.product.ProductoViewModelFactory
import com.example.axxionsystem.ui.product.ProductosUiState
import kotlinx.coroutines.launch

class ProductoListFragment : Fragment(R.layout.fragment_producto_list) {

    private var _binding: FragmentProductoListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProductosViewModel
    private lateinit var adapter: ProductosAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProductoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupRecyclerView()
        setupListeners()
        observeUiState()

        binding.btnVolverProducto.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupViewModel() {
        val apiService = RetrofitClient.getApiService(requireContext())
        val database = AppDatabase.getDatabase(requireContext())
        val repository = ProductoRepository(apiService, database.productoDao())
        val factory = ProductoViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ProductosViewModel::class.java]
    }

    private fun setupRecyclerView() {
        adapter = ProductosAdapter { producto ->
            val action = ProductoListFragmentDirections.actionListToDetail(producto.id)
            findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter
    }

    private fun setupListeners() {
        binding.etSearch.doOnTextChanged { text, _, _, _ ->
            viewModel.loadProductos(text.toString().trim())
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

    private fun handleState(state: ProductosUiState) {
        when (state) {
            is ProductosUiState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            is ProductosUiState.Success -> {
                binding.progressBar.visibility = View.GONE
                adapter.submitList(state.productos)
            }
            is ProductosUiState.Error -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}