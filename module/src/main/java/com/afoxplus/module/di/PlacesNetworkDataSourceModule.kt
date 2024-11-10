package com.afoxplus.module.di

import com.afoxplus.module.domain.repository.source.network.EstablishmentNetworkDataSource
import com.afoxplus.module.domain.repository.source.network.api.EstablishmentApiNetwork
import com.afoxplus.module.domain.repository.source.network.service.EstablishmentNetworkService
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