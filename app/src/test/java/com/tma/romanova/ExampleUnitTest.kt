package com.tma.romanova

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.junit.Test

import org.junit.Assert.*
import kotlin.reflect.KClass

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @InternalSerializationApi
    @Test
    fun addition_isCorrect() {
        DASada<DASada.Abb>(
            values = listOf(
                DASada.Abc(
                  DASada.Abb("fsdfsdf")
                )
            ),
            DASada.Abb::class
        ).print()
    }

    class DASada<T: Any>(val values: List<Abc<T>>, val clazz: KClass<T>) {

        @InternalSerializationApi
        fun print(){
            Json.encodeToString<List<Abc<T>>>(
                ListSerializer(Abc.serializer<T>(clazz.serializer())),
                values
            ).also { println(it) }
        }

        @Serializable
        data class Abc<T>(val abc: T)

        @Serializable
        data class Abb(val value: String)
    }
}