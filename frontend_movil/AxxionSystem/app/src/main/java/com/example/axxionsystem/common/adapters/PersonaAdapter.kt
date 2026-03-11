package com.example.axxionsystem.common.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class PersonaAdapter(private val personas: List<String>) : RecyclerView.Adapter<PersonaAdapter.PersonaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return PersonaViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonaViewHolder, position: Int) {
        holder.bind(personas[position], position + 1)
    }

    override fun getItemCount(): Int = personas.size

    class PersonaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val text1: TextView = itemView.findViewById(android.R.id.text1)
        private val text2: TextView = itemView.findViewById(android.R.id.text2)

        fun bind(persona: String, index: Int) {
            text1.text = persona
            text1.setTextColor(0xFFE8EAF6.toInt())
            text1.textSize = 15f
            text2.text = "Usuario #$index"
            text2.setTextColor(0xFF78909C.toInt())
            text2.textSize = 12f
            itemView.setBackgroundColor(0xFF112240.toInt())
            itemView.setPadding(32, 20, 32, 20)
        }
    }
}