package com.mak.pocketnotes.android.ui.theme

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val roundShapes = Shapes(
    extraSmall = RoundedCornerShape(2.dp),
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

val cutShapes = Shapes(
    extraSmall = CutCornerShape(2.dp),
    small = CutCornerShape(4.dp),
    medium = CutCornerShape(8.dp),
    large = CutCornerShape(16.dp),
    extraLarge = CutCornerShape(24.dp)
)

val LeafyCardShape = RoundedCornerShape(
    topStart = 28.dp,
    topEnd = 12.dp,
    bottomStart = 12.dp,
    bottomEnd = 28.dp
)