package com.kttasks.models

import io.ktor.auth.*

data class ApplicationUser(val username: String, val password: String): Principal