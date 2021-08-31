package com.tma.romanova.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.tma.romanova.core.metadata
import com.tma.romanova.domain.feature.metadata.Metadata
import com.tma.romanova.domain.navigation.NavigationDirections
import com.tma.romanova.domain.navigation.NavigationManager
import com.tma.romanova.domain.navigation.NavigationManager.startDirection
import com.tma.romanova.presentation.extensions.DarkStatusBar
import com.tma.romanova.presentation.extensions.EnterAnimation
import com.tma.romanova.presentation.extensions.LightStatusBar
import com.tma.romanova.presentation.feature.about_author.AboutAuthor
import com.tma.romanova.presentation.feature.main.ui.MainScreen
import com.tma.romanova.presentation.feature.main.view_model.MainScreenViewModel
import com.tma.romanova.presentation.feature.onboarding.ui.OnBoarding
import com.tma.romanova.presentation.feature.onboarding.view_model.OnBoardingViewModel
import com.tma.romanova.presentation.feature.player.Player
import com.tma.romanova.presentation.theme.AppTheme
import kotlinx.coroutines.flow.collect
import org.koin.androidx.compose.getViewModel
import org.koin.core.context.GlobalContext

class MainActivity : ComponentActivity() {
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

                composable(NavigationDirections.Player.route){ backStackEntry ->
                    DarkStatusBar()

                    EnterAnimation {
                        Player()
                    }
                }

            }
        }
    }
}