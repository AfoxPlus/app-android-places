package com.afoxplus.places.demo.viewmodels

import androidx.lifecycle.ViewModel
import com.afoxplus.uikit.bus.UIKitEventBus
import com.afoxplus.uikit.bus.UIKitEventBusWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val eventBusWrapper: UIKitEventBusWrapper) :
    ViewModel() {
    val onEventBusListener: SharedFlow<UIKitEventBus> get() = eventBusWrapper.listen()
}