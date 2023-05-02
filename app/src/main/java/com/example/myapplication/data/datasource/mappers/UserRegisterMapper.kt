package com.example.myapplication.data.datasource.mappers

import com.example.myapplication.data.datasource.UserRegister

class UserRegisterMapper {
    fun fromDtoToDomain(user: UserRegister): HashMap<String, Any> {
        return hashMapOf(
            "email" to user.email,
            "name" to user.name,
            "lastName1" to user.lastName,
            "lastName2" to user.lastName,
            "position" to user.position,
            "birthDate" to user.birthDate,
            "team" to user.team,
            "profilePhoto" to user.profilePhoto,
            "phone" to user.phone,
            "employee" to user.employeeNumber,
            "assistDay" to "user.assistDay"
        )
    }
}