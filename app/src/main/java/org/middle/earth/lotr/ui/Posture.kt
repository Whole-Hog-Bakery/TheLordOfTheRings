
@file:OptIn(ExperimentalContracts::class)

package org.middle.earth.lotr.ui

import android.graphics.Rect
import androidx.window.layout.FoldingFeature
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Information about the posture of the device
 */
sealed interface DevicePosture {
    data object NormalPosture : DevicePosture
    data class TableTopPosture(val hingePosition: Rect) : DevicePosture
    data class BookPosture(val hingePosition: Rect) : DevicePosture
    data class SeparatingPosture(val hingePosition: Rect, var orientation: FoldingFeature.Orientation) : DevicePosture
}

fun isTableTopPosture(foldFeature: FoldingFeature?): Boolean {
    contract { returns(true) implies (foldFeature != null) }
    return foldFeature?.state == FoldingFeature.State.HALF_OPENED && foldFeature.orientation == FoldingFeature.Orientation.HORIZONTAL
}

fun isBookPosture(foldFeature: FoldingFeature?): Boolean {
    contract { returns(true) implies (foldFeature != null) }
    return foldFeature?.state == FoldingFeature.State.HALF_OPENED && foldFeature.orientation == FoldingFeature.Orientation.VERTICAL
}

fun isSeparatingPosture(foldFeature: FoldingFeature?): Boolean {
    contract { returns(true) implies (foldFeature != null) }
    return foldFeature?.state == FoldingFeature.State.FLAT && foldFeature.isSeparating
}

/**
 * Different type of navigation supported by app depending on size and state.
 */
enum class NavigationType {
    BOTTOM_NAVIGATION, NAVIGATION_RAIL, PERMANENT_NAVIGATION_DRAWER
}

/**
 * Content shown depending on size and state of device.
 */
enum class ContentType {
    LIST_ONLY, LIST_AND_DETAIL
}