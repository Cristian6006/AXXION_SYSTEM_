package com.example.axxionsystem.common.adapters

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.View

/**
 * Adapter genérico y reutilizable para mostrar listas de texto con estilo dark theme.
 * Se usa en AlquilerActivity y MantenimientoActivity.
 *
 * @param items  Lista de textos a mostrar
 * @param accentColor Color hexadecimal de acento (ej "#4FC3F7")
 * @param onItemClick Callback opcional al pulsar un ítem (recibe el índice)
 */
class SimpleTextAdapter(
    private val items: List<String>,
    private val accentColor: String = "#4FC3F7",
    private val onItemClick: ((Int) -> Unit)? = null
) : RecyclerView.Adapter<SimpleTextAdapter.VH>() {

    inner class VH(val tv: TextView) : RecyclerView.ViewHolder(tv)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val tv = TextView(parent.context).apply {
            layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 0, 0, 2) }
            setBackgroundColor(0xFF1A3A5C.toInt())
            setTextColor(0xFFE8EAF6.toInt())
            textSize = 13f
            setPadding(32, 20, 32, 20)
        }
        return VH(tv)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.tv.text = items[position]
        onItemClick?.let { click ->
            holder.tv.setOnClickListener { click(position) }
        }
    }

    override fun getItemCount() = items.size
}
