package com.afoxplus.places.di

import com.afoxplus.places.domain.usecases.FetchEstablishmentByQueryUseCase
import com.afoxplus.places.domain.usecases.FetchEstablishmentTypesUseCase
import com.afoxplus.places.domain.usecases.FetchEstablishmentsUseCase
import com.afoxplus.places.domain.usecases.actions.FetchEstablishmentByQuery
import com.afoxplus.places.domain.usecases.actions.FetchEstablishmentTypes
import com.afoxplus.places.domain.usecases.actions.FetchEstablishments
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

    @Binds
    fun bindFetchEstablishmentsByQueryUseCase(fetchEstablishmentByQueryUseCase: FetchEstablishmentByQueryUseCase): FetchEstablishmentByQuery
}