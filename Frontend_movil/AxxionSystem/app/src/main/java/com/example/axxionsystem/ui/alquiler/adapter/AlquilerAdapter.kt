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
import com.example.axxionsystem.data.model.Alquiler.AlquilerItem

/**
 * Adapter personalizado para mostrar solicitudes y rentas de alquiler
 */
class AlquilerAdapter(
    private var items: List<AlquilerItem>,
    private val onItemClick: ((Int) -> Unit)? = null
) : RecyclerView.Adapter<AlquilerAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvTipo: TextView = view.findViewById(R.id.tvTipo)
        val tvEstado: TextView = view.findViewById(R.id.tvEstado)
        val tvId: TextView = view.findViewById(R.id.tvId)
        val tvFechaItem: TextView = view.findViewById(R.id.tvFechaItem)
        val layoutSolicitud: LinearLayout = view.findViewById(R.id.layoutSolicitud)
        val tvClienteId: TextView = view.findViewById(R.id.tvClienteId)
        val tvCantidad: TextView = view.findViewById(R.id.tvCantidad)
        val tvProductoNombre: TextView = view.findViewById(R.id.tvProductoNombre)
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

    fun updateData(newItems: List<AlquilerItem>) {
        this.items = newItems
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]

        holder.tvTipo.text = if (item.tipo == AlquilerItem.TipoItem.SOLICITUD) "SOLICITUD" else "RENTA"
        holder.tvId.text = if (item.tipo == AlquilerItem.TipoItem.SOLICITUD) "Solicitud #${item.id}" else "Renta #${item.id}"
        holder.tvFechaItem.text = item.fechaReferencia ?: ""

        holder.tvEstado.text = item.estado
        holder.tvEstado.background = createEstadoBackground(item.estado)

        when (item.tipo) {
            AlquilerItem.TipoItem.SOLICITUD -> {
                holder.layoutSolicitud.visibility = View.VISIBLE
                holder.layoutRenta.visibility = View.GONE
                holder.layoutItems.visibility = View.GONE

                holder.tvClienteId.text = "#${item.clienteId}"
                holder.tvCantidad.text = "${item.cantidad ?: 1} unidad${if ((item.cantidad ?: 1) > 1) "es" else ""}"
                holder.tvProductoNombre.text = item.nombreProducto ?: "Producto genérico"

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

        holder.layoutAcciones.visibility = if (item.mostrarAcciones) View.VISIBLE else View.GONE
        holder.itemView.setOnClickListener { onItemClick?.invoke(position) }
    }

    override fun getItemCount() = items.size

    private fun createEstadoBackground(estado: String): GradientDrawable {
        val color = when (estado.uppercase()) {
            "PENDIENTE", "EN_PROCESO", "NUEVA" -> Color.parseColor("#FF9800")
            "APROBADA", "ACTIVA", "EN_CURSO" -> Color.parseColor("#4CAF50")
            "RECHAZADA", "CANCELADA" -> Color.parseColor("#F44336")
            "COMPLETADA", "FINALIZADA" -> Color.parseColor("#2196F3")
            else -> Color.parseColor("#607D8B")
        }
        return GradientDrawable().apply {
            setColor(color)
            cornerRadius = 12f
        }
    }
}
