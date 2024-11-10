package com.afoxplus.module.di

import com.afoxplus.module.domain.repository.source.network.api.EstablishmentApiNetwork
import com.afoxplus.network.api.RetrofitGenerator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object PlacesApiNetworkModule {

    @Provides
    fun providerEstablishmentService(
        retrofitGenerator: RetrofitGenerator
    ): EstablishmentApiNetwork =
        retrofitGenerator.createRetrofit(EstablishmentApiNetwork::class.java)
}