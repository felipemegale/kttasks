package com.kttasks.dtos

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(val username: String, val password: String)

fun UserDto.validate(): Boolean = username.length > 3 && password.length >= 6