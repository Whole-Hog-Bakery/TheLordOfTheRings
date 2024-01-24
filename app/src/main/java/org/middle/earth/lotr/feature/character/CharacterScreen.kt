package org.middle.earth.lotr.feature.character

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
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
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.emptyFlow
import org.middle.earth.lotr.data.local.CharacterDO

@Composable
fun CharacterScreen(
    modifier: Modifier = Modifier,
    viewModel: CharacterViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val moleculeUiState by viewModel.uiStateFlow.collectAsState(CharactersScreenUiState(emptyFlow()))
    val uiState = (moleculeUiState as CharactersScreenUiState)

    val characters: LazyPagingItems<CharacterDO> = uiState.characters.collectAsLazyPagingItems()
    LaunchedEffect(Unit) {
        viewModel.reduce((GetCharacters))
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 4.dp, end = 4.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp)
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
