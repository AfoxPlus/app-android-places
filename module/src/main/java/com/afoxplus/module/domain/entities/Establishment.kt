package com.afoxplus.module.domain.entities

data class Establishment(
    val id: String,
    val name: String,
    val primaryType: String,
    val description: String,
    val hasSubscription: Boolean,
    val isOpen: Boolean,
    val isVerify: Boolean,
    val rating: Float,
    val ratingCount: Int,
    val address: String,
    val phone: String,
    val email: String,
    val imageLogo: String,
    val imageBanner: String,
    val googleMapUri: String,
    val websiteUrl: String,
    val zoneCode: String,
    val zoneName: String,
    val city: String,
    val country: String,
    val location: Location,
    val types: List<String>
)