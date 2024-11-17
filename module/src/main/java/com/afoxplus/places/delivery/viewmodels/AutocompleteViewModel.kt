package com.afoxplus.places.delivery.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afoxplus.places.domain.usecases.actions.FetchEstablishmentByQuery
import com.afoxplus.uikit.di.UIKitCoroutineDispatcher
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
class AutocompleteViewModel @Inject constructor(
    private val fetchEstablishmentByQuery: FetchEstablishmentByQuery,
    private val uiKitCoroutineDispatcher: UIKitCoroutineDispatcher
) : ViewModel() {
    private val mEstablishments: MutableStateFlow<ListState<Establishment>> by lazy {
        MutableStateFlow(
            ListEmptyData()
        )
    }
    val establishments: StateFlow<ListState<Establishment>> get() = mEstablishments

    private val mResults = mutableListOf<com.afoxplus.places.domain.entities.Establishment>()

    fun searchEstablishments(query: String) =
        viewModelScope.launch(uiKitCoroutineDispatcher.getIODispatcher()) {
            try {
                mEstablishments.value = ListLoading()
                val results = fetchEstablishmentByQuery.invoke(query)
                mResults.clear()
                mResults.addAll(results)
                mEstablishments.value = ListSuccess(results.map {
                    Establishment(
                        imageLandscape = it.imageBanner,
                        imagePortrait = it.imageLogo,
                        name = it.name,
                        description = it.primaryType,
                        hasSubscription = it.hasSubscription,
                        isOpen = it.isOpen,
                        rating = it.rating,
                        addressDescription = it.address,
                        phoneDescription = it.phone
                    )
                })
            } catch (ex: Exception) {
                mEstablishments.value = ListError(ex)
            }
        }

    fun selectEstablishment(
        index: Int,
        onReturnToMap: (com.afoxplus.places.domain.entities.Establishment) -> Unit
    ) {
        onReturnToMap(mResults[index])
    }
}
