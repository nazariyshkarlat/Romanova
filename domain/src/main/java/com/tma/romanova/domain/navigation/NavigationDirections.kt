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

    object AboutAuthor : NavigationCommand() {
        override val arguments = emptyList<Argument>()

        override val destination: Destination =
            destinationOf("about_author")
    }

    object Player : NavigationCommand(){
        override val arguments = emptyList<Argument>()

        override val destination: Destination =
            destinationOf("player")
    }

}
