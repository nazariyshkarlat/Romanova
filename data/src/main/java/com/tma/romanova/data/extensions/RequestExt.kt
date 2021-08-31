package com.tma.romanova.data.extensions

import com.tma.romanova.data.data_source.network.NetworkResult
import io.ktor.client.features.*
import io.ktor.utils.io.errors.*
import java.net.ConnectException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

suspend fun <T : Any>(suspend () -> T).handleNetworkRequest(): NetworkResult<T> =
    try{
        NetworkResult.Success(this())
    }catch(cause: Exception){
        cause.printStackTrace()
        when(cause){
            is ResponseException -> NetworkResult.ServerError(code = cause.response.status.value, message = cause.message)
            is HttpRequestTimeoutException, is java.net.SocketTimeoutException,
            is UnknownHostException, is SSLException, is EOFException,
            is ConnectException, is io.ktor.utils.io.CancellationException -> NetworkResult.NetworkError
            else -> NetworkResult.LocalException(cause = cause)
        }
    }