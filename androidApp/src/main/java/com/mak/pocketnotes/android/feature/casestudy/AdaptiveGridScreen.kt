package com.mak.pocketnotes.android.feature.casestudy

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.android.common.navigation.AdaptiveScreenType

@Composable
internal fun AdaptiveGridScreen(
    adaptiveScreenType: AdaptiveScreenType,
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
) {
    val columns: GridCells = rememberColumns(adaptiveScreenType)
    LazyVerticalGrid(
        columns = columns,
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        verticalArrangement = verticalArrangement,
        horizontalArrangement = horizontalArrangement,
        flingBehavior = flingBehavior
    ) {
        items(sweets, contentType = { "sweets" }, key = { it.id }) {
            SweetsCard(sweets = it, onClick = {})
        }
    }
}

@Composable
private fun SweetsCard(
    sweets: AdaptiveItem,
    modifier: Modifier = Modifier,
    onClick: (AdaptiveItem) -> Unit = {}
) {
    var isFocused by remember {
        mutableStateOf(false)
    }
    val outlineColor = if (isFocused) {
        MaterialTheme.colorScheme.outline
    } else {
        MaterialTheme.colorScheme.background
    }

    Card(
        modifier = modifier
            .aspectRatio(1.0f)
            .onFocusChanged {
                isFocused = it.isFocused
            }
            .border(width = 2.dp, color = outlineColor),
        onClick = { onClick(sweets) }
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = sweets.imageUrl,
            contentDescription = stringResource(id = R.string.thumbnail_content_description),
//            placeholder = painterResource(id = R.drawable.placeholder_sweets),
            contentScale = ContentScale.Crop
        )
    }
}


private class AdaptiveItem(
    val id: Int,
    val imageUrl: String,
    @StringRes val description: Int,
)
private val sweets = listOf(
    AdaptiveItem(
        id = 0,
        imageUrl = "https://source.unsplash.com/V4MBq8kue3U",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 1,
        imageUrl = "https://source.unsplash.com/cSzyY2UaFSI",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 2,
        imageUrl = "https://source.unsplash.com/mGP8gyGb8zY",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 3,
        imageUrl = "https://source.unsplash.com/PL5FZkW0Qkk",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 4,
        imageUrl = "https://source.unsplash.com/xLvIcAYuuMQ",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 5,
        imageUrl = "https://source.unsplash.com/PMOoaWCqX_Q",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 6,
        imageUrl = "https://source.unsplash.com/yCOzRIbL08E",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 7,
        imageUrl = "https://source.unsplash.com/ZYKCgsRz9Mg",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 8,
        imageUrl = "https://source.unsplash.com/Fq54FqucgCE",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 9,
        imageUrl = "https://source.unsplash.com/WqCRDVs7ZI8",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 10,
        imageUrl = "https://source.unsplash.com/gP1YecpRyD8",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 11,
        imageUrl = "https://source.unsplash.com/hLOLcUwR0Y4",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 12,
        imageUrl = "https://source.unsplash.com/mtut50xOeC4",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 13,
        imageUrl = "https://source.unsplash.com/qZ5lPCPvdXE",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 14,
        imageUrl = "https://source.unsplash.com/uG3Vu5TXKxE",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 15,
        imageUrl = "https://source.unsplash.com/90HdOlGbjck",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 16,
        imageUrl = "https://source.unsplash.com/BhK9JdaBTvk",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 17,
        imageUrl = "https://source.unsplash.com/w0_w3N_hG00",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 18,
        imageUrl = "https://source.unsplash.com/AguGBqWbmME",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 19,
        imageUrl = "https://source.unsplash.com/yE_jI4KApfc",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 20,
        imageUrl = "https://source.unsplash.com/p6OLZPnq810",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 21,
        imageUrl = "https://source.unsplash.com/AHF_ZktTL6Q",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 22,
        imageUrl = "https://source.unsplash.com//LU_fCezP9-o",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 23,
        imageUrl = "https://source.unsplash.com/_C5zsV_p-YI",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 24,
        imageUrl = "https://source.unsplash.com/aXq1oCCjlVM",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 25,
        imageUrl = "https://source.unsplash.com/cRwZACu3kQI",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 26,
        imageUrl = "https://source.unsplash.com/8XkNFQG_cgk",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 27,
        imageUrl = "https://source.unsplash.com/FDYbS43jUrU",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 28,
        imageUrl = "https://source.unsplash.com/-ayOfwsd9mY",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 29,
        imageUrl = "https://source.unsplash.com/dcPNZeSY3yk",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 30,
        imageUrl = "https://source.unsplash.com/tWe8ib-cnXY",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 31,
        imageUrl = "https://source.unsplash.com/r-hQw_obFd0",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 32,
        imageUrl = "https://source.unsplash.com/EwaJbJvS9io",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 33,
        imageUrl = "https://source.unsplash.com/LjzAqkZnGFM",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 34,
        imageUrl = "https://source.unsplash.com/PqYvDBwpXpU",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 35,
        imageUrl = "https://source.unsplash.com/89h9zKa0L0g",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 36,
        imageUrl = "https://source.unsplash.com/OAC2cpzNCxs",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 37,
        imageUrl = "https://source.unsplash.com/PGQxoFvt14o",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 38,
        imageUrl = "https://source.unsplash.com/cLpdEA23Z44",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 39,
        imageUrl = "https://source.unsplash.com/ewOrvEa87j4",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 40,
        imageUrl = "https://source.unsplash.com/uy9DJw9e_vs",
        description = R.string.lorem_ipsum
    ),
    AdaptiveItem(
        id = 41,
        imageUrl = "https://source.unsplash.com/dIs-MqalSSE",
        description = R.string.lorem_ipsum
    )
)


@Composable
private fun rememberColumns(adaptiveScreenType: AdaptiveScreenType) = remember(adaptiveScreenType) {
    when (adaptiveScreenType) {
        AdaptiveScreenType.Compact -> GridCells.Fixed(1)
        AdaptiveScreenType.Medium -> GridCells.Fixed(2)
        else -> GridCells.Adaptive(240.dp)
    }
}