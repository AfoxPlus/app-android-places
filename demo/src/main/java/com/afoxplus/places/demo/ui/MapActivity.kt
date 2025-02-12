package com.afoxplus.places.demo.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.afoxplus.places.delivery.events.OnClickEstablishmentEvent
import com.afoxplus.places.delivery.graphs.AppNavGraph
import com.afoxplus.places.demo.viewmodels.MainViewModel
import com.afoxplus.uikit.designsystem.foundations.UIKitTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MapActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

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
        observeEvents()
    }

    private fun observeEvents() {
        lifecycleScope.launchWhenCreated {
            viewModel.onEventBusListener.collectLatest { events ->
                when (events) {
                    is OnClickEstablishmentEvent -> {
                        Toast.makeText(
                            this@MapActivity,
                            "${events.establishment.name} is Clicked",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}