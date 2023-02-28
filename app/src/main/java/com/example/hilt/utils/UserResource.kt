package com.example.hilt.utils

import com.example.hilt.database.entity.UserEntity
import com.example.hilt.models.GithubUser

sealed class UserResource {

    object Loading: UserResource()

    data class Success(val list: List<UserEntity>?): UserResource()

    data class Error(val message: String): UserResource()
}