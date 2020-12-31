package com.kttasks.dtos

import kotlinx.serialization.Serializable

@Serializable
data class SigninReturnDto(val username: String, val token: String)