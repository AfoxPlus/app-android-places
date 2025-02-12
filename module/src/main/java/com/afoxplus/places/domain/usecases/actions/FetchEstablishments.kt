package com.afoxplus.places.domain.usecases.actions

import com.afoxplus.places.domain.entities.Establishment
import com.afoxplus.places.domain.entities.Location

fun interface FetchEstablishments {
    suspend operator fun invoke(types: List<String>, location: Location?): List<Establishment>
}