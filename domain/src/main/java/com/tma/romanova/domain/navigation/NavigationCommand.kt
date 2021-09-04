package com.tma.romanova.domain.navigation

abstract class NavigationCommand {

    abstract val arguments: List<Argument>
    abstract val destination: Destination

    fun create(arguments: Map<String, Any> = emptyMap()) = CommandResult(route, arguments)

    val route
    get() = destination.toString()

    companion object {
        fun argumentOf(name: String, isOptional: Boolean, navType: NavType) = Argument(name, isOptional, navType)

        fun NavigationCommand.destinationOf(name: String) = Destination(name, arguments)
    }


    data class Argument(val name: String, val isOptional: Boolean, val type: NavType)

    class Destination(private val name: String, private val arguments: List<Argument> = emptyList()){
        override fun toString() = buildString {
            append(name)
            append(formRequiredRoutePart())
            append(formOptionalRoutePart())
        }

        private fun formRequiredArgument(name: String) = "/{${name}}"
        private fun formOptionalArgument(name: String) = "?${name}={${name}}"

        private fun formOptionalRoutePart() =
            arguments
                .filter { it.isOptional }
                .takeIf { it.isNotEmpty() }
                ?.joinToString(prefix = "", postfix = "") {
                    formOptionalArgument(it.name)
                } ?: ""

        private fun formRequiredRoutePart() =
            arguments
                .filter { !it.isOptional }
                .takeIf { it.isNotEmpty() }
                ?.joinToString(prefix = "", postfix = "") {
                    formRequiredArgument(it.name)
                } ?: ""
    }

    class CommandResult(
        val pathTemplate: String,
        val arguments: Map<String, Any> = emptyMap()
    ){

        val isCommandBack = pathTemplate == KEY_NAVIGATE_BACK

        companion object{
            private const val KEY_NAVIGATE_BACK = "<-"

            val navigateBack = CommandResult(
                pathTemplate = KEY_NAVIGATE_BACK
            )
        }

        override fun toString() =
            arguments.toList().fold(initial = pathTemplate) { acc, pair->
                acc.replace("{${pair.first}}", pair.second.toString())
            }
    }

    enum class NavType{
        Int,
        IntArray,
        Long,
        LongArray,
        Float,
        FloatArray,
        Bool,
        BoolArray,
        String,
        StringArray
    }
}
