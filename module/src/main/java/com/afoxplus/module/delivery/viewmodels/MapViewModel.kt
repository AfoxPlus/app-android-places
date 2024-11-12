package com.afoxplus.module.delivery.viewmodels

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afoxplus.module.domain.usecases.actions.FetchEstablishmentTypes
import com.afoxplus.module.domain.usecases.actions.FetchEstablishments
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
    private val uiKitCoroutineDispatcher: UIKitCoroutineDispatcher
) : ViewModel() {

    private val mChips: MutableStateFlow<ListState<ChipItem>> by lazy {
        MutableStateFlow(
            ListEmptyData()
        )
    }
    val chips: StateFlow<ListState<ChipItem>> get() = mChips

    private val mCurrentPosition: MutableStateFlow<Location?> = MutableStateFlow(null)
    val currentPosition: StateFlow<Location?> get() = mCurrentPosition

    private val mEstablishments: MutableStateFlow<ListState<Establishment>> by lazy {
        MutableStateFlow(
            ListEmptyData()
        )
    }
    val establishments: StateFlow<ListState<Establishment>> get() = mEstablishments

    private val mMarkers: MutableStateFlow<List<com.afoxplus.module.domain.entities.Establishment>> =
        MutableStateFlow(listOf())
    val markers: StateFlow<List<com.afoxplus.module.domain.entities.Establishment>> get() = mMarkers

    private val selectedTypes: MutableList<String> = mutableListOf()

    init {
        fetchChips()
    }

    fun fetchChips() = viewModelScope.launch(uiKitCoroutineDispatcher.getIODispatcher()) {
        try {
            mChips.value = ListLoading()
            val results = fetchCategories.invoke()
            mChips.value = ListSuccess(results.map { ChipItem("", false, it) })
        } catch (ex: Exception) {
            mChips.value = ListError(ex)
        }
    }

    fun fetchEstablishments() =
        viewModelScope.launch(uiKitCoroutineDispatcher.getIODispatcher()) {
            try {
                mEstablishments.value = ListLoading()
                mMarkers.value = listOf()
                val results = fetchEstablishments.invoke(
                    selectedTypes,
                    currentPosition.value?.let {
                        com.afoxplus.module.domain.entities.Location(
                            it.latitude,
                            it.longitude
                        )
                    })
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
                mMarkers.value = results
            } catch (ex: Exception) {
                mMarkers.value = listOf()
                mEstablishments.value = ListError(ex)
            }
        }

    fun setLocation(location: Location?) {
        mCurrentPosition.value = location
        fetchEstablishments()
    }

    fun selectedChips(chipItems: List<ChipItem>) {
        selectedTypes.clear()
        selectedTypes.addAll(chipItems.map { it.name })
        fetchEstablishments()
    }

    fun onEstablishmentClick(establishment: Establishment) {

    }
}