package com.mak.pocketnotes.android.feature.casstudy

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme

/**[Reference](https://stackoverflow.com/a/72860869)
 */
// Just for visualization purposes
@Composable
fun GridItem(label: String) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(200.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label)
    }
}

@Composable
fun BentoSample(
    modifier: Modifier = Modifier,
    gridColumnCount: Int = 5,
    isTablet: Boolean = false,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(gridColumnCount),
        modifier = modifier.fillMaxSize()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        // based on index
        items(40, span = { index ->
            val spanCount = getGridSpanSize(index, isTablet, gridColumnCount)
            GridItemSpan(spanCount)
        }) { index ->
            GridItem("Item #$index")
        }

        /*// based  on list content
        items(listOf("Foo", "Bar", "Baz"), span = { item ->
            val spanCount = if (item == "Foo") 3 else 1
            GridItemSpan(spanCount)
        }) { item ->
            GridItem(item)
        }

        // based on either content or index
        itemsIndexed(listOf("Foo", "Bar", "Baz"), span = { index, item ->
            val spanCount = if (item == "Foo" || index == 1) 3 else 1
            GridItemSpan(spanCount)
        }) { index, item ->
            GridItem(item)
        }

        // Bonus: The span lambda receives additional information as "this" context, which allows for further customization
        items(10 , span = {
            // occupy the available remaining width in the current row, but at most 2 cells wide
            GridItemSpan(this.maxCurrentLineSpan.coerceAtMost(2))
        }) { index ->
            GridItem("Item #$index")
        }*/
    }
}

@Preview
@Composable
private fun BentoGridSamplePreview() {
    PocketNotesTheme {
        Surface {
            BentoSample(
                gridColumnCount = 5,
                isTablet = false
            )
        }
    }
}

@Preview(device = Devices.FOLDABLE)
@Composable
private fun BentoGridSamplePreviewTab() {
    PocketNotesTheme {
        Surface {
            BentoSample(
                gridColumnCount = 12,
                isTablet = true
            )
        }
    }
}

/**
 * Trying to create bento box with grids expanding widths depending on [gridColumnCount]
 * @param[position] index of item in list
 * @param[isTablet] is device a tablet
 * @param[gridColumnCount] number of columns in grid
 */
private fun getGridSpanSize(
    position: Int,
    isTablet: Boolean = false,
    gridColumnCount: Int = 12
): Int {
    return if (isTablet) {
//            TODO maybe for tablet it can be staggered grid like this: https://stackoverflow.com/a/65511718
        /**
         * here 12 is [gridColumnCount] for tablet
         * With span size for tablet it will be as below:
         * |         7         | |    5     |
         * |   3  | |  3  | |       6       |
         * |    5     | |         7         |
         * |       6       | |   3  | |  3  |
         */
//            TODO maybe some calculations to get the span size
//            here 10 is number after which sequence will be repeated
//            doing this easy way ;P
        val result = position % 10
        when(result) {
            0, 6 -> 7
            1, 5 -> 5
            2, 3, 8, 9 -> 3
            else -> 6 // position = 4, 7
        }
        /*when(result) {
            0 ,1 , 4, 5, 6, 7 -> 2
            else -> 1 // position = 2, 3, 8, 9
        }*/
    } else {
        /**
         * here 5 is [gridColumnCount] for mobile
         * With span size for mobile it will be as below:
         * |      5      |
         * |   3   | | 2 |
         */
//            TODO maybe some calculations to get the span size
//            here 10 is number after which sequence will be repeated
//            doing this easy way ;P
        val result = position % 10
        when(result) {
            0, 5 -> gridColumnCount
            1, 4, 7, 8 -> 3
            else -> 2 // result = 2, 3, 6, 9
        }

//            This also works but we have different layout
        /*val result = position % 6
        when {
            (result == 0 || result == 3) -> gridColumnCount
            (result == 1 || result == 5) -> 2
            else -> 1
        }*/
    }
}