package com.afoxplus.places.delivery.events

import com.afoxplus.places.domain.entities.Establishment
import com.afoxplus.uikit.bus.UIKitEventBus

data class OnClickEstablishmentEvent(val establishment: Establishment) : UIKitEventBus