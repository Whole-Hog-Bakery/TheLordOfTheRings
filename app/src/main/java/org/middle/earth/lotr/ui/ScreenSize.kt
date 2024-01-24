package org.middle.earth.lotr.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

enum class WindowSize { COMPACT, MEDIUM, EXPANDED }

val ORIENTATION_PORTRAIT = listOf(
    ScreenDimension(width = ScreenWidth(value = WindowSize.COMPACT), ScreenHeight(value = WindowSize.MEDIUM)),
    ScreenDimension(width = ScreenWidth(value = WindowSize.MEDIUM), ScreenHeight(value = WindowSize.EXPANDED)),
)

val ORIENTATION_LANDSCAPE = listOf(
    ScreenDimension(width = ScreenWidth(value = WindowSize.MEDIUM), ScreenHeight(value = WindowSize.COMPACT)),
    ScreenDimension(width = ScreenWidth(value = WindowSize.EXPANDED), ScreenHeight(value = WindowSize.COMPACT)),
    ScreenDimension(width = ScreenWidth(value = WindowSize.EXPANDED), ScreenHeight(value = WindowSize.MEDIUM)),
    ScreenDimension(width = ScreenWidth(value = WindowSize.EXPANDED), ScreenHeight(value = WindowSize.EXPANDED)),
)

@Composable
fun computeWindowSizeClass(): ScreenDimension {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val widthWindowSize = when {
        screenWidth < 600.dp -> WindowSize.COMPACT
        screenWidth < 840.dp -> WindowSize.MEDIUM
        else -> WindowSize.EXPANDED
    }

    val heightWindowSize = when {
        screenHeight < 480.dp -> WindowSize.COMPACT
        screenHeight < 900.dp -> WindowSize.MEDIUM
        else -> WindowSize.EXPANDED
    }

    return ScreenDimension(ScreenWidth(value = widthWindowSize), ScreenHeight(value = heightWindowSize))
}

/**
 *
 */
@JvmInline
value class ScreenWidth(val value: WindowSize? = null)

@JvmInline
value class ScreenHeight(val value: WindowSize? = null)
data class ScreenDimension(
    val width: ScreenWidth,
    val height: ScreenHeight
) {
    override fun toString(): String = "(width = $width, height = $height)"
}
