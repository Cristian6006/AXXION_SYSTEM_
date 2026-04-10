package com.example.axxionSystem.repository

import com.example.axxionSystem.model.DispositivoBiometrico
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface DispositivoBiometricoRepository: JpaRepository<DispositivoBiometrico, Long> {

    fun findByDeviceId(deviceId: String): Optional<DispositivoBiometrico>

    fun existsByDeviceId(deviceId: String): Boolean
}
