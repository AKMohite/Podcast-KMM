package com.mak.pocketnotes.android.feature.search.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.domain.models.Genre

@Composable
internal fun GenreCells(
    genres: List<Genre>,
    onGenreClick: (Genre) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
//                items(
//                    items = state.genres,
//                    key = { _, genre -> genre.id }
//                ) {}
        items(
            items = genres,
            key = { genre -> genre.id }
        ) { genre ->
            GenreCell(
                modifier = Modifier.fillMaxWidth(),
                genre = genre,
                onGenreClick = onGenreClick
            )
        }
    }
}

@Composable
private fun GenreCell(
    genre: Genre,
    modifier: Modifier = Modifier,
    onGenreClick: (Genre) -> Unit
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
//            .border(width = 2.dp, color = MaterialTheme.colorScheme.primaryContainer)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable { onGenreClick(genre) }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = genre.name,
            maxLines = 1,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Preview
@Composable
private fun GenreCellsPreview() {
    PocketNotesTheme {
        Surface {
            GenreCells(
                genres = listOf(
                    Genre(1, "genre 1", 3),
                    Genre(2, "genre 2", 2),
                    Genre(3, "genre 3", 2),
                    Genre(4, "genre 4", 3),
                    Genre(5, "genre 5", 4),
                    Genre(6, "genre 6", 1),
                ),
                onGenreClick = {},
            )
        }
    }
}