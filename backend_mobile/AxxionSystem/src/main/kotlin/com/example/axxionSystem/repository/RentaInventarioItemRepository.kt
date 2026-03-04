package com.example.axxionSystem.repository

import com.example.axxionSystem.model.RentaInventarioItem
import com.example.axxionSystem.model.RentaInventarioItemId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RentaInventarioItemRepository : JpaRepository<RentaInventarioItem, RentaInventarioItemId> {
    fun findByIdRentaId(rentaId: Int): List<RentaInventarioItem>
}
