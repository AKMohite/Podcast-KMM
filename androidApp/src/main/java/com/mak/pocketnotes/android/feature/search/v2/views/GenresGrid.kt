package com.mak.pocketnotes.android.feature.search.v2.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mak.pocketnotes.android.ui.theme.ThemePreviews
import com.mak.pocketnotes.core.feature.domain.search.models.Genre

@Composable
internal fun GenresGrid(genres: List<Genre>) {
  // We use a Box and fixed height to avoid nested scrolling issues in LazyColumn if not careful,
  // or just use items inside LazyColumn with a grid approach.
  // For simplicity here, we'll just use a non-scrolling grid or fixed number of items.
  val columns = 2
  val rows = (genres.size + columns - 1) / columns

  Column(modifier = Modifier.padding(horizontal = 16.dp)) {
    for (i in 0 until rows) {
      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        for (j in 0 until columns) {
          val index = i * columns + j
          if (index < genres.size) {
            GenreCard(genres[index], modifier = Modifier.weight(1f))
          } else {
            Spacer(modifier = Modifier.weight(1f))
          }
        }
      }
      Spacer(modifier = Modifier.height(8.dp))
    }
  }
}

@Composable
private fun GenreCard(genre: Genre, modifier: Modifier = Modifier) {
  val colors = listOf(Color(0xFF1E634E), Color(0xFFF17D50), Color(0xFF1C70A8), Color(0xFFC26A6A))
  val color = colors[genre.id % colors.size]

  Box(
    modifier = modifier
      .height(100.dp)
      .clip(RoundedCornerShape(8.dp))
      .background(color)
      .clickable { /* TODO */ }
      .padding(12.dp)
  ) {
    Text(
      text = genre.name,
      style = MaterialTheme.typography.titleMedium,
      color = Color.White,
      fontWeight = FontWeight.Bold
    )
  }
}

@ThemePreviews
@Composable
private fun GenresGridPreview() {
  GenresGrid(
    genres = listOf(
      Genre(1, "True Crime", 0),
      Genre(2, "Comedy", 0),
      Genre(3, "Technology", 0),
      Genre(4, "News", 0),
      Genre(5, "Arts", 0),
      Genre(6, "Business", 0)
    )
  )
}
