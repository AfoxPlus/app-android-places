package com.afoxplus.module.domain.usecases.actions

import com.afoxplus.module.domain.entities.Establishment
import com.afoxplus.module.domain.entities.Location

fun interface FetchEstablishments {
    suspend operator fun invoke(types: List<String>, location: Location?): List<Establishment>
}