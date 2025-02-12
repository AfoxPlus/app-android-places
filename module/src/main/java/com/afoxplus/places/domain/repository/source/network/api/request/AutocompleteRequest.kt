package com.afoxplus.places.domain.repository.source.network.api.request

import com.google.gson.annotations.SerializedName

data class AutocompleteRequest(@SerializedName("query") val query: String)
