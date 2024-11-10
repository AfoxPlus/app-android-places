package com.afoxplus.module.di

import com.afoxplus.module.domain.usecases.FetchEstablishmentTypesUseCase
import com.afoxplus.module.domain.usecases.FetchEstablishmentsUseCase
import com.afoxplus.module.domain.usecases.actions.FetchEstablishmentTypes
import com.afoxplus.module.domain.usecases.actions.FetchEstablishments
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
internal interface PlacesUseCaseModule {
    @Binds
    fun bindFetchEstablishmentsUseCase(fetchEstablishmentsUseCase: FetchEstablishmentsUseCase): FetchEstablishments

    @Binds
    fun bindFetchEstablishmentTypesUseCase(fetchEstablishmentTypesUseCase: FetchEstablishmentTypesUseCase): FetchEstablishmentTypes

}