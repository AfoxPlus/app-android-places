package com.afoxplus.places.domain.repository.source.network.api.response

import com.afoxplus.places.domain.entities.Establishment
import com.google.gson.annotations.SerializedName

internal data class EstablishmentResponse(
    @SerializedName("code") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("primaryType") val primaryType: String,
    @SerializedName("description") val description: String,
    @SerializedName("hasSubscription") val hasSubscription: Boolean,
    @SerializedName("urlImageBanner") val urlImageBanner: String,
    @SerializedName("urlImageLogo") val imageLogo: String,
    @SerializedName("openNow") val isOpen: Boolean,
    @SerializedName("isVerified") val isVerify: Boolean,
    @SerializedName("rating") val rating: Float,
    @SerializedName("userRatingCount") val ratingCount: Int,
    @SerializedName("address") val address: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("email") val email: String,
    @SerializedName("googleMapsUri") val googleMapUri: String,
    @SerializedName("websiteUri") val websiteUrl: String,
    @SerializedName("postalCode") val zoneCode: String,
    @SerializedName("areaLevel2") val zoneName: String,
    @SerializedName("areaLevel1") val city: String,
    @SerializedName("country") val country: String,
    @SerializedName("location") val location: LocationResponse,
    @SerializedName("types") val types: List<EstablishmentTypeResponse>
) {
    fun toDomain() = Establishment(
        id,
        name,
        primaryType,
        description,
        hasSubscription,
        isOpen,
        isVerify,
        rating,
        ratingCount,
        address,
        phone,
        email,
        imageLogo,
        urlImageBanner,
        googleMapUri,
        websiteUrl,
        zoneCode,
        zoneName,
        city,
        country,
        location = location.toDomain(),
        types = listOf()
    )
}
