package com.afoxplus.module.domain.repository.source.network.api.request

import com.google.gson.annotations.SerializedName

internal data class FilterRequest(
    @SerializedName("types") val types: List<String>,
    @SerializedName("coordinates") val coordinates: List<Double>,
    @SerializedName("maxDistance") val maxDistance: Long = 500
)