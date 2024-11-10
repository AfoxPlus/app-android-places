package com.afoxplus.module.domain.usecases.repositories

import com.afoxplus.module.domain.entities.Establishment
import com.afoxplus.module.domain.entities.Location

interface EstablishmentRepository {
    suspend fun fetchEstablishments(
        query: String,
        location: Location?,
        types: List<String>
    ): List<Establishment>

    suspend fun fetchEstablishmentTypes(): List<String>
}