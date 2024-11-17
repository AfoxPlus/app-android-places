package com.afoxplus.places.delivery.screens

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.afoxplus.places.delivery.components.SearchResultComponent
import com.afoxplus.places.delivery.viewmodels.AutocompleteViewModel
import com.afoxplus.uikit.designsystem.businesscomponents.UIKitSearchAutocomplete
import kotlinx.coroutines.android.awaitFrame

@Composable
fun AutocompleteScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onReturnToMap: (com.afoxplus.places.domain.entities.Establishment) -> Unit
) {
    AutocompleteScreen(
        modifier = modifier,
        viewModel = hiltViewModel(),
        navController = navController,
        onReturnToMap = onReturnToMap
    )
}

@Composable
internal fun AutocompleteScreen(
    modifier: Modifier = Modifier,
    viewModel: AutocompleteViewModel,
    navController: NavHostController,
    onReturnToMap: (com.afoxplus.places.domain.entities.Establishment) -> Unit
) {
    val establishmentState = viewModel.establishments.collectAsStateWithLifecycle()
    val focusRequester = remember { FocusRequester() }

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        awaitFrame()
        focusRequester.requestFocus()
    }

    ConstraintLayout(
        modifier = modifier
            .focusable()
            .fillMaxSize()
    ) {
        val (searchAutoComplete, listResults) = createRefs()

        UIKitSearchAutocomplete(
            modifier = Modifier
                .focusable()
                .constrainAs(searchAutoComplete) {
                    top.linkTo(parent.top, margin = 10.dp)
                    start.linkTo(parent.start, margin = 12.dp)
                    end.linkTo(parent.end, margin = 12.dp)
                    width = Dimension.fillToConstraints
                    height = Dimension.wrapContent
                },
            placeholder = "Buscar más aquí",
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                }
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            focusRequester = focusRequester,
            onBackClick = {
                navController.popBackStack()
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
            SearchResultComponent(establishmentState = establishmentState) { index ->
                viewModel.selectEstablishment(index, onReturnToMap)
            }
        }
    }
}