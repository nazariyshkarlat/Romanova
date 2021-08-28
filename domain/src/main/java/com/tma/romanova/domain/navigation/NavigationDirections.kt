package com.tma.romanova.domain.navigation

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

}
