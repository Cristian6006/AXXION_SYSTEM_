package com.example.axxionsystem.ui.mantenimiento.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.axxionsystem.R
import com.example.axxionsystem.data.model.mantenimiento.Mantenimiento
import com.example.axxionsystem.databinding.ItemMantenimientoBinding

/**
 * Adaptador para mostrar una lista de objetos Mantenimiento en un RecyclerView.
 * Utiliza ListAdapter para manejar eficientemente las actualizaciones de la lista.
 */
class MantenimientoAdapter(
    private val onEditClick: (Mantenimiento) -> Unit,
    private val onDeleteClick: (Mantenimiento) -> Unit
) : ListAdapter<Mantenimiento, MantenimientoAdapter.MantenimientoViewHolder>(MantenimientoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MantenimientoViewHolder {
        val binding = ItemMantenimientoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MantenimientoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MantenimientoViewHolder, position: Int) {
        val mantenimiento = getItem(position)
        holder.bind(mantenimiento)
    }

    inner class MantenimientoViewHolder(private val binding: ItemMantenimientoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(mantenimiento: Mantenimiento) {
            binding.apply {
                tvMantenimientoId.text = "Mantenimiento #${mantenimiento.id}"
                tvDescripcion.text = mantenimiento.descripcion
                tvFecha.text = mantenimiento.fecha // Formatear si es necesario
                tvResponsable.text = mantenimiento.responsable ?: "N/A"
                tvEstado.text = mantenimiento.estado

                // Configurar el color de fondo del estado según el valor
                when (mantenimiento.estado.lowercase()) {
                    "en revisión" -> tvEstado.setBackgroundResource(R.drawable.bg_estado_revision)
                    "pendiente", "en proceso" -> tvEstado.setBackgroundResource(R.drawable.bg_estado_proceso)
                    "completado", "finalizado" -> tvEstado.setBackgroundResource(R.drawable.bg_estado_finalizado)
                    "cancelado" -> tvEstado.setBackgroundResource(R.drawable.bg_estado_cancelado)
                    else -> tvEstado.setBackgroundResource(R.drawable.bg_estado_revision)
                }

                btnActualizar.setOnClickListener { onEditClick(mantenimiento) }
                btnEliminar.setOnClickListener { onDeleteClick(mantenimiento) }
            }
        }
    }

    /**
     * Callback para calcular las diferencias entre dos listas de Mantenimiento.
     */
    class MantenimientoDiffCallback : DiffUtil.ItemCallback<Mantenimiento>() {
        override fun areItemsTheSame(oldItem: Mantenimiento, newItem: Mantenimiento): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Mantenimiento, newItem: Mantenimiento): Boolean {
            return oldItem == newItem
        }
    }
}
