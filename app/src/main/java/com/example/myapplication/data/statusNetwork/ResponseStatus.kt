package com.example.myapplication.data.statusNetwork

sealed class ResponseStatus<T> {
    class Loading<T> : ResponseStatus<T>()
    class Success<T>(val data: T) : ResponseStatus<T>()
    class Error<T>(val messageId: Int) : ResponseStatus<T>()
}