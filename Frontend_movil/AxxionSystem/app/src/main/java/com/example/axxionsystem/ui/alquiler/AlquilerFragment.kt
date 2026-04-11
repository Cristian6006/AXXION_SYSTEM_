package com.example.axxionsystem.ui.alquiler

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.axxionsystem.data.model.Alquiler.AlquilerItem
import com.example.axxionsystem.data.model.Alquiler.RentaResponse
import com.google.android.material.textfield.TextInputEditText
import com.example.axxionsystem.ui.alquiler.adapter.AlquilerAdapter
import com.google.android.material.textfield.TextInputLayout
import java.util.Calendar

class AlquilerFragment : Fragment() {

    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnVolver: Button
    private lateinit var btnNueva: Button
    private lateinit var btnMisRentas: Button
    private lateinit var tvEmpty: TextView
    private lateinit var etFiltroNombre: TextInputEditText
    private lateinit var btnFiltroFecha: Button
    private lateinit var btnLimpiarFiltros: Button

    private lateinit var viewModel: AlquilerViewModel
    private var adapter: AlquilerAdapter? = null
    private val clienteIdDefault = 1
    private var fechaFiltro: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_alquiler, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inicializarVistas(view)
        inicializarViewModel()
        configurarFiltros()
        configurarBotones()
        observarEstados()
        viewModel.cargarSolicitudes()
    }

    private fun inicializarVistas(view: View) {
        progressBar = view.findViewById(R.id.progressBarAlquiler)
        recyclerView = view.findViewById(R.id.recyclerAlquiler)
        btnVolver = view.findViewById(R.id.btnVolverAlquiler)
        btnNueva = view.findViewById(R.id.btnNuevaSolicitud)
        btnMisRentas = view.findViewById(R.id.btnMisRentas)
        tvEmpty = view.findViewById(R.id.tvEmptyAlquiler)
        etFiltroNombre = view.findViewById(R.id.etFiltroNombre)
        btnFiltroFecha = view.findViewById(R.id.btnFiltroFecha)
        btnLimpiarFiltros = view.findViewById(R.id.btnLimpiarFiltros)
        
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun inicializarViewModel() {
        val factory = AlquilerViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, factory)[AlquilerViewModel::class.java]
    }

    private fun configurarFiltros() {
        etFiltroNombre.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.aplicarFiltros(s?.toString(), fechaFiltro)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnFiltroFecha.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(requireContext(), { _, year, month, day ->
                val mes = String.format("%02d", month + 1)
                val dia = String.format("%02d", day)
                fechaFiltro = "$year-$mes-$dia"
                btnFiltroFecha.text = "📅 $fechaFiltro"
                viewModel.aplicarFiltros(etFiltroNombre.text?.toString(), fechaFiltro)
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }

        btnLimpiarFiltros.setOnClickListener {
            etFiltroNombre.setText("")
            fechaFiltro = null
            btnFiltroFecha.text = "Por Fecha"
            viewModel.aplicarFiltros(null, null)
        }
    }

    private fun configurarBotones() {
        btnVolver.setOnClickListener { parentFragmentManager.popBackStack() }
        btnNueva.setOnClickListener { mostrarDialogoNuevaSolicitud() }
        btnMisRentas.setOnClickListener { viewModel.toggleVista(clienteIdDefault) }
    }

    private fun observarEstados() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        viewModel.error.observe(viewLifecycleOwner) { msg ->
            msg?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show(); viewModel.clearError() }
        }
        viewModel.mostrandoRentas.observe(viewLifecycleOwner) { rentas ->
            btnMisRentas.text = if (rentas) "📋 Ver Solicitudes" else "📦 Ver mis Rentas"
        }
        viewModel.items.observe(viewLifecycleOwner) { items ->
            if (items.isEmpty()) {
                tvEmpty.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                tvEmpty.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                if (adapter == null) {
                    adapter = AlquilerAdapter(items) { pos ->
                        if (viewModel.mostrandoRentas.value == true) {
                            viewModel.getRentaPorPosicion(pos)?.let { mostrarOpcionesRenta(it) }
                        }
                    }
                    recyclerView.adapter = adapter
                } else {
                    adapter?.updateData(items)
                }
            }
        }
        viewModel.solicitudCreateResult.observe(viewLifecycleOwner) { it.onSuccess {
            Toast.makeText(requireContext(), "✅ Creada: #$it", Toast.LENGTH_SHORT).show()
            viewModel.clearCreateResult()
        } }
    }

    private fun mostrarDialogoNuevaSolicitud() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_nueva_solicitud, null)
        val inCant = dialogView.findViewById<TextInputEditText>(R.id.inputCantidad)
        val inDesc = dialogView.findViewById<TextInputEditText>(R.id.inputDescripcion)
        val inProd = dialogView.findViewById<TextInputEditText>(R.id.inputProductoAlt)
        inCant.setText("1")

        AlertDialog.Builder(requireContext(), R.style.Theme_AxxionSystem_Dialog)
            .setTitle("Nueva Solicitud")
            .setView(dialogView)
            .setPositiveButton("Crear") { _, _ ->
                viewModel.crearSolicitudSimple(clienteIdDefault, inCant.text.toString().toIntOrNull() ?: 1,
                    inDesc.text.toString(), inProd.text.toString())
            }.setNegativeButton("Cancelar", null).show()
    }

    private fun mostrarOpcionesRenta(renta: RentaResponse) {
        val opciones = arrayOf("✍️ Firmar Entrega", "↩️ Firmar Devolución", "Cerrar")
        AlertDialog.Builder(requireContext(), R.style.Theme_AxxionSystem_Dialog)
            .setTitle("Opciones Renta #${renta.id}")
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> mostrarDialogoFirmaEntrega(renta)
                    1 -> mostrarDialogoFirmaDevolucion(renta)
                }
            }.show()
    }

    private fun mostrarDialogoFirmaEntrega(renta: RentaResponse) {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_firma_entrega, null)
        val inFirma = view.findViewById<TextInputEditText>(R.id.inputFirma)
        AlertDialog.Builder(requireContext(), R.style.Theme_AxxionSystem_Dialog)
            .setTitle("Firmar Entrega")
            .setView(view)
            .setPositiveButton("Firmar") { _, _ ->
                viewModel.firmarEntrega(renta.id, 1, inFirma.text.toString(), null)
            }.setNegativeButton("Cancelar", null).show()
    }

    private fun mostrarDialogoFirmaDevolucion(renta: RentaResponse) {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_firma_devolucion, null)
        val inFirma = view.findViewById<TextInputEditText>(R.id.inputFirma)
        AlertDialog.Builder(requireContext(), R.style.Theme_AxxionSystem_Dialog)
            .setTitle("Firmar Devolución")
            .setView(view)
            .setPositiveButton("Firmar") { _, _ ->
                viewModel.firmarDevolucion(renta.id, inFirma.text.toString(), null, null)
            }.setNegativeButton("Cancelar", null).show()
    }
}
