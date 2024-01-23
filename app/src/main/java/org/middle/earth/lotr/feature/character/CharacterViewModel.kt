package org.middle.earth.lotr.feature.character

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import org.middle.earth.lotr.data.remote.dto.character.CharacterResponse
import org.middle.earth.lotr.mvi.MoleculeUiEvent
import org.middle.earth.lotr.mvi.MoleculeUiState
import org.middle.earth.lotr.mvi.MviViewModel
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(private val repository: CharacterRepository) : MviViewModel<MoleculeUiEvent, MoleculeUiState>() {

    @Composable
    override fun state(events: Flow<MoleculeUiEvent>): MoleculeUiState {
        var state: MoleculeUiState by remember { mutableStateOf(CharactersScreenUiState()) }

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

    //
//    @SuppressLint("ComposableNaming")
//    @Composable
//    fun GetStartedReducer(repository: AccessRepository, event: Flow<MoleculeUiEvent>): MoleculeUiState {
//
//        var state: MoleculeUiState by remember { mutableStateOf(GetSet) }
//
//        LaunchedEffect(Unit) {
//            event.collect { event ->
//                state = when (event) {
//                    is OnYourMarks -> Go
//                    is Authorise -> repository.zoteroOAuth()
//                    is AccessToken -> repository.accessToken(event.uri)
//                    else -> TODO("Unexpected event = $event")
//                }
//            }
//        }
//
//        return state
//    }
}


@Immutable
object GetCharacters : MoleculeUiEvent


@Immutable
data class CharactersScreenUiState(
    val characters: CharacterResponse? = null,
) : MoleculeUiState

@Immutable
object GetSet : MoleculeUiState

@Immutable
data object Authorise : MoleculeUiEvent

@Stable
data class AccessToken(val uri: Uri? = null) : MoleculeUiEvent

@Immutable
object Go : MoleculeUiState

@Stable
data class Authorising(val url: String? = null) : MoleculeUiState

@Stable
data object SignedIn : MoleculeUiState

