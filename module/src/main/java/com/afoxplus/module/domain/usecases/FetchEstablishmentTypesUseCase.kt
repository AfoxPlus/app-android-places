package com.afoxplus.module.domain.usecases

import com.afoxplus.module.domain.usecases.actions.FetchEstablishmentTypes
import com.afoxplus.module.domain.usecases.repositories.EstablishmentRepository
import javax.inject.Inject

internal class FetchEstablishmentTypesUseCase
@Inject constructor(
    private val establishmentRepository: EstablishmentRepository
) : FetchEstablishmentTypes {
    override suspend fun invoke(): List<String> = establishmentRepository.fetchEstablishmentTypes()
}