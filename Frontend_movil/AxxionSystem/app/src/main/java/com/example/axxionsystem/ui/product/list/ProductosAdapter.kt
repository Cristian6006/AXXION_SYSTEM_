package com.example.axxionsystem.ui.product.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.axxionsystem.R
import com.example.axxionsystem.data.model.producto.ProductoEntity
import com.example.axxionsystem.databinding.ItemProductoBinding

class ProductosAdapter(
    private val onItemClick: (ProductoEntity) -> Unit
) : ListAdapter<ProductoEntity, ProductosAdapter.ProductoViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val binding = ItemProductoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = getItem(position)
        holder.bind(producto)
    }

    inner class ProductoViewHolder(private val binding: ItemProductoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(producto: ProductoEntity) {
            binding.apply {
                tvNombre.text = producto.nombre
                tvMarcaModelo.text = "${producto.marca} - ${producto.modelo}"

                tvEstado.text = producto.estado.replace("_", " ")
                setupStatusStyle(producto.estado)

                root.setOnClickListener { onItemClick(producto) }
            }
        }

        private fun setupStatusStyle(estado: String) {
            val context = binding.root.context

            val colorRes = when (estado) {
                "DISPONIBLE" -> R.color.axxion_secondary // Azul claro
                "RENTADO" -> R.color.axxion_primary // Azul fuerte
                "EN_REPARACION" -> R.color.axxion_text_muted // Gris/Muted
                else -> R.color.axxion_input_stroke // Borde neutro
            }

            binding.tvEstado.setBackgroundColor(ContextCompat.getColor(context, colorRes))

            // Si el fondo es muy claro, ponemos el texto oscuro para contraste
            if (estado == "DISPONIBLE") {
                binding.tvEstado.setTextColor(ContextCompat.getColor(context, R.color.axxion_background))
            } else {
                binding.tvEstado.setTextColor(ContextCompat.getColor(context, R.color.white))
            }
        }
    }


    companion object DiffCallback : DiffUtil.ItemCallback<ProductoEntity>() {
        override fun areItemsTheSame(oldItem: ProductoEntity, newItem: ProductoEntity): Boolean {
            // Comparamos por ID único (Referencia)
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductoEntity, newItem: ProductoEntity): Boolean {
            // Comparamos todo el contenido del Data Class (Contenido)
            return oldItem == newItem
        }
    }
}