/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package app.mak.pocketnotes.wearos.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import androidx.wear.compose.ui.tooling.preview.WearPreviewFontScales
import app.mak.pocketnotes.wearos.R
import app.mak.pocketnotes.wearos.feature.details.PodcastDetailsScreen
import app.mak.pocketnotes.wearos.feature.home.HomeNavigation
import app.mak.pocketnotes.wearos.feature.home.HomeScreen
import app.mak.pocketnotes.wearos.feature.trendingpodcasts.TrendingPodcastsScreen
import app.mak.pocketnotes.wearos.presentation.theme.WearPocketNotesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearPocketNotesTheme {
                AppScaffold {
                    val listState = rememberTransformingLazyColumnState()
                    val navController = rememberSwipeDismissableNavController()
                    SwipeDismissableNavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {
                        composable("home") {
                            HomeScreen(
                                columnState = listState,
                                contentPadding = PaddingValues(),
                                navigateTo = { navigation ->
                                    when (navigation) {
                                        HomeNavigation.Podcasts -> navController.navigate("trending")
                                        HomeNavigation.Downloads -> navController.navigate("downloads")
                                        HomeNavigation.Subscribed -> navController.navigate("subscribed")
                                        HomeNavigation.Settings -> navController.navigate("settings")
                                    }
                                }
                            )
                        }
                        composable("trending") {
                            TrendingPodcastsScreen(
                                onClick = {
                                    navController.navigate("details")
                                }
                            )
                        }
                        composable("details") {
                            PodcastDetailsScreen()
                        }
                        composable("downloads") {
                            Text("Downloads")
                        }
                        composable("subscribed") {
                            Text("Subscribed")
                        }
                        composable("settings") {
                            Text("Settings")
                        }

                    }
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
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        ),
                ) {
                    Text("More")
                }
            },
        ) { contentPadding -> // ScreenScaffold provides default padding; adjust as needed
            TransformingLazyColumn(contentPadding = contentPadding, state = listState) {
                item {
                    ListHeader(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .transformedHeight(this, transformationSpec),
                        transformation = SurfaceTransformation(transformationSpec),
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
                        transformation = SurfaceTransformation(transformationSpec),
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
                        transformation = SurfaceTransformation(transformationSpec),
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
                        transformation = SurfaceTransformation(transformationSpec),
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