package com.afoxplus.places.domain.repository

import com.afoxplus.places.domain.entities.Establishment
import com.afoxplus.places.domain.entities.Location
import com.afoxplus.places.domain.repository.source.network.EstablishmentNetworkDataSource
import com.afoxplus.places.domain.usecases.repositories.EstablishmentRepository
import javax.inject.Inject

internal class EstablishmentRepositorySource @Inject constructor(private val dataSource: EstablishmentNetworkDataSource) :
    EstablishmentRepository {
    override suspend fun fetchEstablishments(
        location: Location?,
        types: List<String>
    ): List<Establishment> = dataSource.fetchEstablishments(location, types)

    override suspend fun fetchEstablishments(query: String): List<Establishment> =
        dataSource.fetchEstablishments(query)

    override suspend fun fetchEstablishmentTypes(): List<String> =
        dataSource.fetchEstablishmentTypes()
}