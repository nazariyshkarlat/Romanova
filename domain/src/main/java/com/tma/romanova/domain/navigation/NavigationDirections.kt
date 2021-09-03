package com.tma.romanova.domain.navigation

import com.tma.romanova.domain.navigation.NavigationDirections.Player.Arguments.TRACK_ID

object NavigationDirections {
    object OnBoarding : NavigationCommand() {
        override val arguments = emptyList<Argument>()

        override val destination: Destination =
            destinationOf("on_boarding")
    }

    object MainScreen : NavigationCommand() {
        override val arguments = emptyList<Argument>()

        override val destination: Destination =
            destinationOf("main_screen")
    }

    object AboutAuthor : NavigationCommand() {
        override val arguments = emptyList<Argument>()

        override val destination: Destination =
            destinationOf("about_author")
    }

    object Player : NavigationCommand(){
        object Arguments{
            const val TRACK_ID = "track_id"
        }

        override val arguments = listOf(
            argumentOf(TRACK_ID, isOptional = false, navType = NavType.Int)
        )

        override val destination: Destination =
            destinationOf("player")
    }

}
