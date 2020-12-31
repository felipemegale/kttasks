package com.kttasks.dtos

import kotlinx.serialization.Serializable

@Serializable
data class ErrorReturnDto(val error: String)