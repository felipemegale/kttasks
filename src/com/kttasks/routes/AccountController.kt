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

            if (newUserId > 0) {
                return@post call.respond(status = HttpStatusCode.Created, SignupReturnDto(newUserId))
            } else {
                return@post call.respond(status = HttpStatusCode.BadRequest, ErrorReturnDto("error creating user"))
            }
        }

        post("/signin") {
            val userInfo = call.receive<UserDto>()
            val token = accountService.signIn(userInfo)

            if (token != null) {
                return@post call.respond(status = HttpStatusCode.OK, SigninReturnDto(userInfo.username, token))
            } else {
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

                if (passwordDidUpdate) {
                    return@patch call.respond(
                        status = HttpStatusCode.OK,
                        SuccessReturnDto("password updated successfully")
                    )
                } else {
                    return@patch call.respond(
                        status = HttpStatusCode.InternalServerError,
                        ErrorReturnDto("error updating password")
                    )
                }
            }

            delete("/removeuser") {
                val username = call.applicationUser?.username ?: return@delete call.respond(
                    status = HttpStatusCode.Unauthorized,
                    ErrorReturnDto("unauthorized")
                )

                val didRemoveUser = accountService.removeUser(username)

                if (didRemoveUser) {
                    return@delete call.respond(status = HttpStatusCode.OK, SuccessReturnDto("user removed successfully"))
                } else {
                    return@delete call.respond(status = HttpStatusCode.InternalServerError, ErrorReturnDto("error removing user"))
                }
            }
        }
    }
}

fun Application.registerAccountRouting(tokenService: TokenService) {
    routing {
        accountRouting(tokenService)
    }
}