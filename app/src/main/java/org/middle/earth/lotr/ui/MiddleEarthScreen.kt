package org.middle.earth.lotr.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.middle.earth.lotr.R
import org.middle.earth.lotr.feature.character.CharacterScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiddleEarthScreen(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    val screenDimension = LocalWindowSizeClassComposition.current

    Scaffold(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        topBar = {
            Surface(shadowElevation = 3.dp) {
                MiddleEarthTopBar(onClick)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            CharacterScreen()
        }
    }

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MiddleEarthTopBar(onClick: () -> Unit) {
    TopAppBar(
        modifier = Modifier
            .wrapContentHeight(),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(id = R.string.the_lord_of_the_rings),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = stringResource(id = R.string.characters),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = { onClick() }) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Up"
                )
            }
        }
    )
}
