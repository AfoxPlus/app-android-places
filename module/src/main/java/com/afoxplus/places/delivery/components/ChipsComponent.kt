package com.afoxplus.places.delivery.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.afoxplus.uikit.designsystem.businesscomponents.UIKitChipCollection
import com.afoxplus.uikit.designsystem.businesscomponents.UIKitChipCollectionShimmer
import com.afoxplus.uikit.objects.vendor.ChipItem
import com.afoxplus.uikit.views.status.ListEmptyData
import com.afoxplus.uikit.views.status.ListError
import com.afoxplus.uikit.views.status.ListLoading
import com.afoxplus.uikit.views.status.ListState
import com.afoxplus.uikit.views.status.ListSuccess

@Composable
fun ChipsComponent(
    modifier: Modifier = Modifier,
    chipState: State<ListState<ChipItem>> = mutableStateOf(ListEmptyData()),
    onChipClick: (List<ChipItem>) -> Unit = {}
) {
    when (chipState.value) {
        is ListSuccess<ChipItem> -> {
            UIKitChipCollection(
                modifier = modifier
                    .fillMaxWidth(),
                chipItems = (chipState.value as ListSuccess<ChipItem>).data
            ) { chipItems ->
                onChipClick(chipItems)
            }
        }

        is ListLoading<ChipItem> -> {
            UIKitChipCollectionShimmer(modifier)
        }

        is ListError<ChipItem> -> {

        }
    }
}
