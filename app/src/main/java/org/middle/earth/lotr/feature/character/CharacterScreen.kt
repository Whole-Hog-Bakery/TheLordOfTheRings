package org.middle.earth.lotr.feature.character

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CharacterScreen(
    modifier: Modifier = Modifier,
    viewModel: CharacterViewModel = hiltViewModel()
) {

    val moleculeUiState by viewModel.uiStateFlow.collectAsState(CharactersScreenUiState())
    val uiState = (moleculeUiState as CharactersScreenUiState)
    LaunchedEffect(Unit) {
        viewModel.reduce((GetCharacters))
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        // Add a single item
        item {
            Text(text = "First item")
        }

        if (uiState.characters == null) {

        } else
            items(
                items = uiState.characters.docs,
                key = { item -> item.characterId },
                itemContent = {
                    Text(text = it.name)
                })

        // Add another single item
        item {
            Text(text = "Last item")
        }
    }


}
