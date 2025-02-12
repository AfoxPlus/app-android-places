package com.afoxplus.places.demo.di

import com.afoxplus.demo_config.delivery.flow.StartDemoFlow
import com.afoxplus.places.demo.global.SampleStartDemoFlow
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface SampleDemoModule {

    @Binds
    fun bindStartDemoFlow(impl: SampleStartDemoFlow): StartDemoFlow
}