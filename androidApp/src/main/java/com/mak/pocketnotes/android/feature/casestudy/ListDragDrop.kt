package com.mak.pocketnotes.android.feature.casestudy

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

// 1. State holder to manage complex drag-and-drop calculations
class DragDropState(
    val lazyListState: LazyListState,
    private val onMove: (Int, Int) -> Unit
) {
    var draggedIndex by mutableStateOf<Int?>(null)
        private set
    var dragOffset by mutableFloatStateOf(0f)
        private set

    fun onDragStart(index: Int) {
        draggedIndex = index
    }

    fun onDrag(dragAmountY: Float) {
        dragOffset += dragAmountY
        val currentDraggedIdx = draggedIndex ?: return

        val layoutInfo = lazyListState.layoutInfo
        val currentItemInfo = layoutInfo.visibleItemsInfo
            .firstOrNull { it.index == currentDraggedIdx } ?: return

        val currentItemMidpoint = currentItemInfo.offset + (currentItemInfo.size / 2) + dragOffset

        val targetItem = layoutInfo.visibleItemsInfo.firstOrNull { otherItem ->
            val start = otherItem.offset
            val end = otherItem.offset + otherItem.size
            currentItemMidpoint.toInt() in start..end &&
                    otherItem.index != currentDraggedIdx
        }

        if (targetItem != null) {
            val targetIdx = targetItem.index
            
            // Calculate total size of skipped items to adjust dragOffset accurately
            val skippedItems = layoutInfo.visibleItemsInfo.filter { item ->
                if (targetIdx > currentDraggedIdx) {
                    item.index in (currentDraggedIdx + 1)..targetIdx
                } else {
                    item.index in targetIdx until currentDraggedIdx
                }
            }
            val totalSize = skippedItems.sumOf { it.size }
            
            onMove(currentDraggedIdx, targetIdx)
            
            // Adjust dragOffset based on the size of the item(s) we just skipped over
            if (targetIdx > currentDraggedIdx) {
                dragOffset -= totalSize
            } else {
                dragOffset += totalSize
            }
            draggedIndex = targetIdx
        }
    }

    fun onDragEndOrCancel() {
        draggedIndex = null
        dragOffset = 0f
    }
}

@Composable
fun rememberDragDropState(
    lazyListState: LazyListState,
    onMove: (Int, Int) -> Unit
): DragDropState {
    return remember(lazyListState, onMove) {
        DragDropState(lazyListState, onMove)
    }
}

@Composable
fun HandleDragDropLazyColumn() {
    var itemsList by remember { mutableStateOf((1..20).map { "Item $it" }) }
    val listState = rememberLazyListState()

    val dragDropState = rememberDragDropState(lazyListState = listState) { fromIndex, toIndex ->
        itemsList = itemsList.toMutableList().apply {
            add(toIndex, removeAt(fromIndex))
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        itemsIndexed(itemsList, key = { _, item -> item }) { index, item ->
            val isDragging = index == dragDropState.draggedIndex
            val currentIndex by rememberUpdatedState(index)
            
            // Optimization: Use snap() during active drag for responsiveness, 
            // and spring() for a smooth "settle" animation on release.
            val animatedOffset by animateFloatAsState(
                targetValue = if (isDragging) dragDropState.dragOffset else 0f,
                animationSpec = if (isDragging) snap() else spring(),
                label = "DragOffsetAnimation"
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    // Disable automatic placement animation for the dragged item to avoid conflict with manual translation
                    .animateItem(
                        fadeInSpec = null, 
                        fadeOutSpec = null,
                        placementSpec = if (isDragging) null else spring()
                    )
                    .graphicsLayer {
                        translationY = animatedOffset
                        alpha = if (isDragging) 0.9f else 1f
                        scaleX = if (isDragging) 1.02f else 1f
                        scaleY = if (isDragging) 1.02f else 1f
                    }
                    .zIndex(if (isDragging) 1f else 0f)
                    .padding(vertical = 4.dp)
                    .background(
                        if (isDragging) Color.LightGray.copy(alpha = 0.4f) else Color.DarkGray,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = item, color = Color.White)

                // 2. Drag Handle Icon restricting the pointerInput scope
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Drag Handle",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(8.dp)
                        .pointerInput(item) { // Use 'item' as key to keep the gesture alive during reordering
                            detectDragGesturesAfterLongPress(
                                onDragStart = { dragDropState.onDragStart(currentIndex) },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    dragDropState.onDrag(dragAmount.y)
                                },
                                onDragEnd = { dragDropState.onDragEndOrCancel() },
                                onDragCancel = { dragDropState.onDragEndOrCancel() }
                            )
                        }
                )
            }
        }
    }
}
