package com.example.axxionsystem.ui.mantenimiento

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.axxionsystem.databinding.DialogNuevaSolicitudMantenimientoBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * DialogFragment para crear una nueva solicitud de mantenimiento.
 * Permite al usuario introducir la descripción y el responsable.
 */
class NuevaSolicitudMantenimientoDialogFragment(
    private val onSolicitudCreated: (descripcion: String, fecha: String, estado: String, responsable: String?) -> Unit
) : DialogFragment() {

    private var _binding: DialogNuevaSolicitudMantenimientoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogNuevaSolicitudMantenimientoBinding.inflate(layoutInflater)

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

    override fun onResume() {
        super.onResume()
        val dialog = dialog as AlertDialog
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val descripcion = binding.etDescripcion.text.toString().trim()
            val responsable = binding.etResponsable.text.toString().trim().ifEmpty { null }
            val inventarioId = binding.etInventarioId.text.toString().trim() // No se usa en el modelo actual
            val costoEstimado = binding.etCostoEstimado.text.toString().trim() // No se usa en el modelo actual

            if (descripcion.isBlank()) {
                binding.tilDescripcion.error = "La descripción es obligatoria"
                return@setOnClickListener
            } else {
                binding.tilDescripcion.error = null
            }

            // Obtener la fecha actual en un formato adecuado
            val fechaActual = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            val estadoInicial = "Pendiente" // Estado por defecto para una nueva solicitud

            onSolicitudCreated(descripcion, fechaActual, estadoInicial, responsable)
            dismiss() // Cerrar el diálogo después de la creación
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
