/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package app.mak.pocketnotes.wearos.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import androidx.wear.compose.navigation3.rememberSwipeDismissableSceneStrategy
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import androidx.wear.compose.ui.tooling.preview.WearPreviewFontScales
import app.mak.pocketnotes.wearos.R
import app.mak.pocketnotes.wearos.feature.details.WearPodcastDetailsScreen
import app.mak.pocketnotes.wearos.feature.home.HomeNavigation
import app.mak.pocketnotes.wearos.feature.home.WearHomeScreen
import app.mak.pocketnotes.wearos.feature.trendingpodcasts.WearTrendingPodcastsScreen
import app.mak.pocketnotes.wearos.presentation.theme.WearPocketNotesTheme
import kotlinx.serialization.Serializable

@Serializable
sealed interface WearRoute : NavKey {
  @Serializable
  data object HomeRoute : WearRoute

  @Serializable
  data object TrendingPodcastsRoute : WearRoute

  @Serializable
  data class PodcastDetailsRoute(val id: String) : WearRoute

  @Serializable
  data object DownloadsRoute : WearRoute

  @Serializable
  data object SubscribedRoute : WearRoute

  @Serializable
  data object SettingsRoute : WearRoute
}

class WearMainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      WearPocketNotesTheme {
        AppScaffold {
          val listState = rememberTransformingLazyColumnState()
          val backStack = rememberNavBackStack(WearRoute.HomeRoute)
          val strategy = rememberSwipeDismissableSceneStrategy<NavKey>()

          NavDisplay(
            backStack = backStack,
            sceneStrategies = listOf(strategy),
            entryProvider = entryProvider {
              entry<WearRoute.HomeRoute> {
                WearHomeScreen(
                  columnState = listState,
                  navigateTo = { navigation ->
                    when (navigation) {
                      HomeNavigation.Podcasts -> backStack.add(
                        WearRoute.TrendingPodcastsRoute
                      )
                      HomeNavigation.Downloads -> backStack.add(
                        WearRoute.DownloadsRoute
                      )
                      HomeNavigation.Subscribed -> backStack.add(
                        WearRoute.SubscribedRoute
                      )
                      HomeNavigation.Settings -> backStack.add(
                        WearRoute.SettingsRoute
                      )
                    }
                  }
                )
              }
              entry<WearRoute.TrendingPodcastsRoute> {
                WearTrendingPodcastsScreen(
                  state = listState,
                  onClick = { id ->
                    backStack.add(WearRoute.PodcastDetailsRoute(id))
                  }
                )
              }
              entry<WearRoute.PodcastDetailsRoute> { route ->
                WearPodcastDetailsScreen(
                  id = route.id,
                  columnState = listState
                )
              }
              entry<WearRoute.DownloadsRoute> {
                Text("Downloads")
              }
              entry<WearRoute.SubscribedRoute> {
                Text("Subscribed")
              }
              entry<WearRoute.SettingsRoute> {
                Text("Settings")
              }
            }
          )
        }
      }
    }
  }
}

@Composable
fun WearApp(greetingName: String) {
  AppScaffold {
    val listState = rememberTransformingLazyColumnState()
    val transformationSpec = rememberTransformationSpec()
    ScreenScaffold(
      scrollState = listState,
      edgeButton = {
        EdgeButton(
          onClick = { /*TODO*/ },
          colors =
          ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
          )
        ) {
          Text("More")
        }
      }
    ) { contentPadding -> // ScreenScaffold provides default padding; adjust as needed
      TransformingLazyColumn(contentPadding = contentPadding, state = listState) {
        item {
          ListHeader(
            modifier =
            Modifier
              .fillMaxWidth()
              .transformedHeight(this, transformationSpec),
            transformation = SurfaceTransformation(transformationSpec)
          ) {
            Text(text = stringResource(R.string.hello_world, greetingName))
          }
        }
        item {
          Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
              .fillMaxWidth()
              .transformedHeight(this, transformationSpec),
            transformation = SurfaceTransformation(transformationSpec)
          ) {
            Text("Button A")
          }
        }
        item {
          Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
              .fillMaxWidth()
              .transformedHeight(this, transformationSpec),
            transformation = SurfaceTransformation(transformationSpec)
          ) {
            Text("Button B")
          }
        }
        item {
          Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
              .fillMaxWidth()
              .transformedHeight(this, transformationSpec),
            transformation = SurfaceTransformation(transformationSpec)
          ) {
            Text("Button C")
          }
        }
      }
    }
  }
}

@WearPreviewDevices
@WearPreviewFontScales
@Composable
fun DefaultPreview() {
  WearApp("Preview Android")
}
