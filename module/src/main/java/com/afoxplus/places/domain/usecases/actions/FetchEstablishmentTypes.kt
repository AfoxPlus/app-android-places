package com.afoxplus.places.domain.usecases.actions

fun interface FetchEstablishmentTypes {
    suspend operator fun invoke(): List<String>
}