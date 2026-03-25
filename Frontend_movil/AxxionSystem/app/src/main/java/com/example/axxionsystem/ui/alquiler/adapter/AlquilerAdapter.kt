package com.example.axxionsystem.ui.alquiler.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.axxionsystem.R
import com.example.axxionsystem.data.model.AlquilerItem

/**
 * Adapter personalizado para mostrar solicitudes y rentas de alquiler
 * con un diseño UI/UX mejorado usando tarjetas Material Design.
 */
class AlquilerAdapter(
    private val items: List<AlquilerItem>,
    private val onItemClick: ((Int) -> Unit)? = null
) : RecyclerView.Adapter<AlquilerAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvTipo: TextView = view.findViewById(R.id.tvTipo)
        val tvEstado: TextView = view.findViewById(R.id.tvEstado)
        val tvId: TextView = view.findViewById(R.id.tvId)
        val layoutSolicitud: LinearLayout = view.findViewById(R.id.layoutSolicitud)
        val tvClienteId: TextView = view.findViewById(R.id.tvClienteId)
        val tvCantidad: TextView = view.findViewById(R.id.tvCantidad)
        val layoutRenta: LinearLayout = view.findViewById(R.id.layoutRenta)
        val tvFechaInicio: TextView = view.findViewById(R.id.tvFechaInicio)
        val tvFechaFin: TextView = view.findViewById(R.id.tvFechaFin)
        val layoutDescripcion: LinearLayout = view.findViewById(R.id.layoutDescripcion)
        val tvDescripcion: TextView = view.findViewById(R.id.tvDescripcion)
        val layoutItems: LinearLayout = view.findViewById(R.id.layoutItems)
        val tvItemsCount: TextView = view.findViewById(R.id.tvItemsCount)
        val layoutAcciones: LinearLayout = view.findViewById(R.id.layoutAcciones)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alquiler, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]

        // Configurar tipo (Solicitud o Renta)
        holder.tvTipo.text = if (item.tipo == AlquilerItem.TipoItem.SOLICITUD) "SOLICITUD" else "RENTA"

        // Configurar ID
        val tipoPrefix = if (item.tipo == AlquilerItem.TipoItem.SOLICITUD) "Solicitud" else "Renta"
        holder.tvId.text = "$tipoPrefix #${item.id}"

        // Configurar estado con color dinámico
        holder.tvEstado.text = item.estado
        holder.tvEstado.background = createEstadoBackground(item.estado)

        // Mostrar según tipo
        when (item.tipo) {
            AlquilerItem.TipoItem.SOLICITUD -> {
                holder.layoutSolicitud.visibility = View.VISIBLE
                holder.layoutRenta.visibility = View.GONE
                holder.layoutItems.visibility = View.GONE

                holder.tvClienteId.text = "#${item.clienteId}"
                holder.tvCantidad.text = "${item.cantidad ?: 1} unidad${if ((item.cantidad ?: 1) > 1) "es" else ""}"

                // Mostrar descripción si existe
                if (!item.descripcion.isNullOrBlank()) {
                    holder.layoutDescripcion.visibility = View.VISIBLE
                    holder.tvDescripcion.text = item.descripcion
                } else {
                    holder.layoutDescripcion.visibility = View.GONE
                }
            }
            AlquilerItem.TipoItem.RENTA -> {
                holder.layoutSolicitud.visibility = View.GONE
                holder.layoutRenta.visibility = View.VISIBLE
                holder.layoutItems.visibility = View.VISIBLE
                holder.layoutDescripcion.visibility = View.GONE

                holder.tvFechaInicio.text = item.fechaInicio ?: "–"
                holder.tvFechaFin.text = item.fechaFinPrevista ?: "–"
                holder.tvItemsCount.text = "${item.itemsCount} items"
            }
        }

        // Mostrar acciones solo para rentas
        holder.layoutAcciones.visibility = if (item.mostrarAcciones) View.VISIBLE else View.GONE

        // Click listener
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(position)
        }
    }

    override fun getItemCount() = items.size

    /**
     * Crea un background con color basado en el estado.
     */
    private fun createEstadoBackground(estado: String): GradientDrawable {
        val color = when (estado.uppercase()) {
            "PENDIENTE", "EN_PROCESO", "EN_REVISION" -> Color.parseColor("#FF9800") // Naranja
            "APROBADA", "APROBADO", "ACTIVA", "CONFIRMADA" -> Color.parseColor("#4CAF50") // Verde
            "RECHAZADA", "RECHAZADO", "CANCELADA", "CANCELADO" -> Color.parseColor("#F44336") // Rojo
            "COMPLETADA", "FINALIZADA" -> Color.parseColor("#2196F3") // Azul
            "ENTREGADA" -> Color.parseColor("#9C27B0") // Púrpura
            else -> Color.parseColor("#607D8B") // Gris por defecto
        }

        return GradientDrawable().apply {
            setColor(color)
            cornerRadius = 12f
        }
    }
}
