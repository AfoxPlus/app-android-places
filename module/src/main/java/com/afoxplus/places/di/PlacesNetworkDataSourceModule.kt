package com.afoxplus.places.di

import com.afoxplus.places.domain.repository.source.network.EstablishmentNetworkDataSource
import com.afoxplus.places.domain.repository.source.network.api.EstablishmentApiNetwork
import com.afoxplus.places.domain.repository.source.network.service.EstablishmentNetworkService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object PlacesNetworkDataSourceModule {
    @Provides
    fun provideEstablishmentNetworkDataSource(establishmentService: EstablishmentApiNetwork): EstablishmentNetworkDataSource =
        EstablishmentNetworkService(establishmentService)

}