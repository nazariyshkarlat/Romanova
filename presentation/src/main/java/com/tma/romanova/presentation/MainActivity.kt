package com.tma.romanova.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.tma.romanova.core.metadata
import com.tma.romanova.domain.feature.now_playing_track.use_case.NowPlayingTrackInteractor
import com.tma.romanova.domain.navigation.NavigationCommand
import com.tma.romanova.domain.navigation.NavigationDirections
import com.tma.romanova.domain.navigation.NavigationDirections.Player.Arguments.TRACK_ID
import com.tma.romanova.domain.navigation.NavigationManager
import com.tma.romanova.domain.navigation.NavigationManager.startDirection
import com.tma.romanova.presentation.base.BaseViewModel
import com.tma.romanova.presentation.extensions.*
import com.tma.romanova.presentation.feature.about_author.AboutAuthor
import com.tma.romanova.presentation.feature.main.ui.MainScreen
import com.tma.romanova.presentation.feature.main.view_model.MainScreenViewModel
import com.tma.romanova.presentation.feature.onboarding.ui.OnBoarding
import com.tma.romanova.presentation.feature.onboarding.view_model.OnBoardingViewModel
import com.tma.romanova.presentation.feature.player.ui.Player
import com.tma.romanova.presentation.feature.player.view_model.PlayerViewModel
import com.tma.romanova.presentation.theme.AppTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {

    private val nowPlayingTrackInteractor: NowPlayingTrackInteractor by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                ProvideWindowInsets {
                    App()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {

        GlobalScope.launchImmediately {
            nowPlayingTrackInteractor.saveNowPlayingTrack()
        }

        super.onSaveInstanceState(outState)

    }

    @Composable
    private fun App(){

        Box(modifier = Modifier) {
            val navController = rememberNavController()

            LaunchedEffect(true) {
                NavigationManager.command.collect {
                    if (it.pathTemplate.isNotEmpty()) {
                        if (it.isCommandBack) {
                            navController.navigateUp()
                        } else {
                            navController.navigate(it.toString())
                        }
                    }
                }
            }

            NavHost(
                navController,
                startDestination = with(metadata){startDirection}.route
            ) {
                composable(NavigationDirections.OnBoarding.route) { backStackEntry ->
                    LightStatusBar()

                    val viewModel = getViewModel<OnBoardingViewModel>()
                    EnterAnimation {
                        OnBoarding(viewModel)
                    }
                }

                composable(NavigationDirections.MainScreen.route){ backStackEntry ->
                    LightStatusBar()

                    val viewModel = getViewModel<MainScreenViewModel>()
                    EnterAnimation {
                        MainScreen(viewModel = viewModel)
                    }
                }

                composable(NavigationDirections.AboutAuthor.route){ backStackEntry ->
                    DarkStatusBar()

                    EnterAnimation {
                        AboutAuthor()
                    }
                }

                composable(
                    route = NavigationDirections.Player.route,
                    arguments = NavigationDirections.Player.navArguments
                ){ backStackEntry ->
                    DarkStatusBar()

                    EnterAnimation {

                        val viewModel = getViewModel<PlayerViewModel>{
                            parametersOf(
                                backStackEntry.getArgument(
                                    name = TRACK_ID,
                                    type = NavigationCommand.NavType.Int
                                )
                            )
                        }
                        Player(viewModel = viewModel)
                    }
                }

            }
        }
    }
}