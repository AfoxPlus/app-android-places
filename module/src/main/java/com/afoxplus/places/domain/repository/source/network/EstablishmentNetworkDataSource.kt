package com.afoxplus.places.domain.repository.source.network

import com.afoxplus.places.domain.entities.Establishment
import com.afoxplus.places.domain.entities.Location

internal interface EstablishmentNetworkDataSource {
    suspend fun fetchEstablishments(
        location: Location?,
        types: List<String>
    ): List<Establishment>

    suspend fun fetchEstablishmentTypes(): List<String>

    suspend fun fetchEstablishments(query: String): List<Establishment>
}