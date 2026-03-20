package com.example.axxionsystem.ui.alquiler

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.axxionsystem.R
import com.example.axxionsystem.data.model.AlquilerItem
import com.example.axxionsystem.data.model.RentaResponse
import com.google.android.material.textfield.TextInputEditText
import com.example.axxionsystem.ui.alquiler.adapter.AlquilerAdapter

/**
 * Fragment de Alquiler - VERSIÓN MVVM.
 *
 * Este fragment gestiona las operaciones relacionadas con el alquiler de maquinaria.
 * Implementa el patrón MVVM separando la lógica de negocio en [AlquilerViewModel]
 * y el acceso a datos en el repositorio.
 *
 * Funcionalidades:
 * - Creación y visualización de solicitudes de alquiler
 * - Visualización de las rentas activas de un cliente
 * - Firma digital de procesos de Entrega y Devolución
 *
 * El Fragment ahora es solo "vista": observa el estado del ViewModel y responde a cambios.
 */
class AlquilerFragment : Fragment() {

    // ═══════════════════════════════════════════════════════
    // VISTAS
    // ═══════════════════════════════════════════════════════
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnVolver: Button
    private lateinit var btnNueva: Button
    private lateinit var btnMisRentas: Button
    private lateinit var tvEmpty: TextView

    // ═══════════════════════════════════════════════════════
    // VIEWMODEL
    // ═══════════════════════════════════════════════════════
    private lateinit var viewModel: AlquilerViewModel

    // ═══════════════════════════════════════════════════════
    // ADAPTER
    // ═══════════════════════════════════════════════════════
    private var adapter: AlquilerAdapter? = null

    // ID del cliente (hardcoded para compatibilidad)
    private val clienteIdDefault = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_alquiler, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar vistas
        inicializarVistas(view)

        // Inicializar ViewModel
        inicializarViewModel()

        // Configurar RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Configurar listeners
        configurarBotones()

        // Observar estados del ViewModel
        observarEstados()

        // Cargar datos iniciales
        viewModel.cargarSolicitudes()
    }

    /**
     * Inicializa las referencias a las vistas del layout.
     */
    private fun inicializarVistas(view: View) {
        progressBar = view.findViewById(R.id.progressBarAlquiler)
        recyclerView = view.findViewById(R.id.recyclerAlquiler)
        btnVolver = view.findViewById(R.id.btnVolverAlquiler)
        btnNueva = view.findViewById(R.id.btnNuevaSolicitud)
        btnMisRentas = view.findViewById(R.id.btnMisRentas)
        tvEmpty = view.findViewById(R.id.tvEmptyAlquiler)
    }

    /**
     * Inicializa el ViewModel usando la Factory.
     */
    private fun inicializarViewModel() {
        val factory = AlquilerViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, factory)[AlquilerViewModel::class.java]
    }

    /**
     * Configura los listeners de los botones.
     */
    private fun configurarBotones() {
        // Botón volver - navega al fragment anterior
        btnVolver.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Botón nueva solicitud
        btnNueva.setOnClickListener {
            mostrarDialogoNuevaSolicitud()
        }

        // Botón mis rentas - alterna entre vistas
        btnMisRentas.setOnClickListener {
            viewModel.toggleVista(clienteIdDefault)
        }
    }

    /**
     * Observa los estados del ViewModel y actualiza la UI.
     */
    private fun observarEstados() {
        // Estado de carga
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Errores
        viewModel.error.observe(viewLifecycleOwner) { mensajeError ->
            mensajeError?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }

        // Modo de visualización
        viewModel.mostrandoRentas.observe(viewLifecycleOwner) { mostrandoRentas ->
            btnMisRentas.text = if (mostrandoRentas) {
                "📋 Ver Solicitudes"
            } else {
                "📦 Ver mis Rentas"
            }
        }

        // Lista de Solicitudes
        viewModel.solicitudes.observe(viewLifecycleOwner) { solicitudes ->
            mostrarLista(solicitudes, emptyList())
        }

        // Lista de Rentas
        viewModel.rentas.observe(viewLifecycleOwner) { rentas ->
            mostrarLista(rentas, rentas)
        }

        // Resultado de crear solicitud
        viewModel.solicitudCreateResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { id ->
                Toast.makeText(requireContext(), "✅ Solicitud creada: #$id", Toast.LENGTH_SHORT).show()
                viewModel.clearCreateResult()
            }.onFailure { error ->
                if (error.message?.isNotEmpty() == true) {
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
                viewModel.clearCreateResult()
            }
        }

        // Resultado de firma de entrega
        viewModel.entregaFirmaResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { mensaje ->
                Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
                viewModel.clearEntregaResult()
            }.onFailure { error ->
                if (error.message?.isNotEmpty() == true) {
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
                viewModel.clearEntregaResult()
            }
        }

        // Resultado de firma de devolución
        viewModel.devolucionFirmaResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { mensaje ->
                Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
                viewModel.clearDevolucionResult()
            }.onFailure { error ->
                if (error.message?.isNotEmpty() == true) {
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
                viewModel.clearDevolucionResult()
            }
        }
    }

    /**
     * Muestra la lista de items en el RecyclerView.
     */
    private fun mostrarLista(items: List<AlquilerItem>, listaRentas: List<AlquilerItem>) {
        if (items.isEmpty()) {
            tvEmpty.visibility = View.VISIBLE
            tvEmpty.text = if (viewModel.mostrandoRentas.value == true) {
                "📦 No hay rentals activos"
            } else {
                "📋 No hay solicitudes de alquiler"
            }
            recyclerView.visibility = View.GONE
        } else {
            tvEmpty.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE

            adapter = AlquilerAdapter(items) { position ->
                if (viewModel.mostrandoRentas.value == true) {
                    val renta = viewModel.getRentaPorPosicion(position)
                    renta?.let { mostrarOpcionesRenta(it) }
                }
            }
            recyclerView.adapter = adapter
        }
    }

    /**
     * Muestra el diálogo para crear una nueva solicitud.
     */
    private fun mostrarDialogoNuevaSolicitud() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_nueva_solicitud, null)

        val inputClienteId = dialogView.findViewById<TextInputEditText>(R.id.inputClienteId)
        val inputCantidad = dialogView.findViewById<TextInputEditText>(R.id.inputCantidad)
        val inputDescripcion = dialogView.findViewById<TextInputEditText>(R.id.inputDescripcion)
        val inputProductoAlt = dialogView.findViewById<TextInputEditText>(R.id.inputProductoAlt)

        inputCantidad.setText("1")
        inputClienteId.setText(clienteIdDefault.toString())

        AlertDialog.Builder(requireContext(), R.style.Theme_AxxionSystem_Dialog)
            .setTitle("Nueva Solicitud de Alquiler")
            .setView(dialogView)
            .setPositiveButton("Crear Solicitud") { _, _ ->
                val clienteId = inputClienteId.text.toString().toIntOrNull()
                if (clienteId == null || clienteId <= 0) {
                    Toast.makeText(requireContext(), "ID de cliente inválido", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                viewModel.crearSolicitudSimple(
                    clienteId = clienteId,
                    cantidad = inputCantidad.text.toString().toIntOrNull() ?: 1,
                    descripcion = inputDescripcion.text.toString().ifBlank { null },
                    productoAlt = inputProductoAlt.text.toString().ifBlank { null }
                )
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    /**
     * Muestra las opciones disponibles para una renta.
     */
    private fun mostrarOpcionesRenta(renta: RentaResponse) {
        val opciones = arrayOf("✍️ Firmar Entrega", "↩️ Firmar Devolución", "Cerrar")
        AlertDialog.Builder(requireContext(), R.style.Theme_AxxionSystem_Dialog)
            .setTitle("Opciones Renta #${renta.id}")
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> mostrarDialogoFirmaEntrega(renta)
                    1 -> mostrarDialogoFirmaDevolucion(renta)
                }
            }
            .show()
    }

    /**
     * Muestra el diálogo para firmar la entrega.
     */
    private fun mostrarDialogoFirmaEntrega(renta: RentaResponse) {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_firma_entrega, null)

        val tvRentaId = dialogView.findViewById<TextView>(R.id.tvRentaId)
        val inputDireccionId = dialogView.findViewById<TextInputEditText>(R.id.inputDireccionId)
        val inputFirma = dialogView.findViewById<TextInputEditText>(R.id.inputFirma)
        val inputNotas = dialogView.findViewById<TextInputEditText>(R.id.inputNotas)

        tvRentaId.text = "Renta #${renta.id}"
        inputDireccionId.setText("1")

        AlertDialog.Builder(requireContext(), R.style.Theme_AxxionSystem_Dialog)
            .setTitle("Firmar Entrega")
            .setView(dialogView)
            .setPositiveButton("Firmar") { _, _ ->
                val dirId = inputDireccionId.text.toString().toIntOrNull() ?: 1
                val firma = inputFirma.text.toString()
                val notas = inputNotas.text.toString().ifBlank { null }

                if (firma.isBlank()) {
                    Toast.makeText(requireContext(), "La firma es obligatoria", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                viewModel.firmarEntrega(renta.id, dirId, firma, notas)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    /**
     * Muestra el diálogo para firmar la devolución.
     */
    private fun mostrarDialogoFirmaDevolucion(renta: RentaResponse) {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_firma_devolucion, null)

        val tvRentaId = dialogView.findViewById<TextView>(R.id.tvRentaId)
        val inputFirma = dialogView.findViewById<TextInputEditText>(R.id.inputFirma)
        val inputRecibe = dialogView.findViewById<TextInputEditText>(R.id.inputRecibe)
        val inputNotas = dialogView.findViewById<TextInputEditText>(R.id.inputNotas)

        tvRentaId.text = "Renta #${renta.id}"

        AlertDialog.Builder(requireContext(), R.style.Theme_AxxionSystem_Dialog)
            .setTitle("Firmar Devolución")
            .setView(dialogView)
            .setPositiveButton("Firmar") { _, _ ->
                val firma = inputFirma.text.toString()
                val recibe = inputRecibe.text.toString().ifBlank { null }
                val notas = inputNotas.text.toString().ifBlank { null }

                if (firma.isBlank()) {
                    Toast.makeText(requireContext(), "La firma es obligatoria", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                viewModel.firmarDevolucion(renta.id, firma, recibe, notas)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
