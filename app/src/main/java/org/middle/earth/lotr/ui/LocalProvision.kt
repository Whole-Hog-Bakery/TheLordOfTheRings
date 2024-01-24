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

val LocalGridColumnsComposition = staticCompositionLocalOf<GridColumnCount> {
    error("No Number Of Grid columns provided")
}

val LocalListColumnsComposition = staticCompositionLocalOf<ListColumnCount> {
    error("No Number Of List columns provided")
}

val LocalDevicePostureComposition = staticCompositionLocalOf<DevicePosture> {
    error("No DevicePosture provided")
}

/**
 *
 */
@JvmInline
value class GridColumnCount(val value: Int = 0)

@JvmInline
value class ListColumnCount(val value: Int = 0)

inline fun <T : Any> LazyGridScope.items(
    items: List<T>,
    noinline key: ((item: T) -> Any)? = null,
    noinline span: (LazyGridItemSpanScope.(item: T?) -> GridItemSpan)? = null,
    noinline contentType: (item: T?) -> Any? = { null },
    crossinline itemContent: @Composable LazyGridItemScope.(item: T?) -> Unit
) = items(
    count = items.size,
    key = if (key != null) { index: Int -> items[index].let(key) } else null,
    span = if (span != null) { { span(items[it]) } } else null,
    contentType = { index: Int -> contentType(items[index]) }
) {
    itemContent(items[it])
}