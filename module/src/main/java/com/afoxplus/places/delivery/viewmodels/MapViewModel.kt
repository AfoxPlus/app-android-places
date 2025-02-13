package com.afoxplus.places.delivery.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afoxplus.places.delivery.events.OnClickEstablishmentEvent
import com.afoxplus.places.delivery.viewobjects.EstablishmentVO
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
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import com.afoxplus.places.domain.entities.Location as PlaceLocation
import com.afoxplus.places.domain.entities.Establishment as PlaceEstablishment
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val fetchCategories: FetchEstablishmentTypes,
    private val fetchEstablishments: FetchEstablishments,
    private val uiKitCoroutineDispatcher: UIKitCoroutineDispatcher,
    private val eventBusWrapper: UIKitEventBusWrapper
) : ViewModel() {

    private val mLastKnownLocation: MutableStateFlow<PlaceLocation> =
        MutableStateFlow(DEFAULT_COORDINATES)

    private val mChips: MutableStateFlow<ListState<ChipItem>> by lazy {
        MutableStateFlow(ListEmptyData())
    }
    val chips = mChips.asStateFlow()

    private val mEstablishmentsState: MutableStateFlow<ListState<Establishment>> by lazy {
        MutableStateFlow(ListEmptyData())
    }
    val establishmentState = mEstablishmentsState.asStateFlow()

    private val mEstablishmentMarkers: MutableStateFlow<List<EstablishmentVO>> =
        MutableStateFlow(listOf())
    val establishmentMarkers: StateFlow<List<EstablishmentVO>> get() = mEstablishmentMarkers

    private val selectedTypes: MutableList<String> = mutableListOf()
    private val establishmentResult: MutableList<PlaceEstablishment> = mutableListOf()

    private val chipsResults: MutableList<String> = mutableListOf()

    private val mCameraPositionState: MutableSharedFlow<PlaceLocation> by lazy { MutableSharedFlow() }
    val mapCameraPositionState = mCameraPositionState.asSharedFlow()

    init {
        fetchChips()
    }

    private fun fetchChips() = viewModelScope.launch(uiKitCoroutineDispatcher.getIODispatcher()) {
        try {
            mChips.value = ListLoading()
            val results = fetchCategories.invoke()
            chipsResults.clear()
            chipsResults.addAll(results)
            mChips.value = ListSuccess(results.map { ChipItem(it, false) })
        } catch (ex: Exception) {
            mChips.value = ListError(ex)
        }
    }

    fun fetchEstablishments() {
        fetchEstablishments(mLastKnownLocation.value)
    }

    private fun fetchEstablishments(location: PlaceLocation) =
        viewModelScope.launch(uiKitCoroutineDispatcher.getIODispatcher()) {
            try {
                mEstablishmentsState.value = ListLoading()
                mEstablishmentMarkers.value = listOf()
                val results = fetchEstablishments.invoke(
                    selectedTypes,
                    location
                )
                mEstablishmentsState.value = ListSuccess(results.map {
                    Establishment(
                        imageLandscape = it.imageBanner,
                        imagePortrait = it.imageLogo,
                        name = it.name,
                        primaryType = it.primaryType,
                        description = it.description,
                        hasSubscription = it.hasSubscription,
                        isOpen = it.isOpen,
                        rating = it.rating,
                        addressDescription = it.address,
                        phoneDescription = it.phone
                    )
                })
                establishmentResult.clear()
                establishmentResult.addAll(results)
                mapEstablishmentVO(results, 0)

            } catch (ex: Exception) {
                mEstablishmentMarkers.value = listOf()
                mEstablishmentsState.value = ListError(ex)
            }
        }

    private fun mapEstablishmentVO(
        list: List<PlaceEstablishment>,
        selectedIndex: Int
    ) {
        viewModelScope.launch(uiKitCoroutineDispatcher.getMainDispatcher()) {
            if (list.isNotEmpty()) {
                val establishmentVOs = list.map { EstablishmentVO(false, it) }
                establishmentVOs[selectedIndex].isSelected = true
                mEstablishmentMarkers.value = establishmentVOs
                mCameraPositionState.emit(establishmentVOs[selectedIndex].establishment.location)
            }
        }
    }

    fun updateLocation(latLng: LatLng) {
        mLastKnownLocation.value =
            PlaceLocation(latitude = latLng.latitude, longitude = latLng.longitude)
    }

    fun setMapCurrentLocation(location: PlaceLocation) {
        viewModelScope.launch(uiKitCoroutineDispatcher.getMainDispatcher()) {
            mCameraPositionState.emit(location)
            if (location.latitude != mLastKnownLocation.value.latitude
                && location.longitude != mLastKnownLocation.value.longitude
            ) {
                mLastKnownLocation.value = location
            }
            fetchEstablishments(location)
        }
    }

    fun selectedChips(chipItems: List<ChipItem>) {
        selectedTypes.clear()
        selectedTypes.addAll(chipItems.map { it.name })
        mapSelectedChips(chipItems.map { it.name })
        fetchEstablishments(mLastKnownLocation.value)
    }

    fun onEstablishmentClick(index: Int) {
        viewModelScope.launch(uiKitCoroutineDispatcher.getMainDispatcher()) {
            eventBusWrapper.send(OnClickEstablishmentEvent(establishmentResult[index]))
        }
    }

    fun handleResultEstablishment(location: PlaceLocation) {
        mapSelectedChips(emptyList())
        fetchEstablishments(location)
    }

    fun selectedEstablishmentPage(index: Int) {
        mapEstablishmentVO(establishmentResult, index)
    }

    private fun mapSelectedChips(selectedChips: List<String>) {
        viewModelScope.launch(uiKitCoroutineDispatcher.getIODispatcher()) {
            mChips.value = ListLoading()
            selectedTypes.clear()
            selectedTypes.addAll(selectedChips)
            mChips.value =
                ListSuccess(chipsResults.map {
                    val isSelected = selectedChips.contains(it)
                    ChipItem(it, isSelected)
                })
        }
    }

    companion object {
        val DEFAULT_COORDINATES: PlaceLocation = PlaceLocation(-8.11599, -79.02998)
    }
}
