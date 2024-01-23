package org.middle.earth.lotr.feature.character

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.middle.earth.lotr.data.local.CharacterDO
import org.middle.earth.lotr.mvi.MoleculeUiEvent
import org.middle.earth.lotr.mvi.MoleculeUiState
import org.middle.earth.lotr.mvi.MviViewModel
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(private val repository: CharacterRepository) : MviViewModel<MoleculeUiEvent, MoleculeUiState>() {

    private val characters = repository.characters()
        .flowOn(Dispatchers.IO)
        .cachedIn(viewModelScope)

    @Composable
    override fun state(events: Flow<MoleculeUiEvent>): MoleculeUiState {
        var state: MoleculeUiState by remember { mutableStateOf(CharactersScreenUiState(characters)) }

        LaunchedEffect(Unit) {
            events.collect { event ->
                when (event) {
                    is GetCharacters -> state = CharactersScreenUiState(characters = repository.characters())
                    else -> TODO("Unexpected event = $event")

                }
            }

        }

        return state
    }
}


@Immutable
object GetCharacters : MoleculeUiEvent


@Immutable
data class CharactersScreenUiState(
    val characters: Flow<PagingData<CharacterDO>>,
) : MoleculeUiState

