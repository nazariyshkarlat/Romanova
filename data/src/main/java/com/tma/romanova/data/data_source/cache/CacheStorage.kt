package com.tma.romanova.data.data_source.cache

import android.content.SharedPreferences
import kotlinx.serialization.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import nl.adaptivity.xmlutil.core.impl.multiplatform.name
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.reflect.KClass

abstract class CacheStorage<T: Any>(val clazz: KClass<T>): KoinComponent {

    private val key: String = this::class.name

    private val cache: SharedPreferences by inject()

    fun setElement(data: T, id: String): CacheResult<List<T>> {
        val result = (cache to key).add(CacheElement(id = id, data = data), clazz = clazz)
        return CacheResult.Success(data = result.map { it.data })
    }

    fun setSingleElement(data: T, id: String): CacheResult<T> {
        (cache to key).clear<T>(clazz)
        val result = (cache to key).add(CacheElement(id = id, data = data), clazz = clazz)
        println((cache to key).all(clazz = clazz))
        return CacheResult.Success(data = result.map { it.data }.first())
    }

    fun getElement(id: String) = try{
        CacheResult.Success(data = (cache to key).element<T>(id = id, clazz = clazz)!!.data)
    }catch (e: Exception){
        CacheResult.DataNotFound
    }

    fun getSingleElement() = try{
        CacheResult.Success(data = (cache to key).all<T>(clazz).first().data)
    }catch (e: Exception){
        CacheResult.DataNotFound
    }

    fun getAllElements(): CacheResult<List<T>> = try{
        val elements = (cache to key).all<T>(clazz)
        if(elements.isNotEmpty()) CacheResult.Success(data = elements.map { it.data })
        else CacheResult.DataNotFound
    }catch (e: Exception){
        CacheResult.DataNotFound
    }

    fun clear(): CacheResult<List<T>> {
        return CacheResult.Success(data = (cache to key).clear<T>(clazz).map { it.data })
    }

    fun removeElement(id: String): CacheResult<List<T>> {
        return CacheResult.Success(data = (cache to key).remove<T>(id = id, clazz = clazz).map { it.data })
    }
}

@OptIn(InternalSerializationApi::class)
private val <T: Any> KClass<T>.serializer
get() = ListSerializer(CacheElement.serializer(this.serializer()))

@OptIn(InternalSerializationApi::class)
fun <T: Any> Pair<SharedPreferences, String>.all(clazz: KClass<T>): List<CacheElement<T>>{
    return Json.decodeFromString(
        clazz.serializer,
        first.getString(second, emptyList<CacheElement<T>>().toString()) ?:
        emptyList<CacheElement<T>>().toString()
    )
}

@OptIn(InternalSerializationApi::class)
fun <T: Any> Pair<SharedPreferences, String>.element(id: String, clazz: KClass<T>): CacheElement<T>?{
    return Json.decodeFromString(
        clazz.serializer,
        first.getString(second, emptyList<CacheElement<T>>().toString()) ?:
        emptyList<CacheElement<T>>().toString()
    ).find { it.id == id }
}

fun <T: Any> Pair<SharedPreferences, String>.add(element: CacheElement<T>, clazz: KClass<T>): List<CacheElement<T>>{
    val newArray = all<T>(clazz).filter { it.id != element.id }.plus(element)
    first.edit().putString(second, Json.encodeToString(
        clazz.serializer,
        newArray
    )).apply()
    return newArray
}


fun <T: Any> Pair<SharedPreferences, String>.remove(id: String, clazz: KClass<T>): List<CacheElement<T>>{
    val newArray = all<T>(clazz).filter { it.id != id }
    first.edit().putString(second, Json.encodeToString(
        clazz.serializer,
        newArray
    )).apply()
    return newArray
}

fun <T: Any> Pair<SharedPreferences, String>.clear(clazz: KClass<T>): List<CacheElement<T>>{
    val beforeClear = all<T>(clazz)
    first.edit().putString(second, emptyList<T>().toString()).apply()
    return beforeClear
}

@Serializable
data class CacheElement<T>(
    val id: String,
    val data: T
)