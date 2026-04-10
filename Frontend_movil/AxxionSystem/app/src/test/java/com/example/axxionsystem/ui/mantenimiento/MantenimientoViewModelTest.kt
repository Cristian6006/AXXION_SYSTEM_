package com.example.axxionsystem.ui.mantenimiento

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.axxionsystem.data.model.mantenimiento.Mantenimiento
import com.example.axxionsystem.data.model.mantenimiento.MantenimientoResponse
import com.example.axxionsystem.data.repository.mantenimiento.MantenimientoRepository
import com.example.axxionsystem.util.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Pruebas unitarias para el ViewModel de Mantenimiento.
 * Utiliza MockK para simular el Repositorio.
 */
@ExperimentalCoroutinesApi
class MantenimientoViewModelTest {

    // Regla para ejecutar LiveData de forma síncrona en las pruebas
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val repository: MantenimientoRepository = mockk()
    private lateinit var viewModel: MantenimientoViewModel

    @Before
    fun setup() {
        // Configuramos el Dispatcher principal para pruebas de corrutinas
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = MantenimientoViewModel(repository)
    }

    @Test
    fun `cargarMantenimientos actualiza la lista en caso de exito`() {
        // Given (Dado que)
        val mockData = listOf(
            MantenimientoResponse(
                success = true,
                message = "Exito",
                mantenimiento = Mantenimiento(1, "Prueba", "2024-03-15", "Pendiente")
            )
        )
        coEvery { repository.consultarSolicitudesMantenimiento(any()) } returns Result.Success(mockData)

        // When (Cuando)
        viewModel.cargarMantenimientos()

        // Then (Entonces)
        assertEquals(mockData, viewModel.mantenimientos.value)
    }

    @Test
    fun `crearMantenimiento muestra error si los campos estan vacios`() {
        // When
        viewModel.crearMantenimiento("", "", "", null)

        // Then
        assertEquals("Todos los campos son obligatorios", viewModel.error.value)
    }
}
