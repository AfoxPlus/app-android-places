package com.afoxplus.places.cross.mapper

import com.afoxplus.places.domain.entities.Establishment
import com.afoxplus.uikit.objects.vendor.Establishment as UIEstablishment

internal fun Establishment.toUIEstablishment(): UIEstablishment {
    return UIEstablishment(
        imageLandscape = this.imageBanner,
        imagePortrait = this.imageLogo,
        name = this.name,
        description = this.primaryType,
        hasSubscription = this.hasSubscription,
        isOpen = this.isOpen,
        rating = this.rating,
        addressDescription = this.address,
        phoneDescription = this.phone
    )
}