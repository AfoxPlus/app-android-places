package com.afoxplus.places.delivery.components

import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.afoxplus.uikit.designsystem.businesscomponents.UIKitItemAutocomplete
import com.afoxplus.uikit.designsystem.foundations.UIKitTheme
import com.afoxplus.uikit.objects.vendor.Establishment
import com.afoxplus.uikit.views.status.ListEmptyData
import com.afoxplus.uikit.views.status.ListError
import com.afoxplus.uikit.views.status.ListLoading
import com.afoxplus.uikit.views.status.ListState
import com.afoxplus.uikit.views.status.ListSuccess

@Composable
fun SearchResultComponent(
    modifier: Modifier = Modifier,
    establishmentState: State<ListState<Establishment>> = mutableStateOf(ListEmptyData()),
    onEstablishmentClick: (Establishment) -> Unit = {}
) {
    when (establishmentState.value) {
        is ListSuccess<Establishment> -> {
            val establishments = (establishmentState.value as ListSuccess<Establishment>).data
            LazyColumn(
                modifier = modifier
                    .background(UIKitTheme.colors.light01)
            ) {
                items(establishments.size) { item ->
                    UIKitItemAutocomplete(
                        establishment = establishments[item]
                    ) {
                        onEstablishmentClick(establishments[item])
                    }
                }
            }
        }

        is ListLoading<Establishment> -> {

        }

        is ListError<Establishment> -> {

        }
    }
}