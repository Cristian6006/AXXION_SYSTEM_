package com.example.axxionsystem.ui.mantenimiento

import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.axxionsystem.data.api.RetrofitClient
import com.example.axxionsystem.data.local.AppDatabase
import com.example.axxionsystem.data.repository.product.ProductoRepository
import com.example.axxionsystem.databinding.DialogNuevaSolicitudMantenimientoBinding
import com.example.axxionsystem.ui.product.ProductosUiState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * DialogFragment para crear una nueva solicitud de mantenimiento.
 * Permite al usuario seleccionar un producto de la lista y completar los detalles de la solicitud.
 * 
 * Responsabilidades:
 * - Cargar la lista de productos disponibles desde el repositorio
 * - Validar campos obligatorios
 * - Generar solicitud de mantenimiento con datos de entrada
 * - Informar al usuario de operaciones exitosas/fallidas
 */
class NuevaSolicitudMantenimientoDialogFragment(
    private val onSolicitudCreated: (descripcion: String, fecha: String, estado: String, responsable: String?, productoId: Int?) -> Unit
) : DialogFragment() {

    private var _binding: DialogNuevaSolicitudMantenimientoBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var productoRepository: ProductoRepository
    private val productoMap = mutableMapOf<String, Int>() // Mapeo de nombre a ID

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogNuevaSolicitudMantenimientoBinding.inflate(layoutInflater)
        
        // Inicializar repositorio
        val apiService = RetrofitClient.getApiService(requireContext())
        val database = AppDatabase.getDatabase(requireContext())
        productoRepository = ProductoRepository(apiService, database.productoDao())

        // Cargar productos
        cargarProductos()

        return AlertDialog.Builder(requireContext())
            .setTitle("Nueva Solicitud de Mantenimiento")
            .setView(binding.root)
            .setPositiveButton("Crear") { _, _ ->
                // La lógica de creación se manejará en onResume para evitar el cierre automático
                // del diálogo si la validación falla.
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }
            .create()
    }

    /**
     * Carga la lista de productos desde la base de datos y los muestra en el AutoCompleteTextView
     */
    private fun cargarProductos() {
        lifecycleScope.launch {
            productoRepository.getAllProductosFlow().collect { productos ->
                productoMap.clear()
                val productosNombres = productos.map { producto ->
                    productoMap[producto.nombre] = producto.id
                    "${producto.nombre} (${producto.marca})"
                }
                
                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    productosNombres
                )
                binding.inputProducto.setAdapter(adapter)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val dialog = dialog as AlertDialog
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            // Validar campos obligatorios
            val productoSeleccionado = binding.inputProducto.text.toString().trim()
            val descripcion = binding.etDescripcion.text.toString().trim()
            val responsable = binding.etResponsable.text.toString().trim().ifEmpty { null }

            var esValido = true
            
            // Validación del producto
            if (productoSeleccionado.isBlank()) {
                binding.tilProducto.error = "Debe seleccionar un producto"
                esValido = false
            } else {
                binding.tilProducto.error = null
            }

            // Validación de la descripción
            if (descripcion.isBlank()) {
                binding.tilDescripcion.error = "La descripción es obligatoria"
                esValido = false
            } else {
                binding.tilDescripcion.error = null
            }

            if (!esValido) {
                return@setOnClickListener
            }

            // Obtener el ID del producto seleccionado
            val productoId = productoMap[productoSeleccionado.split(" (")[0]]

            // Obtener la fecha actual en un formato adecuado
            val fechaActual = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            val estadoInicial = "Pendiente" // Estado por defecto para una nueva solicitud

            onSolicitudCreated(descripcion, fechaActual, estadoInicial, responsable, productoId)
            dismiss() // Cerrar el diálogo después de la creación
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
