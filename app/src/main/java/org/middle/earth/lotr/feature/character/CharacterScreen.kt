package org.middle.earth.lotr.feature.character

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.emptyFlow
import org.middle.earth.lotr.data.local.CharacterDO
import org.middle.earth.lotr.ui.LocalWindowSizeClassComposition
import org.middle.earth.lotr.ui.ORIENTATION_LANDSCAPE
import org.middle.earth.lotr.ui.ORIENTATION_PORTRAIT
import org.middle.earth.lotr.ui.ScreenDimension

@Composable
fun CharacterScreen(
    modifier: Modifier = Modifier,
    viewModel: CharacterViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val screenDimension = LocalWindowSizeClassComposition.current


    val moleculeUiState by viewModel.uiStateFlow.collectAsState(CharactersScreenUiState(emptyFlow()))
    val uiState = (moleculeUiState as CharactersScreenUiState)

    val characters: LazyPagingItems<CharacterDO> = uiState.characters.collectAsLazyPagingItems()


    LaunchedEffect(Unit) {
        viewModel.reduce((GetCharacters))
    }


    LazyVerticalGrid(
        columns = GridCells.Fixed(calculateNumberOfColumns(screenDimension)),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        items(
            count = characters.itemCount,
            key = characters.itemKey { it.characterId },
            contentType = characters.itemContentType()
        ) {

            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                elevation = CardDefaults.elevatedCardElevation(24.dp),
                shape = RoundedCornerShape(1.dp),
                colors = CardDefaults.cardColors(
                    contentColor = MaterialTheme.colorScheme.primary,
                    containerColor = MaterialTheme.colorScheme.background
                )
            ) {
                Spacer(modifier = Modifier.height(5.dp))
                Row(modifier = Modifier.padding(start = 8.dp, end = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.height(5.dp))
                    characters[it]?.let {

                        val intent = remember { Intent(Intent.ACTION_VIEW, Uri.parse(it.wikiUrl)) }

                        Text(modifier = Modifier.weight(1f), text = it.name)
                        IconButton(onClick = { context.startActivity(intent) }) {
                            Icon(
                                Icons.Outlined.Language,
                                contentDescription = "wiki"
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                }
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}

fun calculateNumberOfColumns(screenDimension: ScreenDimension): Int = when {
    ORIENTATION_PORTRAIT.contains(screenDimension) -> 1
    ORIENTATION_LANDSCAPE.contains(screenDimension) -> 2
    else -> TODO("Unexpected screenDimension = $screenDimension")
}
