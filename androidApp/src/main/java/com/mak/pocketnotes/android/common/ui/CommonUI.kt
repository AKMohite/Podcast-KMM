package com.mak.pocketnotes.android.common.ui

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import com.mak.pocketnotes.android.R

@Composable
internal fun debugPlaceholder(@DrawableRes debugPreview: Int = R.drawable.default_image, @DrawableRes placeHolder: Int? = null) =
    if (LocalInspectionMode.current) {
        painterResource(id = debugPreview)
    } else {
        placeHolder?.let { painterResource(id = it) }
    }