package com.example.myapplication.data.mappers

import com.example.myapplication.data.models.UserHomeDomainModel
import com.example.myapplication.data.remote.response.UserHomeResponse

class UserHomeResponseMapper: Mapper<UserHomeDomainModel, UserHomeResponse> {
    override suspend fun map(input: UserHomeDomainModel): UserHomeResponse {
        return UserHomeResponse(
            email = input.email,
            employee = input.employee,
            lastName1 = input.lastName1,
            lastName2 = input.lastName2,
            position = input.position,
            birthDate = input.birthDate,
            team = input.team,
            profilePhoto = input.profilePhoto,
            phone = input.phone,
            name=input.name
        )
    }
}