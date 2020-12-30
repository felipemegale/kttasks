package com.kttasks.dtos

import kotlinx.serialization.Serializable

@Serializable
data class SigninErrorReturnDto(val error: String)