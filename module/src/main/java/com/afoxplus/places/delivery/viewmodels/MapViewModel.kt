package com.afoxplus.places.delivery.viewmodels

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afoxplus.places.delivery.events.OnClickEstablishmentEvent
import com.afoxplus.places.domain.usecases.actions.FetchEstablishmentTypes
import com.afoxplus.places.domain.usecases.actions.FetchEstablishments
import com.afoxplus.uikit.bus.UIKitEventBusWrapper
import com.afoxplus.uikit.di.UIKitCoroutineDispatcher
import com.afoxplus.uikit.objects.vendor.ChipItem
import com.afoxplus.uikit.objects.vendor.Establishment
import com.afoxplus.uikit.views.status.ListEmptyData
import com.afoxplus.uikit.views.status.ListError
import com.afoxplus.uikit.views.status.ListLoading
import com.afoxplus.uikit.views.status.ListState
import com.afoxplus.uikit.views.status.ListSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val fetchCategories: FetchEstablishmentTypes,
    private val fetchEstablishments: FetchEstablishments,
    private val uiKitCoroutineDispatcher: UIKitCoroutineDispatcher,
    private val eventBusWrapper: UIKitEventBusWrapper
) : ViewModel() {

    private val mChips: MutableStateFlow<ListState<ChipItem>> by lazy {
        MutableStateFlow(
            ListEmptyData()
        )
    }
    val chips: StateFlow<ListState<ChipItem>> get() = mChips

    private val mLastKnownLocation: MutableStateFlow<Location?> = MutableStateFlow(null)
    val lastKnownLocation: StateFlow<Location?> get() = mLastKnownLocation

    private val mEstablishments: MutableStateFlow<ListState<Establishment>> by lazy {
        MutableStateFlow(
            ListEmptyData()
        )
    }
    val establishments: StateFlow<ListState<Establishment>> get() = mEstablishments

    private val mEstablishmentResults: MutableStateFlow<List<com.afoxplus.places.domain.entities.Establishment>> =
        MutableStateFlow(listOf())
    val establishmentsResults: StateFlow<List<com.afoxplus.places.domain.entities.Establishment>> get() = mEstablishmentResults

    private val selectedTypes: MutableList<String> = mutableListOf()

    init {
        fetchChips()
    }

    private fun fetchChips() = viewModelScope.launch(uiKitCoroutineDispatcher.getIODispatcher()) {
        try {
            mChips.value = ListLoading()
            val results = fetchCategories.invoke()
            mChips.value = ListSuccess(results.map { ChipItem("", false, it) })
        } catch (ex: Exception) {
            mChips.value = ListError(ex)
        }
    }

    private fun fetchEstablishments(location: com.afoxplus.places.domain.entities.Location) =
        viewModelScope.launch(uiKitCoroutineDispatcher.getIODispatcher()) {
            try {
                mEstablishments.value = ListLoading()
                mEstablishmentResults.value = listOf()
                val results = fetchEstablishments.invoke(
                    selectedTypes,
                    location
                )
                mEstablishments.value = ListSuccess(results.map {
                    Establishment(
                        imageLandscape = it.imageBanner,
                        imagePortrait = it.imageLogo,
                        name = it.name,
                        description = it.description,
                        hasSubscription = it.hasSubscription,
                        isOpen = it.isOpen,
                        rating = it.rating,
                        addressDescription = it.address,
                        phoneDescription = it.phone
                    )
                })
                mEstablishmentResults.value = results
            } catch (ex: Exception) {
                mEstablishmentResults.value = listOf()
                mEstablishments.value = ListError(ex)
            }
        }

    fun setMapCurrentLocation(location: Location?) {
        if (location != lastKnownLocation.value) {
            mLastKnownLocation.value = location
            location?.let {
                fetchEstablishments(
                    com.afoxplus.places.domain.entities.Location(
                        it.latitude,
                        it.longitude
                    )
                )
            }
        }
    }

    fun selectedChips(chipItems: List<ChipItem>) {
        selectedTypes.clear()
        selectedTypes.addAll(chipItems.map { it.name })
        mLastKnownLocation.value?.let {
            fetchEstablishments(
                com.afoxplus.places.domain.entities.Location(
                    it.latitude,
                    it.longitude
                )
            )
        }
    }

    fun onEstablishmentClick(index: Int) {
        viewModelScope.launch(uiKitCoroutineDispatcher.getMainDispatcher()) {
            eventBusWrapper.send(OnClickEstablishmentEvent(mEstablishmentResults.value[index]))
        }
    }

    fun handleResultEstablishment(location: com.afoxplus.places.domain.entities.Location) {
        fetchEstablishments(location)
    }
}
