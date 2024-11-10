package com.afoxplus.module.domain.repository

import com.afoxplus.module.domain.entities.Establishment
import com.afoxplus.module.domain.entities.Location
import com.afoxplus.module.domain.repository.source.network.EstablishmentNetworkDataSource
import com.afoxplus.module.domain.usecases.repositories.EstablishmentRepository
import javax.inject.Inject

internal class EstablishmentRepositorySource @Inject constructor(private val dataSource: EstablishmentNetworkDataSource) :
    EstablishmentRepository {
    override suspend fun fetchEstablishments(
        query: String,
        location: Location?,
        types: List<String>
    ): List<Establishment> = dataSource.fetchEstablishments(query, location, types)

    override suspend fun fetchEstablishmentTypes(): List<String> =
        dataSource.fetchEstablishmentTypes()
}