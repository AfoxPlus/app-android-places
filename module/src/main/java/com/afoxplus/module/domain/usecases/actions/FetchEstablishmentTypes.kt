package com.afoxplus.module.domain.usecases.actions

fun interface FetchEstablishmentTypes {
    suspend operator fun invoke(): List<String>
}