package org.middle.earth.lotr.mvi

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Immutable
interface MoleculeUiEvent

@Stable
interface MoleculeUiState

@Immutable
object RenderUI : MoleculeUiState

@Immutable
interface MoleculeShortcut : MoleculeUiEvent, MoleculeUiState