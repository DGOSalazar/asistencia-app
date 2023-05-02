package com.example.myapplication.data.datasource.mappers

import com.example.myapplication.data.datasource.Login
import com.example.myapplication.data.datasource.LoginDTO

class LoginMapper {
    fun fromDtoToDomain(loginDTO: LoginDTO): Login {
        return Login(
            loginDTO.email,
            loginDTO.isEmailVerified
        )
    }
}