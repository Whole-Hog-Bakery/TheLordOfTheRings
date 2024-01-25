package org.middle.earth.lotr.ui

import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

val LocalNetworkConnectedComposition = staticCompositionLocalOf<Boolean> {
    error("No isNetworkConnected provided")
}

val LocalWindowSizeClassComposition = staticCompositionLocalOf<ScreenDimension> {
    error("No WindowSizeClass provided")
}