package app.mak.pocketnotes.wearos.feature.nowplaying.v2

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.horologist.media.ui.material3.components.PodcastControlButtons
import com.google.android.horologist.media.ui.material3.screens.player.PlayerScreen

@Composable
internal fun NowPlayingScreen() {
    NowPlayingContentV2()
}

@Composable
fun NowPlayingContentV2() {
    PlayerScreen(
        modifier = Modifier.fillMaxSize(),
        mediaDisplay = {},
        controlButtons = {},
        buttons = {
            PodcastControlButtons(
                seekBackButtonEnabled = true,
                seekForwardButtonEnabled = true,
                playPauseButtonEnabled = true,
                playing = true,
//                trackPositionUiModel = ,
                onPlayButtonClick = {},
                onPauseButtonClick = {},
                onSeekBackButtonClick = {},
                onSeekForwardButtonClick = {},
            )
        },
        background = {}
    )
}

@Preview(
    name = "Round Watch",
    device = "id:wearos_small_round",
    showSystemUi = true,
    backgroundColor = 0xFF000000,
    showBackground = true
)
@Composable
private fun NowPlayingContentV2Preview() {
    NowPlayingContentV2()
}