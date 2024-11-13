package com.afoxplus.places.domain.repository.source.network.api.response

import com.afoxplus.places.domain.entities.Location
import com.google.gson.annotations.SerializedName

data class LocationResponse(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double
) {

    fun toDomain() = Location(latitude, longitude)
}
