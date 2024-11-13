package com.afoxplus.places.domain.usecases

import com.afoxplus.places.domain.entities.Establishment
import com.afoxplus.places.domain.usecases.actions.FetchEstablishmentByQuery
import com.afoxplus.places.domain.usecases.repositories.EstablishmentRepository
import javax.inject.Inject

internal class FetchEstablishmentByQueryUseCase @Inject constructor(private val repository: EstablishmentRepository) :
    FetchEstablishmentByQuery {
    override suspend fun invoke(query: String): List<Establishment> =
        repository.fetchEstablishments(query)
}