package com.afoxplus.places.domain.repository.source.network.service

import com.afoxplus.places.domain.entities.Establishment
import com.afoxplus.places.domain.entities.Location
import com.afoxplus.places.domain.repository.source.network.EstablishmentNetworkDataSource
import com.afoxplus.places.domain.repository.source.network.api.EstablishmentApiNetwork
import com.afoxplus.places.domain.repository.source.network.api.request.AutocompleteRequest
import com.afoxplus.places.domain.repository.source.network.api.request.FilterRequest
import javax.inject.Inject

internal class EstablishmentNetworkService
@Inject constructor(
    private val api: EstablishmentApiNetwork
) : EstablishmentNetworkDataSource {
    override suspend fun fetchEstablishments(
        location: Location?,
        types: List<String>
    ): List<Establishment> {
        val response = api.fetchEstablishments(
            FilterRequest(
                types,
                location?.let { listOf(it.latitude, it.longitude) } ?: listOf()))

        return response.body()?.payload?.map { it.toDomain() } ?: emptyList()
    }

    override suspend fun fetchEstablishments(query: String): List<Establishment> {
        val response = api.fetchEstablishmentsByQuery(AutocompleteRequest(query = query))
        return response.body()?.payload?.map { it.toDomain() } ?: emptyList()
    }

    override suspend fun fetchEstablishmentTypes(): List<String> {
        val response = api.fetchEstablishmentTypes()
        return response.body()?.payload ?: emptyList()
    }
}