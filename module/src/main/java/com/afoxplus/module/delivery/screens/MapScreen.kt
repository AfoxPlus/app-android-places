package com.afoxplus.module.delivery.screens

import android.app.Activity
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.afoxplus.module.delivery.components.ChipsComponent
import com.afoxplus.module.delivery.components.EstablishmentsComponent
import com.afoxplus.module.delivery.components.LocationButton
import com.afoxplus.module.delivery.utils.askForLocationPermission
import com.afoxplus.module.delivery.utils.getCurrentLocation
import com.afoxplus.module.delivery.utils.hasLocationPermission
import com.afoxplus.module.delivery.viewmodels.MapViewModel
import com.afoxplus.uikit.designsystem.businesscomponents.UIKitMapSearch
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
    activity: Activity
) {
    MapScreen(
        modifier = modifier,
        activity = activity,
        mapViewModel = hiltViewModel()
    )
}

@Composable
internal fun MapScreen(
    modifier: Modifier = Modifier,
    activity: Activity,
    mapViewModel: MapViewModel
) {
    val cameraPositionState = rememberCameraPositionState()
    val currentPositionState = mapViewModel.currentPosition.collectAsState()
    val chips = mapViewModel.chips.collectAsStateWithLifecycle()
    val establishmentState = mapViewModel.establishments.collectAsStateWithLifecycle()
    val markers = mapViewModel.markers.collectAsState()

    val navController = rememberNavController()

    activity.getCurrentLocation { location ->
        mapViewModel.setLocation(location)
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
                Marker(
                    state = MarkerState(
                        position = LatLng(
                            it.location.latitude,
                            it.location.latitude
                        )
                    ),
                    title = it.name,
                    snippet = it.address
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
        )

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
                modifier = Modifier.fillMaxWidth()
            ) { establishment ->
                mapViewModel.onEstablishmentClick(establishment)
            }
        }

        LocationButton(
            onClick = {
                if (activity.hasLocationPermission()) {
                    currentPositionState.value?.let {
                        val userLocation = LatLng(it.latitude, it.longitude)
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

