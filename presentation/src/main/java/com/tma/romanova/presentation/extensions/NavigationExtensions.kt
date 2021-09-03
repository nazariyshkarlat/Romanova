package com.tma.romanova.presentation.extensions

import android.os.Bundle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.navArgument
import com.tma.romanova.domain.navigation.NavigationCommand
import kotlinx.serialization.builtins.IntArraySerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun NavBackStackEntry.getArgument(
    name: String,
    type: NavigationCommand.NavType
): Any = this.arguments!!.get(name, type)

fun Bundle.get(
    name: String,
    navType: NavigationCommand.NavType): Any =
        when(navType){
            NavigationCommand.NavType.Int -> this.getInt(name)
            NavigationCommand.NavType.IntArray -> this.getIntArray(name)!!
            NavigationCommand.NavType.Long -> this.getLong(name)
            NavigationCommand.NavType.LongArray -> this.getLongArray(name)!!
            NavigationCommand.NavType.Float -> this.getFloat(name)
            NavigationCommand.NavType.FloatArray -> this.getFloatArray(name)!!
            NavigationCommand.NavType.Bool -> this.getBoolean(name)
            NavigationCommand.NavType.BoolArray -> this.getBooleanArray(name)!!
            NavigationCommand.NavType.String -> this.getString(name)!!
            NavigationCommand.NavType.StringArray -> this.getStringArray(name)!!
        }

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


