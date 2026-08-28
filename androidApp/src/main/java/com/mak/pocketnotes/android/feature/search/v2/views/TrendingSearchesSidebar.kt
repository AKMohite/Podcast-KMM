package com.mak.pocketnotes.android.feature.search.v2.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mak.pocketnotes.android.R

@Composable
internal fun TrendingSearchesSidebar(trending: List<String>) {
  Column(
    modifier = Modifier
        .width(250.dp)
        .fillMaxHeight()
        .padding(16.dp)
  ) {
    Text(
      text = stringResource(R.string.trending_searches_header),
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(16.dp))
    trending.forEach { term ->
      Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO */ }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          Icons.Default.TrendingUp,
          contentDescription = null,
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = term, style = MaterialTheme.typography.bodyLarge)
      }
    }
  }
}
