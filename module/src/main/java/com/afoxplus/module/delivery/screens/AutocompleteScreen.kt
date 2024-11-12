package com.afoxplus.module.delivery.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.afoxplus.uikit.designsystem.businesscomponents.UIKitItemAutocomplete
import com.afoxplus.uikit.designsystem.businesscomponents.UIKitSearchAutocomplete
import com.afoxplus.uikit.designsystem.foundations.UIKitTheme
import com.afoxplus.uikit.objects.vendor.Establishment

@Composable
fun AutocompleteScreen(modifier: Modifier = Modifier) {
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
                println("Here text's written is $it")
            })
        LazyColumn(
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
        }
    }
}