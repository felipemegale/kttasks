package com.kttasks.routes

import applicationUser
import com.kttasks.dtos.*
import com.kttasks.services.AccountService
import com.kttasks.services.TokenService
import com.kttasks.dtos.ErrorReturnDto
import com.kttasks.dtos.SuccessReturnDto
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.accountRouting(tokenService: TokenService) {
    val accountService = AccountService(tokenService)

    route("/account") {
        get {
            return@get call.respondText("Hello World", status = HttpStatusCode.OK)
        }

        post("/signup") {
            val userInfo = call.receive<UserDto>()

            if (!userInfo.validate()) {
                return@post call.respond(
                    status = HttpStatusCode.BadRequest,
                    ErrorReturnDto("username or password invalid")
                )
            }

            val newUserId = accountService.signUp(userInfo)

            val status = if (newUserId > 0) {
                HttpStatusCode.Created
            } else {
                HttpStatusCode.BadRequest
            }

            return@post call.respond(status = status, SignupReturnDto(newUserId))
        }

        post("/signin") {
            val userInfo = call.receive<UserDto>()
            val token = accountService.signIn(userInfo)

            if (token != null) {
                return@post call.respond(status = HttpStatusCode.OK, SigninReturnDto(userInfo.username, token))
            } else {
                HttpStatusCode.BadRequest
                return@post call.respond(
                    status = HttpStatusCode.BadRequest,
                    ErrorReturnDto("invalid username or password")
                )
            }
        }

        authenticate {
            patch("/updatepasswd") {
                var newPasswordDto = call.receive<NewPasswordDto>()
                val username = call.applicationUser?.username ?: return@patch call.respond(
                    status = HttpStatusCode.Unauthorized,
                    ErrorReturnDto("unauthorized")
                )

                val userInfo = UserDto(username, newPasswordDto.newPassword)

                val passwordDidUpdate = accountService.updatePassword(userInfo)

                val status = if (passwordDidUpdate) {
                    HttpStatusCode.OK
                } else {
                    HttpStatusCode.InternalServerError
                }

                return@patch call.respond(status = status, SuccessReturnDto("password updated successfully"))
            }

            delete("/removeuser") {
                return@delete call.respondText("Delete user OK", status = HttpStatusCode.OK)
            }
        }
    }
}

fun Application.registerAccountRouting(tokenService: TokenService) {
    routing {
        accountRouting(tokenService)
    }
}