package com.example.myapplication.core.utils

class Resource<T>(
    val status: Status,
    val data: T? = null,
    val message: Int? = null){
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data)
        }
        fun <T> error(msg: Int? = null): Resource<T> {
            return Resource(Status.ERROR, message = msg)
        }
        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }
}