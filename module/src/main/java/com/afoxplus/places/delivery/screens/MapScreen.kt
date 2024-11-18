package com.afoxplus.places.delivery.screens

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.afoxplus.places.delivery.components.ChipsComponent
import com.afoxplus.places.delivery.components.EstablishmentsComponent
import com.afoxplus.places.delivery.components.LocationButton
import com.afoxplus.places.delivery.utils.askForLocationPermission
import com.afoxplus.places.delivery.utils.getCurrentLocation
import com.afoxplus.places.delivery.utils.hasLocationPermission
import com.afoxplus.places.delivery.viewmodels.MapViewModel
import com.afoxplus.places.domain.entities.Location
import com.afoxplus.uikit.designsystem.businesscomponents.UIKitMapSearch
import com.afoxplus.uikit.designsystem.extensions.getBitmapFromVectorDrawable
import com.afoxplus.uikit.designsystem.foundations.UIKitTheme
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    activity: Activity,
    navController: NavHostController,
    onNavigateToAutocomplete: () -> Unit,
) {
    MapScreen(
        modifier = modifier,
        activity = activity,
        mapViewModel = hiltViewModel(),
        navController = navController,
        onNavigateToAutocomplete = onNavigateToAutocomplete
    )
}

@Composable
internal fun MapScreen(
    modifier: Modifier = Modifier,
    activity: Activity,
    mapViewModel: MapViewModel,
    navController: NavHostController,
    onNavigateToAutocomplete: () -> Unit,
) {
    val cameraPositionState = rememberCameraPositionState()
    val chips = mapViewModel.chips.collectAsStateWithLifecycle()
    val establishmentState = mapViewModel.establishments.collectAsStateWithLifecycle()
    val markers = mapViewModel.establishmentsVOs.collectAsStateWithLifecycle()
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val establishmentResult =
        savedStateHandle?.getStateFlow<Location?>("location_result", null)
            ?.collectAsStateWithLifecycle()

    val currentLocationState = mapViewModel.lastKnownLocation.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        activity.getCurrentLocation { location ->
            mapViewModel.setMapCurrentLocation(location)
            location?.let {
                cameraPositionState.position =
                    CameraPosition.fromLatLngZoom(
                        LatLng(location.latitude, location.longitude),
                        15f
                    )
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        establishmentResult?.value?.let {
            mapViewModel.handleResultEstablishment(it)
        }
    }

    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        val (map, searchBar, chipsRow, establishmentsCard, locationButton) = createRefs()
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(map) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                compassEnabled = false
            )
        ) {
            markers.value.forEach {
                val icon = if (it.isSelected) {
                    UIKitTheme.icons.icon_location_filled
                } else {
                    UIKitTheme.icons.icon_location_filled_red
                }
                val customPinBitmap =
                    icon.getBitmapFromVectorDrawable(activity.baseContext)
                Marker(
                    state = MarkerState(
                        position = LatLng(
                            it.establishment.location.latitude,
                            it.establishment.location.longitude
                        )
                    ),
                    title = it.establishment.name,
                    snippet = it.establishment.address,
                    icon = BitmapDescriptorFactory.fromBitmap(customPinBitmap)
                )
            }
        }

        UIKitMapSearch(
            modifier = Modifier
                .constrainAs(searchBar) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                    width = Dimension.fillToConstraints
                },
            placeholderText = "Buscar más aquí"
        ) {
            onNavigateToAutocomplete()
        }

        ChipsComponent(
            modifier = Modifier.constrainAs(chipsRow) {
                top.linkTo(searchBar.bottom, margin = 6.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            },
            chipState = chips
        ) { selectedChips ->
            mapViewModel.selectedChips(selectedChips)
        }

        Box(modifier = Modifier.constrainAs(establishmentsCard) {
            bottom.linkTo(parent.bottom, margin = 16.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            width = Dimension.fillToConstraints
        }) {
            EstablishmentsComponent(
                establishmentState = establishmentState,
                modifier = Modifier.fillMaxWidth(),
                onPageSelectedListener = {
                    mapViewModel.selectedEstablishmentPage(index = it)
                }
            ) { index ->
                mapViewModel.onEstablishmentClick(index)
            }
        }

        LocationButton(
            onClick = {
                if (activity.hasLocationPermission()) {
                    currentLocationState.value?.let { currentLocation ->
                        val userLocation = LatLng(
                            currentLocation.latitude,
                            currentLocation.longitude
                        )
                        mapViewModel.setMapCurrentLocation(currentLocation)
                        cameraPositionState.position =
                            CameraPosition.fromLatLngZoom(userLocation, 15f)
                    }
                } else {
                    activity.askForLocationPermission()
                }
            },
            modifier = Modifier
                .constrainAs(locationButton) {
                    bottom.linkTo(establishmentsCard.top, margin = 8.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }
        )
    }
}
