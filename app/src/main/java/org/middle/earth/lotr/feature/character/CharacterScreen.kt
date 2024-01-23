package org.middle.earth.lotr.feature.character

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.emptyFlow
import org.middle.earth.lotr.data.local.CharacterDO

@Composable
fun CharacterScreen(
    modifier: Modifier = Modifier,
    viewModel: CharacterViewModel = hiltViewModel()
) {

    val moleculeUiState by viewModel.uiStateFlow.collectAsState(CharactersScreenUiState(emptyFlow()))
    val uiState = (moleculeUiState as CharactersScreenUiState)

    val characters: LazyPagingItems<CharacterDO> = uiState.characters.collectAsLazyPagingItems()
    LaunchedEffect(Unit) {
        viewModel.reduce((GetCharacters))
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {

        items(
            count = characters.itemCount,
            key = characters.itemKey { it.characterId },
            contentType = characters.itemContentType()
        ) {
            characters[it]?.let {
                Text(text = it?.name ?: "Not Set")
            }


        }
    }


}
