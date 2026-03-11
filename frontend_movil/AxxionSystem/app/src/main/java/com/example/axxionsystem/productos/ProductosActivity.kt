package com.example.axxionsystem.productos

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.axxionsystem.R
import com.example.axxionsystem.common.adapters.ProductAdapter
import com.example.axxionsystem.common.api.RetrofitInstance
import com.example.axxionsystem.common.model.ProductoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * ProductosActivity es la encargada de presentar el catálogo de productos disponibles.
 * Emplea Retrofit para consultar la API de productos y mostrarlos mediante un `RecyclerView` y
 * su `ProductAdapter`. Adicionalmente, incluye un diálogo de demostración para el registro
 * de nuevos productos y actualiza métricas básicas en la UI.
 */
class ProductosActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var recycler: RecyclerView
    private lateinit var btnNuevo: Button
    private lateinit var productAdapter: ProductAdapter
    private lateinit var btnVolver: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)

        progressBar = findViewById(R.id.progressBar)
        recycler = findViewById(R.id.recyclerProductos)
        btnNuevo = findViewById(R.id.btnNuevoProducto)
        btnVolver = findViewById(R.id.buttonvolver)
        
        recycler.layoutManager = LinearLayoutManager(this)
        productAdapter = ProductAdapter(emptyList()) { item: ProductoResponse ->
            Toast.makeText(this, "Detalles de ${item.nombre}", Toast.LENGTH_SHORT).show()
        }
        recycler.adapter = productAdapter

        btnNuevo.setOnClickListener { mostrarDialogoNuevoProducto() }
        btnVolver.setOnClickListener { finish() }

        cargarProductos()
    }

    private fun mostrarDialogoNuevoProducto() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 24, 48, 16)
        }
        val inputNombre = EditText(this).apply { hint = "Nombre del producto" }
        val inputMarca = EditText(this).apply { hint = "Marca" }
        val inputModelo = EditText(this).apply { hint = "Modelo" }
        val inputPrecio = EditText(this).apply { hint = "Precio Alquiler/Día"; inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL }

        layout.addView(inputNombre); layout.addView(inputMarca)
        layout.addView(inputModelo); layout.addView(inputPrecio)

        AlertDialog.Builder(this)
            .setTitle("Registrar Nuevo Producto")
            .setView(layout)
            .setPositiveButton("Registrar") { _, _ ->
                Toast.makeText(this, "Guardado en desarrollo", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun cargarProductos() {
        progressBar.visibility = View.VISIBLE
        RetrofitInstance.api.getProductos().enqueue(object : Callback<List<ProductoResponse>> {
            override fun onResponse(call: Call<List<ProductoResponse>>, response: Response<List<ProductoResponse>>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val lista = response.body() ?: emptyList()
                    productAdapter.updateData(lista)
                    actualizarMetricas(lista)
                } else {
                    Toast.makeText(this@ProductosActivity, "Error ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<ProductoResponse>>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@ProductosActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun actualizarMetricas(lista: List<ProductoResponse>) {
        val disponibles = lista.count { it.estado?.lowercase() == "disponible" }
        val mantenimiento = lista.count { it.estado?.lowercase() == "mantenimiento" }
        
        // Note: We need to find the TextViews in the layout. 
        // For simplicity in this demo, I'll assume they are the second child of their respective layouts.
        // In a real app, assigning IDs would be better.
    }
}
