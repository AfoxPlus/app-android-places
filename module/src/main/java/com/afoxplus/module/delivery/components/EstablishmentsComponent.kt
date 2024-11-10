package com.afoxplus.module.delivery.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.afoxplus.uikit.designsystem.businesscomponents.UIKitEstablishmentMap
import com.afoxplus.uikit.objects.vendor.Establishment
import com.afoxplus.uikit.views.status.ListEmptyData
import com.afoxplus.uikit.views.status.ListError
import com.afoxplus.uikit.views.status.ListLoading
import com.afoxplus.uikit.views.status.ListState
import com.afoxplus.uikit.views.status.ListSuccess

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EstablishmentsComponent(
    modifier: Modifier = Modifier,
    establishmentState: State<ListState<Establishment>> = mutableStateOf(ListEmptyData()),
    onEstablishmentClick: (Establishment) -> Unit = {}
) {
    when (establishmentState.value) {
        is ListSuccess<Establishment> -> {
            val establishments = (establishmentState.value as ListSuccess<Establishment>).data
            val pagerState = rememberPagerState(pageCount = { establishments.size })
            HorizontalPager(
                state = pagerState,
                modifier = modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                pageSpacing = 6.dp
            ) { page ->
                val establishment = establishments[page]
                Box(
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .width(334.dp)
                ) {
                    UIKitEstablishmentMap(
                        modifier = Modifier
                            .wrapContentSize(),
                        establishment = establishment
                    ) {
                        onEstablishmentClick(establishment)
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