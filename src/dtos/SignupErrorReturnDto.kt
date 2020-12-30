package com.kttasks.dtos

import kotlinx.serialization.Serializable

@Serializable
data class SignupErrorReturnDto(val error: String)