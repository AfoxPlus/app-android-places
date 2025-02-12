package com.afoxplus.places.domain.usecases.repositories

import com.afoxplus.places.domain.entities.Establishment
import com.afoxplus.places.domain.entities.Location

interface EstablishmentRepository {
    suspend fun fetchEstablishments(
        location: Location?,
        types: List<String>
    ): List<Establishment>

    suspend fun fetchEstablishmentTypes(): List<String>

    suspend fun fetchEstablishments(query: String): List<Establishment>
}