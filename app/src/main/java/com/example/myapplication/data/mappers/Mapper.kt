package com.example.myapplication.data.mappers

interface Mapper<I, O> {
    suspend fun map(input: I): O
}