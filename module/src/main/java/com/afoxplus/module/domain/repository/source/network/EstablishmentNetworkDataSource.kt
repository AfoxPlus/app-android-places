package com.afoxplus.module.domain.repository.source.network

import com.afoxplus.module.domain.entities.Establishment
import com.afoxplus.module.domain.entities.Location

internal interface EstablishmentNetworkDataSource {
    suspend fun fetchEstablishments(
        query: String,
        location: Location?,
        types: List<String>
    ): List<Establishment>

    suspend fun fetchEstablishmentTypes(): List<String>
}