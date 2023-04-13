package com.example.myapplication.data.mappers

import com.example.myapplication.data.datasource.UserRegister
import com.example.myapplication.data.models.User
import com.example.myapplication.data.remote.request.UserRegisterRequest

class UserRequestMapper : Mapper<UserRegister,UserRegisterRequest>{
    override suspend fun map(input: UserRegister): UserRegisterRequest {
        return UserRegisterRequest(
            email = input.email,
            name = input.name,
            lastName1 = input.lastName,
            lastName2 = input.lastName,
            birthDate = input.birthDate,
            position = input.position,
            employee = input.employeeNumber,
            team = input.team,
            profilePhoto = input.profilePhoto,
            assistDay = "user.assistDay"
        )
    }

}