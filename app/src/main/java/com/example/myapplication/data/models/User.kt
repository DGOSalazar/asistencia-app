package com.example.myapplication.data.models

data class User(var email: String="",
                var name: String="",
                var lastName1: String="",
                var lastName2: String="",
                var position: String="",
                var birthDate: String="",
                var team: String="",
                var profilePhoto: String="",
                var phone: String="",
                var employee: Int=0,
                var assistDay: List<String> = listOf())