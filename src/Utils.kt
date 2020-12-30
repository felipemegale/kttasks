import com.kttasks.models.ApplicationUser

import io.ktor.application.*
import io.ktor.auth.authentication

val ApplicationCall.applicationUser get() = authentication.principal<ApplicationUser>()
