package com.example.myapplication.data.mappers

import com.example.myapplication.data.models.LoginDomainModel
import com.example.myapplication.data.remote.response.LoginResponse

class LoginResponseMapper : Mapper<LoginResponse,LoginDomainModel>{
    override suspend fun map(input: LoginResponse): LoginDomainModel {
        return LoginDomainModel(
            email = input.email,
            isEmailVerified = input.isEmailVerified
        )
    }
}