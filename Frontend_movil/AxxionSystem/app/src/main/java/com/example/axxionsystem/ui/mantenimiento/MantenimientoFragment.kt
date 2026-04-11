package com.example.axxionsystem.ui.mantenimiento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.axxionsystem.data.api.RetrofitClient
import com.example.axxionsystem.data.repository.mantenimiento.MantenimientoRepository
import com.example.axxionsystem.databinding.FragmentMantenimientoBinding
import com.example.axxionsystem.ui.mantenimiento.adapter.MantenimientoAdapter

/**
 * Fragmento para la gestión de solicitudes de mantenimiento.
 * Implementa el patrón MVVM y Clean Architecture.
 */
class MantenimientoFragment : Fragment() {

    private var _binding: FragmentMantenimientoBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MantenimientoViewModel
    private lateinit var mantenimientoAdapter: MantenimientoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMantenimientoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupRecyclerView()
        setupObservers()
        setupListeners()

        // Carga inicial de datos
        viewModel.cargarMantenimientos()
    }

    private fun setupViewModel() {
        val apiService = RetrofitClient.getApiService(requireContext())
        val repository = MantenimientoRepository(apiService)
        val factory = MantenimientoViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[MantenimientoViewModel::class.java]
    }

    private fun setupRecyclerView() {
        mantenimientoAdapter = MantenimientoAdapter(
            onEditClick = { mantenimiento ->
                // Aquí se podría abrir un diálogo similar al de creación pero con datos cargados
                Toast.makeText(context, "Editar: ${mantenimiento.descripcion}", Toast.LENGTH_SHORT).show()
            },
            onDeleteClick = { mantenimiento ->
                // Implementación de eliminación (requiere endpoint en ApiService si es real)
                Toast.makeText(context, "Eliminar solicitud #${mantenimiento.id}", Toast.LENGTH_SHORT).show()
            }
        )
        binding.rvMantenimientos.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mantenimientoAdapter
        }
    }

    private fun setupObservers() {
        viewModel.mantenimientos.observe(viewLifecycleOwner) { listaResponse ->
            // Filtramos nulos por seguridad y enviamos la lista al adaptador
            val listaMantenimientos = listaResponse.mapNotNull { it.mantenimiento }
            mantenimientoAdapter.submitList(listaMantenimientos)
            binding.layoutEmpty.visibility = if (listaMantenimientos.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.swipeRefresh.isRefreshing = isLoading
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }

        viewModel.operacionResult.observe(viewLifecycleOwner) { result ->
            result
                .onSuccess { data ->
                    Toast.makeText(context, "Operación exitosa: ${data.message}", Toast.LENGTH_SHORT).show()
                }
                .onFailure { exception ->
                    Toast.makeText(context, exception.message ?: "Error en la operación", Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun setupListeners() {
        binding.btnNuevaSolicitud.setOnClickListener {
            val dialog = NuevaSolicitudMantenimientoDialogFragment { desc, fecha, estado, resp, productoId ->
                viewModel.crearMantenimiento(desc, fecha, estado, resp)
            }
            dialog.show(parentFragmentManager, "NuevaSolicitudDialog")
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.cargarMantenimientos()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
