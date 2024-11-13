package com.afoxplus.places.delivery.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.afoxplus.places.delivery.components.SearchResultComponent
import com.afoxplus.places.delivery.viewmodels.AutocompleteViewModel
import com.afoxplus.uikit.designsystem.businesscomponents.UIKitSearchAutocomplete


@Composable
fun AutocompleteScreen(
    modifier: Modifier = Modifier
) {
    AutocompleteScreen(modifier = modifier, viewModel = hiltViewModel())
}

@Composable
internal fun AutocompleteScreen(modifier: Modifier = Modifier, viewModel: AutocompleteViewModel) {
    val establishmentState = viewModel.establishments.collectAsStateWithLifecycle()
    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        val (searchAutoComplete, listResults) = createRefs()

        UIKitSearchAutocomplete(
            modifier = Modifier
                .constrainAs(searchAutoComplete) {
                    top.linkTo(parent.top, margin = 10.dp)
                    start.linkTo(parent.start, margin = 12.dp)
                    end.linkTo(parent.end, margin = 12.dp)
                    width = Dimension.fillToConstraints
                    height = Dimension.wrapContent
                },
            placeholder = "Buscar más aquí",
            onBackClick = {

            }, onTextChange = {
                viewModel.searchEstablishments(it)
            })
        Box(
            modifier = Modifier
                .constrainAs(listResults) {
                    top.linkTo(searchAutoComplete.bottom, margin = 10.dp)
                    start.linkTo(parent.start, margin = 12.dp)
                    end.linkTo(parent.end, margin = 12.dp)
                    bottom.linkTo(parent.bottom, margin = 12.dp)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }) {
            SearchResultComponent(establishmentState = establishmentState)
        }
        /*LazyColumn(
            modifier = Modifier
                .constrainAs(listResults) {
                    top.linkTo(searchAutoComplete.bottom, margin = 10.dp)
                    start.linkTo(parent.start, margin = 12.dp)
                    end.linkTo(parent.end, margin = 12.dp)
                    bottom.linkTo(parent.bottom, margin = 12.dp)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .background(UIKitTheme.colors.light01)
        ) {
            items(15) { item ->
                UIKitItemAutocomplete(
                    establishment = Establishment(
                        imageLandscape = "https://static.bandainamcoent.eu/high/dragon-ball/dragon-ball-sparking-zero/00-page-setup/Page-Setup-Revamp/DBSZ_banner_mobile.jpg",
                        imagePortrait = "https://image.api.playstation.com/vulcan/ap/rnd/202405/2216/e6550a5a29624aee479b088bbefa4abc0097dc9253bca3d0.png",
                        name = "Kitchen Resto",
                        description = "Cafe & Resto",
                        hasSubscription = false,
                        isOpen = false,
                        rating = 4f,
                        addressDescription = "Av. Arenales 1241",
                        phoneDescription = "966885488"
                    )
                ) {

                }
            }
        }*/
    }
}