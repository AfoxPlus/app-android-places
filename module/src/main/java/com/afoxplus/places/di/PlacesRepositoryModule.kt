package com.afoxplus.places.di

import com.afoxplus.places.domain.repository.EstablishmentRepositorySource
import com.afoxplus.places.domain.repository.source.network.EstablishmentNetworkDataSource
import com.afoxplus.places.domain.usecases.repositories.EstablishmentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object PlacesRepositoryModule {
    @Provides
    fun provideEstablishmentRepository(
        establishmentNetworkDataSource: EstablishmentNetworkDataSource
    ): EstablishmentRepository = EstablishmentRepositorySource(establishmentNetworkDataSource)
}