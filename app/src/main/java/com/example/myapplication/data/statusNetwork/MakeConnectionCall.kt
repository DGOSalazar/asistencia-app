package com.example.myapplication.data.statusNetwork

import com.example.myapplication.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> makeCall(
    call: suspend () -> T
): ResponseStatus<T> = withContext(Dispatchers.IO) {
    try {
        ResponseStatus.Success(call())
    } catch (e: Throwable) {
        ResponseStatus.Error(R.string.throwable_exception)
    } catch (e: Exception) {
        ResponseStatus.Error(R.string.exception)
    }
}