package com.afoxplus.places.domain.usecases.actions

import com.afoxplus.places.domain.entities.Establishment

fun interface FetchEstablishmentByQuery {
    suspend fun invoke(query: String): List<Establishment>
}