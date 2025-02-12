package com.afoxplus.places.domain.usecases

import com.afoxplus.places.domain.entities.Establishment
import com.afoxplus.places.domain.entities.Location
import com.afoxplus.places.domain.usecases.actions.FetchEstablishments
import com.afoxplus.places.domain.usecases.repositories.EstablishmentRepository
import javax.inject.Inject

internal class FetchEstablishmentsUseCase @Inject constructor(private val establishmentRepository: EstablishmentRepository) :
    FetchEstablishments {

    override suspend fun invoke(types: List<String>, location: Location?): List<Establishment> =
        establishmentRepository.fetchEstablishments(location, types)
}
