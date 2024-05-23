package com.mak.pocketnotes.android.feature.search.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme

@Composable
internal fun SearchField(
    onKeyboardDoneClick: (String) -> Unit,
    searchText: String,
    modifier: Modifier = Modifier,
    onSearchTextChanged: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = searchText,
        onValueChange = onSearchTextChanged,
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = stringResource(id = R.string.search)
            )
        },
        trailingIcon = {
            AnimatedVisibility(visible = searchText.isNotBlank()) {
                IconButton(
                    onClick = {
                        onSearchTextChanged("")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = stringResource(R.string.clear_search)
                    )
                }
            }
        },
        placeholder = {
            Text(
                text = stringResource(R.string.search_placeholder)
            )
        },
        singleLine = true,
        keyboardActions = KeyboardActions(onDone = {
            onKeyboardDoneClick(searchText)
        })
    )
}

@Preview
@Composable
private fun SearchFieldPreview() {
    PocketNotesTheme {
        Surface {
            SearchField(
                onKeyboardDoneClick = {},
                modifier = Modifier,
                searchText = "asdasd",
                onSearchTextChanged = {}
            )
        }
    }
}