package com.example.axxionsystem.common.adapters

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.axxionsystem.common.model.ProductoResponse
import com.example.axxionsystem.R

class ProductAdapter(
    private var items: List<ProductoResponse>,
    private val onItemClick: (ProductoResponse) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textNombre: TextView = view.findViewById(R.id.textNombre)
        val textPrecio: TextView = view.findViewById(R.id.textPrecio)
        val textDetalles: TextView = view.findViewById(R.id.textDetalles)
        val viewStatus: View = view.findViewById(R.id.viewStatus)
        val chipCategoria: TextView = view.findViewById(R.id.chipCategoria)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.textNombre.text = item.nombre
        holder.textPrecio.text = "$${item.precioAlquilerDia ?: "0.00"}"
        holder.textDetalles.text = "Marca: ${item.marca ?: "Genérica"} | Modelo: ${item.modelo ?: "N/A"}"
        holder.chipCategoria.text = item.categoria ?: "General"

        // Map status to colors (using gradients)
        val statusRes = when(item.estado?.lowercase()) {
            "disponible" -> R.drawable.bg_gradient_green
            "alquilado" -> R.drawable.bg_gradient_blue
            "mantenimiento" -> R.drawable.bg_gradient_orange
            else -> R.drawable.bg_gradient_purple
        }
        holder.viewStatus.setBackgroundResource(statusRes)

        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount() = items.size

    fun updateData(newItems: List<ProductoResponse>) {
        items = newItems
        notifyDataSetChanged()
    }
}
