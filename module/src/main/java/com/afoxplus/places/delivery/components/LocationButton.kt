package com.afoxplus.places.delivery.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.afoxplus.uikit.designsystem.atoms.UIKitIcon
import com.afoxplus.uikit.designsystem.foundations.UIKitTheme

@Composable
fun LocationButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .size(UIKitTheme.spacing.spacing42)
            .background(UIKitTheme.colors.light01, shape = CircleShape)
            .clickable(onClick = onClick)
            .padding(UIKitTheme.spacing.spacing08),
        contentAlignment = Alignment.Center
    ) {
        UIKitIcon(
            modifier = Modifier
                .width(UIKitTheme.spacing.spacing24)
                .height(UIKitTheme.spacing.spacing24),
            icon = UIKitTheme.icons.icon_current_location
        )
    }
}