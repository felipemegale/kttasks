package com.kttasks.routes

import com.kttasks.dtos.*
import com.kttasks.services.AccountService
import com.kttasks.services.TokenService
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
                    SignupErrorReturnDto("username or password invalid")
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
                    SigninErrorReturnDto("invalid username or password")
                )
            }

        }

        authenticate {
            patch("/updatepasswd") {
                return@patch call.respondText("Update password OK", status = HttpStatusCode.OK)
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