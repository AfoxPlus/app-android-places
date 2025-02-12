package com.afoxplus.places.delivery.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

const val LOCATION_PERMISSION_REQUEST_CODE = 1001

internal fun Activity.hasLocationPermission(): Boolean {
    return when {
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED -> true

        else -> {
            this.askForLocationPermission()
            false
        }
    }
}

internal fun Activity.askForLocationPermission() {
    ActivityCompat.shouldShowRequestPermissionRationale(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
}

internal fun Activity.getCurrentLocation(onLocationReceived: (Location?) -> Unit) {
    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(this)

    // Check if permission is granted
    if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // Request permissions if not granted
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
        onLocationReceived(null) // Return null if permission isn't granted
        return
    }

    // Get the last known location
    fusedLocationClient.lastLocation
        .addOnSuccessListener { location: Location? ->
            if (location != null) {
                // Location is received, pass it to the callback
                onLocationReceived(location)
            } else {
                // No location was retrieved
                onLocationReceived(null)
            }
        }
        .addOnFailureListener { e ->
            Log.e("LocationError", "Failed to get location: ${e.message}")
            onLocationReceived(null)
        }
}