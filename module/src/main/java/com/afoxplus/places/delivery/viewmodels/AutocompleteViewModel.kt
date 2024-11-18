package com.afoxplus.places.delivery.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afoxplus.places.cross.mapper.toUIEstablishment
import com.afoxplus.places.domain.entities.Establishment
import com.afoxplus.uikit.objects.vendor.Establishment as UIEstablishment
import com.afoxplus.places.domain.usecases.actions.FetchEstablishmentByQuery
import com.afoxplus.uikit.views.status.ListEmptyData
import com.afoxplus.uikit.views.status.ListError
import com.afoxplus.uikit.views.status.ListLoading
import com.afoxplus.uikit.views.status.ListState
import com.afoxplus.uikit.views.status.ListSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AutocompleteViewModel @Inject constructor(
    private val fetchEstablishmentByQuery: FetchEstablishmentByQuery
) : ViewModel() {

    private val mResults = mutableListOf<Establishment>()

    private val mQueryAutocomplete = MutableStateFlow("")

    fun onSearchTextChanged(value: String) {
        mQueryAutocomplete.value = value
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val predictionsResult = mQueryAutocomplete
        .onStart { delay(SEARCH_DELAY_MS) }
        .mapLatest { query -> executeSearch(query) }
        .catch { emit(ListError(Exception("Autocomplete service error"))) }
        .stateIn(
            scope = viewModelScope,
            initialValue = ListLoading(),
            started = SharingStarted.Eagerly
        )

    private suspend fun executeSearch(query: String): ListState<UIEstablishment> {
        if (query.length >= MIN_QUERY_LENGTH) {
            val results = fetchEstablishmentByQuery.invoke(query)
            mResults.clear()
            mResults.addAll(results)
            val state = ListSuccess(results.map { it.toUIEstablishment()})
            return state
        } else return ListEmptyData()
    }

    fun selectEstablishment(
        index: Int,
        onReturnToMap: (Establishment) -> Unit
    ) {
        onReturnToMap(mResults[index])
    }

    companion object {
        const val SEARCH_DELAY_MS = 400L
        const val MIN_QUERY_LENGTH = 3
    }
}
