package com.afoxplus.places.domain.repository.source.network.api.request

import com.google.gson.annotations.SerializedName

internal data class FilterRequest(
    @SerializedName("types") val types: List<String>,
    @SerializedName("coordinates") val coordinates: List<Double>,
    @SerializedName("maxDistance") val maxDistance: Long = 5000
)