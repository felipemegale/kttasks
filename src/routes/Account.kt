package com.kttasks.routes

import com.kttasks.dtos.*
import com.kttasks.services.AccountService
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

val accountService = AccountService()

fun Route.accountRouting() {
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

            val newUserId = accountService.signUserUp(userInfo)

            val status = if (newUserId > 0) {
                HttpStatusCode.Created
            } else {
                HttpStatusCode.BadRequest
            }

            return@post call.respond(status = status, SignupReturnDto(newUserId))
        }

        post("/signin") {
            val userInfo = call.receive<UserDto>()
            return@post call.respondText("Signin OK", status = HttpStatusCode.OK)
        }

        authenticate {
            patch("/updatepasswd") {
                return@patch call.respondText("Update password OK", status = HttpStatusCode.OK)
            }
        }
    }
}

fun Application.registerAccountRouting() {
    routing {
        accountRouting()
    }
}