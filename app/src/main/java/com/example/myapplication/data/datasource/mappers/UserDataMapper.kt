package com.example.myapplication.data.datasource.mappers


import com.example.myapplication.data.datasource.UserDTO
import com.example.myapplication.data.models.User

class UserDataMapper {
    fun fromDtoToDomain(userDTO: UserDTO): User {
        return User(
            userDTO.name,
            userDTO.lastName1,
            userDTO.lastName2,
            userDTO.birthDate,
            userDTO.position,
            userDTO.employee,
            userDTO.team,
            userDTO.profilePhoto,
            userDTO.assistDay
        )
    }
}