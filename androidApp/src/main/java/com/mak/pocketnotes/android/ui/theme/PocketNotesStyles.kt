package com.mak.pocketnotes.android.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.style.Style
import androidx.compose.ui.unit.dp

object PocketNotesStyles {
  val artworkStyle = Style {
    width(280.dp)
    height(280.dp)
    shape(RoundedCornerShape(16.dp))
    clip(true)
  }
}
