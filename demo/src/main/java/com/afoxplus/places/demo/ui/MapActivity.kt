package com.afoxplus.places.demo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.afoxplus.places.delivery.graphs.AppNavGraph
import com.afoxplus.uikit.designsystem.foundations.UIKitTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UIKitTheme {
                Scaffold { paddingValues ->
                    Column(
                        modifier = Modifier
                            .padding(paddingValues = paddingValues)
                            .fillMaxSize()
                    ) {
                        AppNavGraph(activity = this@MapActivity, rememberNavController())
                    }
                }
            }
        }
    }
}