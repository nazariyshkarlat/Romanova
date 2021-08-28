package com.tma.romanova.presentation.extensions

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.navArgument
import com.tma.romanova.domain.navigation.NavigationCommand

inline fun <reified T>NavBackStackEntry.getArgument(name: String) = this.arguments!!.get(name) as T

val NavigationCommand.navArguments
    get() = arguments.map {
        navArgument(name = it.name){
            type = it.type.toComposeNavType()
        }
    }

fun NavigationCommand.NavType.toComposeNavType() = when(this){
    NavigationCommand.NavType.Int -> NavType.IntType
    NavigationCommand.NavType.IntArray -> NavType.IntArrayType
    NavigationCommand.NavType.Long -> NavType.LongType
    NavigationCommand.NavType.LongArray -> NavType.LongArrayType
    NavigationCommand.NavType.Float -> NavType.FloatType
    NavigationCommand.NavType.FloatArray -> NavType.FloatArrayType
    NavigationCommand.NavType.Bool -> NavType.BoolType
    NavigationCommand.NavType.BoolArray -> NavType.BoolArrayType
    NavigationCommand.NavType.String -> NavType.StringType
    NavigationCommand.NavType.StringArray -> NavType.StringArrayType
}


